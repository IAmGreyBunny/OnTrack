package com.example.ontrack;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainPageControllerHolder {
    private MainPageController mainPageController;

    private final static MainPageControllerHolder INSTANCE = new MainPageControllerHolder();

    private MainPageControllerHolder() {
    }

    public static MainPageControllerHolder getInstance()
    {
        return INSTANCE;
    }

    public MainPageController getMainPageController() {
        return mainPageController;
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

}
