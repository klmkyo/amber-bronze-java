package com.wieik.amberbronze.entities;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.helpers.randomString;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the system.
 */
@DatabaseTable(tableName = "user")
public class User {
    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @DatabaseField(id = true)
    // uzywane UUID zamiast ina - sqlite nie lubi id=0
    private UUID id;

    @DatabaseField(canBeNull = false, unique = true)
    private String name;

    @DatabaseField(canBeNull = false, unique = true)
    private String phoneNumber;

    @DatabaseField(canBeNull = false)
    private String password_hash;

    @DatabaseField(canBeNull = false)
    private String password_salt;

    @DatabaseField(canBeNull = false)
    private UUID defaultAccountId;

    /**
     * Default constructor for the User class.
     * ORMLite needs a no-arg constructor.
     */
    public User() {
    }

   

        /**
         * Constructs a new User object with the specified name, default account balance, phone number, and password.
         *
         * @param name                  the name of the user
         * @param defaultAccountBalance the default account balance of the user
         * @param phoneNumber           the phone number of the user
         * @param password              the password of the user
         */
    
    public User(String name, double defaultAccountBalance, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = UUID.randomUUID();

        // TODO czy mozna to zaimpotrowac ladniej?
        this.password_salt = randomString.generate(16);

        // concat salt and password
        String saltedPassword = password + this.password_salt;

        // hash using built-in java hash function
        this.password_hash = Integer.toString(saltedPassword.hashCode());

        try {
            createDefaultAccount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a default account for the user.
     *
     * @throws SQLException if there is an error creating the account
     */
    public void createDefaultAccount() throws SQLException {
        if (this.id == null) throw new RuntimeException("User id is null.");

        Dao<Account, String> accDao = DaoFactory.getDao(Account.class);

        // if user has any accounts, don't do anything
        boolean userHasAccount = accDao.queryForFirst(accDao.queryBuilder().where().eq("userId", this.id).prepare()) != null;
        if(userHasAccount) {
            return;
        }

        Account account = new Account(this.id, "Konto oszczędnościowe" , 0.0);
        accDao.createIfNotExists(account);

        this.defaultAccountId = account.getId();
    }

    /**
     * Gets the ID of the user.
     *
     * @return the ID of the user
     */
    public UUID getId() {
        return id;
    }

    /**
     * Checks if the provided password matches the user's password.
     *
     * @param password the password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        // concat salt and password
        String saltedPassword = password + this.password_salt;

        // hash using built-in java hash function
        String hashedPassword = Integer.toString(saltedPassword.hashCode());

        return hashedPassword.equals(this.password_hash);
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return a string representation of the User object
     */
    @Override
    public String toString() {
                return "Account(id=" + id + ", name=" + name + ", phone_number=" + phoneNumber + ")";
    }

    /**
     * Gets the default account ID of the user.
     * @return the default account ID of the user
     */
    public UUID getDefaultAccountId() {
        return defaultAccountId;
    }

    /**
     * Sets the default account ID of the user.
     * @param defaultAccountId the default account ID of the user
     */
    public void setDefaultAccountId(UUID defaultAccountId) {
        this.defaultAccountId = defaultAccountId;
    }
}

