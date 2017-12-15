package com.dci.intellij.dbn.connection.session.ui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import java.awt.BorderLayout;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.DBNHeaderForm;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerRef;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;

public class CreateRenameSessionForm extends DBNFormImpl<CreateRenameSessionDialog>{
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JTextField sessionNameTextField;
    private JLabel errorLabel;

    private ConnectionHandlerRef connectionHandlerRef;
    private DatabaseSession session;

    CreateRenameSessionForm(final CreateRenameSessionDialog parentComponent, @NotNull ConnectionHandler connectionHandler, @Nullable final DatabaseSession session) {
        super(parentComponent);
        this.connectionHandlerRef = connectionHandler.getRef();
        this.session = session;
        errorLabel.setForeground(JBColor.RED);
        errorLabel.setIcon(Icons.EXEC_MESSAGES_ERROR);
        errorLabel.setVisible(false);

        DBNHeaderForm headerForm = new DBNHeaderForm(connectionHandler);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);

        final Set<String> sessionNames = connectionHandler.getSessionBundle().getSessionNames();

        String name;
        if (session == null) {
            name = connectionHandler.getName() + " 1";
            while (sessionNames.contains(name)) {
                name = NamingUtil.getNextNumberedName(name, true);
            }
        } else {
            name = session.getName();
            sessionNames.remove(name);
            parentComponent.getOKAction().setEnabled(false);
        }
        sessionNameTextField.setText(name);

        sessionNameTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                String errorText = null;
                String text = StringUtil.trim(sessionNameTextField.getText());

                if (StringUtil.isEmpty(text)) {
                    errorText = "Session name must be specified";
                }
                else if (sessionNames.contains(text)) {
                    errorText = "Session name already in use";
                }


                errorLabel.setVisible(errorText != null);
                parentComponent.getOKAction().setEnabled(errorText == null && (session == null || !session.getName().equals(text)));
                if (errorText != null) {
                    errorLabel.setText(errorText);
                }
            }
        });
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return sessionNameTextField;
    }

    public String getSessionName() {
        return sessionNameTextField.getText();
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandlerRef.get();
    }

    public DatabaseSession getSession() {
        return session;
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }


    @Override
    public void dispose() {
        super.dispose();
        session = null;
    }

}