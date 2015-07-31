package com.dci.intellij.dbn.debugger.jdwp.config.ui;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dci.intellij.dbn.debugger.common.config.ui.DBProgramRunConfigurationEditorForm;
import com.dci.intellij.dbn.debugger.jdwp.config.DBStatementJdwpRunConfig;
import com.dci.intellij.dbn.execution.statement.StatementExecutionInput;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.Range;

public class DBStatementJdwpRunConfigEditorForm extends DBProgramRunConfigurationEditorForm<DBStatementJdwpRunConfig>{
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JCheckBox compileDependenciesCheckBox;
    private JTextField fromPortTextField;
    private JTextField toPortTextField;

    private StatementExecutionInput executionInput;

    public DBStatementJdwpRunConfigEditorForm(final DBStatementJdwpRunConfig configuration) {
        super(configuration);
        if (configuration.isGeneric()) {
            headerPanel.setVisible(false);
        }
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public StatementExecutionInput getExecutionInput() {
        return executionInput;
    }

    public void writeConfiguration(DBStatementJdwpRunConfig configuration) throws ConfigurationException {
        configuration.setCompileDependencies(compileDependenciesCheckBox.isSelected());

        int fromPort = 0;
        int toPort = 0;
        try {
            fromPort = Integer.parseInt(fromPortTextField.getText());
            toPort = Integer.parseInt(toPortTextField.getText());
        } catch (NumberFormatException e) {
            throw new ConfigurationException("TCP Port Range inputs must me numeric");
        }
        configuration.setTcpPortRange(new Range<Integer>(fromPort, toPort));
        //selectMethodAction.setConfiguration(configuration);
    }

    public void readConfiguration(DBStatementJdwpRunConfig configuration) {
        compileDependenciesCheckBox.setSelected(configuration.isCompileDependencies());
        fromPortTextField.setText(String.valueOf(configuration.getTcpPortRange().getFrom()));
        toPortTextField.setText(String.valueOf(configuration.getTcpPortRange().getTo()));
    }

    public void dispose() {
        super.dispose();
        executionInput = null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}