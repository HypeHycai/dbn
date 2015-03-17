package com.dci.intellij.dbn.connection.action;

import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.common.thread.TaskInstructions;
import com.dci.intellij.dbn.connection.ConnectionAction;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

public class ShowDatabaseInformationAction extends DumbAwareAction {
    private ConnectionHandler connectionHandler;

    public ShowDatabaseInformationAction(ConnectionHandler connectionHandler) {
        super("Connection Info", null, null);
        this.connectionHandler = connectionHandler;
        //getTemplatePresentation().setEnabled(connectionHandler.getConnectionStatus().isConnected());
    }

    public void actionPerformed(@NotNull AnActionEvent e) {
        TaskInstructions taskInstructions = new TaskInstructions("Loading database information for " + connectionHandler.getName(), false, false);
        new ConnectionAction("showing database information", connectionHandler, taskInstructions) {
            @Override
            public void execute() {
                Project project = getProject();
                ConnectionManager connectionManager = ConnectionManager.getInstance(project);
                connectionManager.showConnectionInfoDialog(connectionHandler);
            }
        }.start();
    }
}
