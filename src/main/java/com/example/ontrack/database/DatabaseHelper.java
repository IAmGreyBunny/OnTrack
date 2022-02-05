package com.example.ontrack.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {

    //Get size of resultset
    public static int getResultSetSize(ResultSet resultSet) throws SQLException {
        int size = 0;
        if(resultSet!=null)
        {
            resultSet.last();
            size=resultSet.getRow();
            resultSet.first(); //Returns to first for other operations
        }
        return size;
    }
}
