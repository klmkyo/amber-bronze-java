package com.wieik.amberbronze.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

/**
 * Represents a credit card entity.
 */
@DatabaseTable(tableName = "credit_card")
public class CreditCard {
    /**
     * The unique identifier of the credit card.
     */
    @DatabaseField(generatedId = true)
    private UUID id;

    public UUID getAccountId() {
        return accountId;
    }

    /**
     * The unique identifier of the account associated with the credit card.
     */
    @DatabaseField(canBeNull = false)
    private UUID accountId;

    /**
     * The number of the credit card.
     */
    @DatabaseField(canBeNull = false)
    private String number;

    /**
     * The CVV (Card Verification Value) of the credit card.
     */
    @DatabaseField(canBeNull = false)
    private int cvv;

    /**
     * The expiration month of the credit card.
     */
    @DatabaseField(canBeNull = false)
    private int expirationMonth;

    /**
     * The expiration year of the credit card.
     */
    @DatabaseField(canBeNull = false)
    private int expirationYear;

    /**
     * The PIN (Personal Identification Number) of the credit card.
     */
    @DatabaseField(canBeNull = false)
    private int pin;

    /**
     * Returns the unique identifier of the credit card.
     *
     * @return the unique identifier of the credit card
     */
    public UUID getId() {
        return id;
    }

    /**
     * Default constructor required by ORMLite.
     */
    public CreditCard() {
    }

    /**
     * Constructs a new CreditCard object with the specified account ID, number, CVV, expiration month, expiration year, and PIN.
     *
     * @param accountId       the unique identifier of the account associated with the credit card
     * @param number          the number of the credit card
     * @param cvv             the CVV of the credit card
     * @param expirationMonth the expiration month of the credit card
     * @param expirationYear  the expiration year of the credit card
     * @param pin             the PIN of the credit card
     */
    public CreditCard(UUID accountId, String number, int cvv, int expirationMonth, int expirationYear, int pin) {
        this.accountId = accountId;
        this.number = number;
        this.cvv = cvv;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.pin = pin;
    }

    /**
     * Returns the expiration date of the credit card in the format "MM/YYYY".
     *
     * @return the expiration date of the credit card
     */
    public String getExpirationDate() {
        return String.format("%d/%d", expirationMonth, expirationYear);
    }

    /**
     * Returns the CVV of the credit card.
     *
     * @return the CVV of the credit card
     */
    public int getCvv() {
        return cvv;
    }

    /**
     * Returns the PIN of the credit card.
     *
     * @return the PIN of the credit card
     */
    public int getPin() {
        return pin;
    }

    /**
     * Returns a string representation of the CreditCard object.
     *
     * @return a string representation of the CreditCard object
     */
    @Override
    public String toString() {
        return "CreditCard(id=" + id + ", account_uuid=" + accountId
                + ", number=" + number + ", cvv=" + cvv + ", expiration_month=" + expirationMonth
                + ", expiration_year=" + expirationYear + ", pin=" + pin + ")";
    }

   /**
     * Returns the unique identifier of the credit card.
     *
     * @return the unique identifier of the credit card
     */
    public String getNumber() {
        return number;
    }
}
