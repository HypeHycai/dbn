package com.dci.intellij.dbn.connection.config.ui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.common.ui.DBNComboBox;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.ValueSelectorListener;
import com.dci.intellij.dbn.driver.DriverSource;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;


public class ConnectionDriverSettingsForm extends DBNFormImpl<ConnectionDatabaseSettingsForm>{
    private TextFieldWithBrowseButton driverLibraryTextField;
    private DBNComboBox<DriverOption> driverComboBox;
    private JPanel mainPanel;
    private JLabel driverErrorLabel;
    private DBNComboBox<DriverSource> driverSourceComboBox;
    private JPanel driverSetupPanel;

    private static final FileChooserDescriptor LIBRARY_FILE_DESCRIPTOR = new FileChooserDescriptor(false, false, true, true, false, false);

    public ConnectionDriverSettingsForm(@NotNull ConnectionDatabaseSettingsForm parentComponent) {
        super(parentComponent);

        driverSourceComboBox.setValues(DriverSource.BUILTIN, DriverSource.EXTERNAL);
        driverSourceComboBox.addListener(new ValueSelectorListener<DriverSource>() {
            @Override
            public void valueSelected(DriverSource value) {
                boolean isExternalLibrary = value == DriverSource.EXTERNAL;
                driverLibraryTextField.setEnabled(isExternalLibrary);
                driverComboBox.setEnabled(isExternalLibrary);
                //driverSetupPanel.setVisible(isExternalLibrary);
            }
        });

        driverLibraryTextField.addBrowseFolderListener(
                "Select driver library",
                "Library must contain classes implementing the 'java.sql.Driver' class.",
                null, LIBRARY_FILE_DESCRIPTOR);
    }

    public TextFieldWithBrowseButton getDriverLibraryTextField() {
        return driverLibraryTextField;
    }

    public DBNComboBox<DriverOption> getDriverComboBox() {
        return driverComboBox;
    }

    public JLabel getDriverErrorLabel() {
        return driverErrorLabel;
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public DBNComboBox<DriverSource> getDriverSourceComboBox() {
        return driverSourceComboBox;
    }
}
