package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DevTestConnectionAction extends AbstractConnectionAction{
    DevTestConnectionAction(@NotNull ConnectionHandler connectionHandler) {
        super("Dev Test", connectionHandler);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connectionHandler) {

    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable ConnectionHandler connectionHandler) {
        presentation.setVisible(DatabaseNavigator.DEVELOPER);
    }
}
