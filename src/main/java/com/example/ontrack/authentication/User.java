package com.example.ontrack.authentication;

import com.example.ontrack.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    private String username;
    private String email;
    private String password;

    User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Creates a user entry in database, returns a true if entry is created successfully
    public static Boolean registerUser(String username,String email,String password)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

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
    public static User authenticateUser(String email,String password)
    {
        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM user WHERE (email = '%s' AND password = '%s')",email,password);
        try{
            System.out.println();
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
                User user = new User(resultSet.getString("username"),resultSet.getString("email"),resultSet.getString("password"));
                return user;
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public int getUid()
    {
        //Connect to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        //Look for user in database, and return userid if it exists
        String sql = String.format("SELECT * FROM user WHERE (email = '%s' AND password = '%s')",email,password);
        try{
            System.out.println();
            System.out.println("Executing Query:\n" + sql);
            PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();

            //Check if user exists
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

            //If user exist, return the uid
            if (size>=1)
            {
                int uid = resultSet.getInt("userId");
                return uid;
            }
            else
            {
                return 0;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
