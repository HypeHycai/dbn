package com.dci.intellij.dbn.database.common.metadata.impl;

import com.dci.intellij.dbn.database.common.metadata.DBObjectMetadataBase;
import com.dci.intellij.dbn.database.common.metadata.def.DBCharsetMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCharsetMetadataImpl extends DBObjectMetadataBase implements DBCharsetMetadata {

    public DBCharsetMetadataImpl(ResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public String getCharsetName() throws SQLException {
        return resultSet.getString("CHARSET_NAME");
    }

    @Override
    public short getMaxLength() throws SQLException {
        return resultSet.getShort("MAX_LENGTH");
    }
}
