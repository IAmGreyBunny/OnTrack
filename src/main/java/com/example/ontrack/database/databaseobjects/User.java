package com.example.ontrack.database.databaseobjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    private String username;
    private String email;
    private String password;

    //Creates a user entry in database, returns a true if entry is created successfully
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

    //Checks if email and password combination exists
    public static Boolean authenticateUser(Connection connection,String email,String password)
    {
        String sql = String.format("SELECT * FROM user WHERE (email = '%s' AND password = '%s')",email,password);
        try{
            System.out.println("Executing Query:\n" + sql);
            PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            int size = 0;
            if(resultSet!=null)
            {
                resultSet.last();
                size=resultSet.getRow();
                System.out.println("Result contains: "+size);
            }
            else
            {
                System.out.println("Query Fails");
            }

            if (size>=1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
