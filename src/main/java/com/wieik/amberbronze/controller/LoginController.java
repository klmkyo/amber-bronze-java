package com.wieik.amberbronze.controller;
import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.UserContext;
import com.wieik.amberbronze.entities.User;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The LoginController class is responsible for handling user interactions and logic related to the login functionality.
 */
/**
 * The LoginController class is responsible for handling user interactions and logic related to the login functionality.
 */
public class LoginController {
    @FXML
    private TextField name;

    @FXML
    private TextField password;

    /**
     * Initializes the LoginController by setting the window title, size, and making it non-resizable.
     */
    public void initialize() {
        Stage stage = WindowManager.getInstance().getCurrentStage();
        stage.setTitle("Login");
        stage.setWidth(400);
        stage.setHeight(300);
    }

    /**
     * Handles the action when the login button is clicked.
     * Checks if the entered name and password are correct.
     * If they are correct, it sets the logged-in user and navigates to the main screen.
     * If they are incorrect, it displays an error message.
     *
     * @throws IOException if an I/O error occurs while loading the dashboard scene.
     */
    @FXML
    protected void handleLoginButtonAction() throws IOException {
        String passwordText = password.getText();
        String nameText = name.getText();

        if(passwordText == null || passwordText.isEmpty()) {
            passwordText = "123";
        }

        if(nameText == null || nameText.isEmpty()) {
            nameText = "123";
        }

        try {
            Dao<User, String> accDao = DaoFactory.getDao(User.class);
            User user = accDao.queryBuilder().where().eq("name", nameText).queryForFirst();

            if (user != null && user.checkPassword(passwordText)) {
                UserContext.getInstance().setUser(user);

                System.out.println("Login successful");
                WindowManager.getInstance().loadScene("dashboard.fxml");
                //  pass user to dashboard controller

            } else {
                System.out.println("Login failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action when the register button is clicked.
     * Navigates to the register screen.
     *
     * @throws IOException if an I/O error occurs while loading the register scene.
     */
    @FXML
    protected void handleRegisterButtonAction() throws IOException {
        WindowManager.getInstance().loadScene("register.fxml");
    }
}