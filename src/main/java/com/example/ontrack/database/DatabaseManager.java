package com.example.ontrack.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {
    public Connection databaseLink;

    //Connects to database and returns connection object
    public Connection getConnection()
    {
        //Information about database
        String databaseName = "ontrack";
        String databaseUser = "root";
        String databasePassword = "Password1234";
        String url = "jdbc:mysql://localhost/"+databaseName;

        //Attempts to connect to database
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
            System.out.println("Successfully Connected to Database");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return databaseLink;
    }

}
