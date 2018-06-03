package com.dci.intellij.dbn.connection.console;

import com.dci.intellij.dbn.common.dispose.DisposableBase;
import com.dci.intellij.dbn.common.dispose.DisposerUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerRef;
import com.dci.intellij.dbn.vfs.DBConsoleType;
import com.dci.intellij.dbn.vfs.file.DBConsoleVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseConsoleBundle extends DisposableBase {
    private ConnectionHandlerRef connectionHandlerRef;

    private List<DBConsoleVirtualFile> consoles = new CopyOnWriteArrayList<DBConsoleVirtualFile>();

    public DatabaseConsoleBundle(ConnectionHandler connectionHandler) {
        super(connectionHandler);
        this.connectionHandlerRef = connectionHandler.getRef();
    }

    public List<DBConsoleVirtualFile> getConsoles() {
        if (consoles.size() == 0) {
            synchronized (this) {
                if (consoles.size() == 0) {
                    createConsole(getConnectionHandler().getName(), DBConsoleType.STANDARD);
                }
            }

        }
        return consoles;
    }

    public Set<String> getConsoleNames() {
        Set<String> consoleNames = new HashSet<String>();
        for (DBConsoleVirtualFile console : consoles) {
            consoleNames.add(console.getName());
        }

        return consoleNames;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandlerRef.get();
    }

    @NotNull
    public DBConsoleVirtualFile getDefaultConsole() {
        return getConsole(getConnectionHandler().getName(), DBConsoleType.STANDARD, true);
    }

    @Nullable
    public DBConsoleVirtualFile getConsole(String name) {
        for (DBConsoleVirtualFile console : consoles) {
            if (console.getName().equals(name)) {
                return console;
            }
        }
        return null;
    }

    public DBConsoleVirtualFile getConsole(String name, DBConsoleType type, boolean create) {
        DBConsoleVirtualFile console = getConsole(name);
        if (console == null && create) {
            synchronized (this) {
                console = getConsole(name);
                if (console == null) {
                    return createConsole(name, type);
                }
            }
        }
        return console;
    }

    public DBConsoleVirtualFile createConsole(String name, DBConsoleType type) {
        ConnectionHandler connectionHandler = getConnectionHandler();
        DBConsoleVirtualFile console = new DBConsoleVirtualFile(connectionHandler, name, type);
        consoles.add(console);
        Collections.sort(consoles);
        return console;
    }

    public void removeConsole(String name) {
        DBConsoleVirtualFile console = getConsole(name);
        consoles.remove(console);
        DisposerUtil.dispose(console);
    }

    @Override
    public void dispose() {
        super.dispose();
        DisposerUtil.dispose(consoles);
    }

    public void renameConsole(String oldName, String newName) {
        DBConsoleVirtualFile console = getConsole(oldName);
        if (console != null) {
            console.setName(newName);
        }
    }
}
