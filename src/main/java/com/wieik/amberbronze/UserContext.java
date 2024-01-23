package com.wieik.amberbronze;

import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.User;

/**
 * The UserContext class represents the context of the current user in the application.
 * It provides access to the user, current account, and current credit card information.
 * This class follows the Singleton design pattern to ensure that only one instance of UserContext exists.
 */
/**
 * The UserContext class represents the context of the current user in the application.
 * It provides access to the user's account, credit card, and user information.
 */
public class UserContext {
    private static UserContext instance = null;
    private User user;
    private Account currentAccount;
    private CreditCard currentCreditCard;

    private UserContext() { }

    /**
     * Returns the singleton instance of the UserContext class.
     * If the instance does not exist, a new one is created.
     *
     * @return The singleton instance of UserContext.
     */
    public static UserContext getInstance() {
        if (instance == null) {
            instance = new UserContext();
        }
        return instance;
    }

    /**
     * Returns the current account associated with the user.
     *
     * @return The current account.
     */
    public Account getCurrentAccount() {
        return getInstance().currentAccount;
    }

    /**
     * Sets the current account for the user.
     *
     * @param account The account to set as the current account.
     */
    public void setCurrentAccount(Account account) {
        getInstance().currentAccount = account;
    }

    /**
     * Returns the current credit card associated with the user.
     *
     * @return The current credit card.
     */
    public CreditCard getCurrentCreditCard() {
        return getInstance().currentCreditCard;
    }

    /**
     * Sets the current credit card for the user.
     *
     * @param creditCard The credit card to set as the current credit card.
     */
    public void setCurrentCreditCard(CreditCard creditCard) {
        getInstance().currentCreditCard = creditCard;
    }

    /**
     * Sets the user for the UserContext.
     *
     * @param user The user to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the user associated with the UserContext.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Clears the user from the UserContext.
     */
    public void clearUser() {
        user = null;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise.
     */
    public boolean isUserLoggedIn() {
        return user != null;
    }
}
