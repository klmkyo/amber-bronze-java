package com.wieik.amberbronze.logic.transfers;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.Transaction.TransactionType;
import com.wieik.amberbronze.logic.Transfer;

import java.sql.SQLException;

/**
 * Represents a direct transfer of funds from one account to another using a card number.
 * Extends the Transfer class.
 */
public class DirectTransfer extends Transfer {
    private final String cardNumber;

    /**
     * Constructs a DirectTransfer object with the specified amount and card number.
     *
     * @param amount     the amount of funds to transfer
     * @param cardNumber the card number of the recipient account
     */
    public DirectTransfer(double amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }

    /**
     * Returns the log message for the direct transfer.
     *
     * @return the log message
     */
    @Override
    public String getLogMessage() {
        String sb = "Transfer bezpośredni" +
                " od konta " + senderAccount.getId() +
                " na nr. karty " + cardNumber;

        return sb;
    }

    /**
     * Returns the transaction type of the direct transfer.
     *
     * @return the transaction type
     */
    @Override
    protected TransactionType getType() {
        return TransactionType.DIRECT;
    }

    /**
     * Finds and returns the recipient account based on the card number.
     *
     * @return the recipient account, or null if not found
     */
    @Override
    protected Account findRecipient() {
        // Implement finding recipient here
        Dao<Account, String> accDao = DaoFactory.getDao(Account.class);
        try {
            return accDao.queryForFirst(accDao.queryBuilder().where().eq("cardNumber", cardNumber).prepare());
        } catch (SQLException e) {
            return null;
        }
    }
}
