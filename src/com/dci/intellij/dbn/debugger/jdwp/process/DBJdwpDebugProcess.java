package com.dci.intellij.dbn.debugger.jdwp.process;

import java.net.Inet4Address;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;
import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.common.notification.NotificationUtil;
import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerRef;
import com.dci.intellij.dbn.database.DatabaseDebuggerInterface;
import com.dci.intellij.dbn.debugger.DBDebugConsoleLogger;
import com.dci.intellij.dbn.debugger.DBDebugOperationTask;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.debugger.common.breakpoint.DBBreakpointHandler;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfig;
import com.dci.intellij.dbn.debugger.common.process.DBDebugProcess;
import com.dci.intellij.dbn.debugger.common.process.DBDebugProcessStatus;
import com.dci.intellij.dbn.debugger.jdwp.DBJdwpBreakpointHandler;
import com.dci.intellij.dbn.debugger.jdwp.ManagedThreadCommand;
import com.dci.intellij.dbn.debugger.jdwp.frame.DBJdwpDebugSuspendContext;
import com.dci.intellij.dbn.execution.ExecutionInput;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.DBProgram;
import com.dci.intellij.dbn.object.DBSchema;
import com.intellij.debugger.engine.JavaDebugProcess;
import com.intellij.debugger.engine.JavaStackFrame;
import com.intellij.debugger.impl.DebuggerContextImpl;
import com.intellij.debugger.impl.DebuggerContextListener;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebugSessionAdapter;
import com.intellij.xdebugger.XDebugSessionListener;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.intellij.xdebugger.impl.XDebugSessionImpl;
import com.sun.jdi.Location;

public abstract class DBJdwpDebugProcess<T extends ExecutionInput> extends JavaDebugProcess implements DBDebugProcess {
    protected Connection targetConnection;
    private T executionInput;
    private ConnectionHandlerRef connectionHandlerRef;
    private DBDebugProcessStatus status = new DBDebugProcessStatus();
    private int localTcpPort = 4000;

    private DBBreakpointHandler<DBJdwpDebugProcess>[] breakpointHandlers;
    private DBDebugConsoleLogger console;

    private transient XSuspendContext lastSuspendContext;

    private XDebugSessionListener suspendContextOverwriteListener = new XDebugSessionAdapter() {
        @Override
        public void sessionPaused() {
            final XDebugSession session = getSession();
            final XSuspendContext suspendContext = session.getSuspendContext();
            if (suspendContext instanceof DBJdwpDebugSuspendContext) {

            } else if (suspendContext != lastSuspendContext){
                if (shouldSuspend(suspendContext)) {
                    lastSuspendContext = suspendContext;
                    final DBJdwpDebugSuspendContext dbSuspendContext = new DBJdwpDebugSuspendContext(DBJdwpDebugProcess.this, suspendContext);
                    new ManagedThreadCommand(getDebuggerSession().getProcess()) {
                        @Override
                        protected void action() throws Exception {
                            session.positionReached(dbSuspendContext);
                        }
                    }.invoke();
                    //throw AlreadyDisposedException.INSTANCE;
                } else {
                    new SimpleLaterInvocator() {
                        @Override
                        protected void execute() {
                            session.resume();
                        }
                    }.start();
                }
            }
        }
    };

    protected boolean shouldSuspend(XSuspendContext suspendContext) {
        XExecutionStack executionStack = suspendContext.getActiveExecutionStack();
        if (executionStack != null) {
            VirtualFile virtualFile = getVirtualFile(executionStack.getTopFrame());
            return virtualFile != null;
        }
        return true;
    }


    protected DBJdwpDebugProcess(@NotNull final XDebugSession session, DebuggerSession debuggerSession, ConnectionHandler connectionHandler, int tcpPort) {
        super(session, debuggerSession);
        console = new DBDebugConsoleLogger(session);
        this.connectionHandlerRef = ConnectionHandlerRef.from(connectionHandler);
        Project project = session.getProject();
        DatabaseDebuggerManager debuggerManager = DatabaseDebuggerManager.getInstance(project);
        debuggerManager.registerDebugSession(connectionHandler);

        DBRunConfig<T> runProfile = (DBRunConfig<T>) session.getRunProfile();
        executionInput = runProfile.getExecutionInput();

        DBJdwpBreakpointHandler breakpointHandler = new DBJdwpBreakpointHandler(session, this);
        breakpointHandlers = new DBBreakpointHandler[]{breakpointHandler};

        getDebuggerSession().getContextManager().addListener(new DebuggerContextListener() {
            @Override
            public void changeEvent(DebuggerContextImpl newContext, int event) {
                //System.out.println();
            }
        });
        localTcpPort = tcpPort;
    }

    public int getLocalTcpPort() {
        return localTcpPort;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandlerRef.get();
    }

    public T getExecutionInput() {
        return executionInput;
    }

    @NotNull
    public Project getProject() {
        return getSession().getProject();
    }

    public DatabaseDebuggerInterface getDebuggerInterface() {
        return getConnectionHandler().getInterfaceProvider().getDebuggerInterface();
    }

    public Connection getTargetConnection() {
        return targetConnection;
    }

    public DBDebugProcessStatus getStatus() {
        return status;
    }

    @NotNull
    @Override
    public DBBreakpointHandler<DBJdwpDebugProcess>[] getBreakpointHandlers() {
        return breakpointHandlers;
    }

    public DBBreakpointHandler<DBJdwpDebugProcess> getBreakpointHandler() {
        return breakpointHandlers[0];
    }

    @Override
    public boolean checkCanInitBreakpoints() {
        return status.CAN_SET_BREAKPOINTS;
    }

    public DBDebugConsoleLogger getConsole() {
        return console;
    }

    @Override
    public void sessionInitialized() {
        final XDebugSession session = getSession();
        if (session instanceof XDebugSessionImpl) {
            XDebugSessionImpl sessionImpl = (XDebugSessionImpl) session;
            sessionImpl.getSessionData().setBreakpointsMuted(false);
        }
        session.addSessionListener(suspendContextOverwriteListener);
        getDebuggerSession().getProcess().setXDebugProcess(this);
        getDebuggerSession().getContextManager().addListener(new DebuggerContextListener() {
            @Override
            public void changeEvent(DebuggerContextImpl newContext, int event) {
                System.out.println(newContext);
            }
        });

        new DBDebugOperationTask(getProject(), "initialize debug environment") {
            public void execute() {
                try {
                    ConnectionHandler connectionHandler = getConnectionHandler();
                    targetConnection = connectionHandler.getPoolConnection(executionInput.getExecutionContext().getTargetSchema());
                    targetConnection.setAutoCommit(false);
                    DatabaseDebuggerInterface debuggerInterface = getDebuggerInterface();
                    debuggerInterface.initializeJdwpSession(targetConnection, Inet4Address.getLocalHost().getHostAddress(), String.valueOf(localTcpPort));
                    console.system("Debug session initialized (JDWP)");

                    status.CAN_SET_BREAKPOINTS = true;
                    startTargetProgram();
                } catch (Exception e) {
                    status.SESSION_INITIALIZATION_THREW_EXCEPTION = true;
                    session.stop();
                    NotificationUtil.sendErrorNotification(getProject(), "Error initializing debug environment.", e.getMessage());
                }
            }
        }.start();
    }

    private void startTargetProgram() {
        new BackgroundTask(getProject(), "Running debugger target program", true, true) {
            @Override
            protected void execute(@NotNull ProgressIndicator progressIndicator) throws InterruptedException {
                console.system("Executing target program");
                if (status.PROCESS_IS_TERMINATING) return;
                if (status.SESSION_INITIALIZATION_THREW_EXCEPTION) return;
                try {
                    status.TARGET_EXECUTION_STARTED = true;
                    executeTarget();
                } catch (SQLException e){
                    status.TARGET_EXECUTION_THREW_EXCEPTION = true;
                    MessageUtil.showErrorDialog(getProject(), "Error executing " + executionInput.getExecutionContext().getTargetName(), e);
                } finally {
                    status.TARGET_EXECUTION_TERMINATED = true;
                    DatabaseDebuggerInterface debuggerInterface = getDebuggerInterface();
                    try {
                        debuggerInterface.disconnectJdwpSession(targetConnection);
                    } catch (SQLException e) {
                        console.error("Error disconnecting session: " + e.getMessage());
                    }
                }
            }
        }.start();
    }

    protected abstract void executeTarget() throws SQLException;

    @Override
    public void stop() {
        super.stop();
        stopDebugger();
    }

    private void stopDebugger() {
        final Project project = getProject();
        new BackgroundTask(project, "Stopping debugger", true) {
            @Override
            protected void execute(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setText("Stopping debug environment.");
                ConnectionHandler connectionHandler = getConnectionHandler();
                try {
                    status.CAN_SET_BREAKPOINTS = false;
                    if (!status.TARGET_EXECUTION_TERMINATED) {
                        DatabaseDebuggerInterface debuggerInterface = getDebuggerInterface();
                        debuggerInterface.disconnectJdwpSession(targetConnection);
                    }

                } catch (final SQLException e) {
                    NotificationUtil.sendErrorNotification(getProject(), "Error stopping debugger.", e.getMessage());
                    //showErrorDialog(e);
                } finally {
                    status.PROCESS_IS_TERMINATED = true;
                    DatabaseDebuggerManager.getInstance(project).unregisterDebugSession(connectionHandler);
                    releaseTargetConnection();
                }
            }
        }.start();
    }


    protected void releaseTargetConnection() {
        ConnectionHandler connectionHandler = getConnectionHandler();
        connectionHandler.dropPoolConnection(targetConnection);
        targetConnection = null;
    }

    public VirtualFile getVirtualFile(XStackFrame stackFrame) {
        try {
            Location location = ((JavaStackFrame) stackFrame).getDescriptor().getLocation();
            if (location != null) {
                int lineNumber = location.lineNumber();
                String sourcePath = location.sourcePath();
                StringTokenizer tokenizer = new StringTokenizer(sourcePath, "\\.");
                String signature = tokenizer.nextToken();
                String programType = tokenizer.nextToken();
                String schemaName = tokenizer.nextToken();
                String programName = tokenizer.nextToken();
                DBSchema schema = getConnectionHandler().getObjectBundle().getSchema(schemaName);
                if (schema != null) {
                    DBProgram program = schema.getProgram(programName);
                    if (program != null) {
                        return program.getVirtualFile();
                    } else {
                        DBMethod method = schema.getMethod(programName, 0);
                        if (method != null) {
                            return method.getVirtualFile();
                        }
                    }
                }
            }
        } catch (Exception e) {
            getConsole().error("Error evaluating susped position: " + e.getMessage());
        }

        return null;
    }

    public String getOwnerName(XStackFrame stackFrame) {
        try {
            Location location = ((JavaStackFrame) stackFrame).getDescriptor().getLocation();
            if (location != null) {
                String sourcePath = location.sourcePath();
                StringTokenizer tokenizer = new StringTokenizer(sourcePath, "\\.");
                String signature = tokenizer.nextToken();
                String programType = tokenizer.nextToken();
                return tokenizer.nextToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}