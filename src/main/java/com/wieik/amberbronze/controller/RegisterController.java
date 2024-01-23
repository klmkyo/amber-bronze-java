package com.wieik.amberbronze.controller;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.entities.User;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The RegisterController class is responsible for handling user registration functionality.
 */
public class RegisterController {
    /**
     * The name TextField for entering the user's name.
     */
    @FXML
    private TextField name;

    /**
     * The phoneNumber TextField for entering the user's phone number.
     */
    @FXML
    private TextField phoneNumber;

    /**
     * The password TextField for entering the user's password.
     */
    @FXML
    private TextField password;

    /**
     * The password2 TextField for confirming the user's password.
     */
    @FXML
    private TextField password2;

    /**
     * The messageLabel Label for displaying registration messages.
     */
    @FXML
    private Label messageLabel;

    /**
     * Handles the password input and checks if the passwords match.
     */
    private void handlePasswordInput() {
        String passwordText = password.getText();
        String password2Text = password2.getText();

        if (passwordText.equals(password2Text)) {
            setMessage(null);
        } else {
            setMessage("Passwords do not match", false);
        }
    }

    /**
     * Initializes the RegisterController.
     */
    public void initialize() {
        Stage stage = WindowManager.getInstance().getCurrentStage();
        stage.setTitle("Register");
        stage.setWidth(400);
        stage.setHeight(300);

        password.setOnKeyTyped(event -> handlePasswordInput());
        password2.setOnKeyTyped(event -> handlePasswordInput());
    }

    /**
     * Sets the message to be displayed in the messageLabel.
     *
     * @param text      the text of the message
     */
    public void setMessage(String text) {
        setMessage(text, true);
    }

    /**
     * Sets the message to be displayed in the messageLabel and specifies if it is a success message.
     *
     * @param text      the text of the message
     * @param isSuccess true if it is a success message, false otherwise
     */
    public void setMessage(String text, Boolean isSuccess) {
        if(text == null) {
            messageLabel.setVisible(false);
            messageLabel.setManaged(false);
            return;
        }

        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
        messageLabel.setText(text);
        if (isSuccess) {
            messageLabel.setStyle("-fx-text-fill: green");
        } else {
            messageLabel.setStyle("-fx-text-fill: red");
        }
    }

    /**
     * Handles the action when the register button is clicked.
     * Validates the input fields and registers the user if all conditions are met.
     */
    @FXML
    protected void handleRegisterButtonAction() {
        String nameText = name.getText();
        String phoneNumberText = phoneNumber.getText();

        String passwordText = password.getText();
        String password2Text = password2.getText();

        if (!passwordText.equals(password2Text)) {
            setMessage("Passwords do not match", false);
            return;
        }

        if (nameText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty()) {
            setMessage("Name or phone number is empty", false);
            return;
        }

        try {
            Dao<User, String> accDao = DaoFactory.getDao(User.class);

            boolean userAlreadyExists = accDao.queryBuilder().where().eq("name", nameText).queryForFirst() != null;
            if (userAlreadyExists) {
                setMessage("User with this name already exists", false);
                return;
            }

            User user = new User(nameText, 0, phoneNumberText, passwordText);
            accDao.create(user);
            setMessage("Registration successful");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action when the return button is clicked.
     * Loads the login scene.
     *
     * @throws IOException if an I/O error occurs
     */
    public void handleReturnAction() throws IOException {
        WindowManager.getInstance().loadScene("login.fxml");
    }
}
