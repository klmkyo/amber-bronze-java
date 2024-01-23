package com.wieik.amberbronze.controller.dialog;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.UserContext;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * The controller class for creating a new account.
 * Extends the DialogController class.
 */
public class NewAccountController extends DialogController {
    @FXML
    TextField accountName;

    /**
     * Handles the action when the new account button is clicked.
     * Creates a new account using the entered account name and the current user's ID.
     * Calls the create method of the Account DAO to persist the account in the database.
     * Closes the dialog after creating the account.
     */
    @FXML
    private void handleNewAccountButtonAction() {
        Dao<Account, String> accDao = DaoFactory.getDao(Account.class);
        User currentUser = UserContext.getInstance().getUser();
        Account account = new Account(currentUser.getId(), accountName.getText(), 0.0);

        try {
            accDao.create(account);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDialog();
    }
}
