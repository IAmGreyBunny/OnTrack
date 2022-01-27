package com.example.ontrack.database.databaseobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    private String username;
    private String email;
    private String password;

    public static Boolean registerUser(Connection connection,String username,String email,String password)
    {
        //Creates query from user account information
        String sql = String.format("INSERT INTO user(username,email,password) VALUES ('%s','%s','%s')",username,email,password);
        try{
            System.out.println("Executing Query:\n" + sql);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
