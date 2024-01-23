package com.wieik.amberbronze.entities;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.wieik.amberbronze.dao.DaoFactory;

import java.util.UUID;

/**
 * Represents an account entity in the system.
 */
@DatabaseTable(tableName = "account")
public class Account {
    /**
     * The unique identifier of the account.
     */
    @DatabaseField(generatedId = true)
    private UUID id;

    @DatabaseField(canBeNull = false)
    private UUID userId;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private double balance;

    /**
     * Default constructor for Account.
     * ORMLite needs a no-arg constructor.
     */
    public Account() {
    }

    /**
     * Constructor for Account with specified user ID, name, and balance.
     * 
     * @param userId  the user ID associated with the account
     * @param name    the name of the account
     * @param balance the balance of the account
     */
    public Account(UUID userId, String name, double balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    /**
     * Enum representing the status of parsing account values.
     */
    public enum ParsingAccountValuesStatus {
        SUCCESS,
        FIELDS_EMPTY,
        BALANCE_OUT_OF_RANGE
    }

    /**
     * Sets the balance of the account and updates the database.
     *
     * @param balance the new balance of the account
     */
    public void setBalance(double balance) {
        this.balance = balance;
        // update database
        Dao<Account, String> accDao = DaoFactory.getDao(Account.class);
        UpdateBuilder<Account, String> updateBuilder = accDao.updateBuilder();
        try {
            updateBuilder.updateColumnValue("balance", balance);
            updateBuilder.where().eq("id", id);
            updateBuilder.update();
        } catch (Exception e) {
            // handle exception
        }
    }

    /**
     * Returns the balance of the account.
     *
     * @return the balance of the account
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the ID of the account.
     *
     * @return the ID of the account
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the user ID associated with the account.
     *
     * @return the user ID associated with the account
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Returns the name of the account.
     *
     * @return the name of the account
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the Account object.
     *
     * @return a string representation of the Account object
     */
    @Override
    public String toString() {
        return "Account(id=" + id + ", name=" + name + ", user=" + userId + ", balance=" + balance + ")";
    }
}