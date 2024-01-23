package com.wieik.amberbronze.logic.transfers;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.Transaction.TransactionType;
import com.wieik.amberbronze.entities.User;
import com.wieik.amberbronze.logic.Transfer;

import java.sql.SQLException;

/**
 * Represents a BLIK transfer, which is a type of transfer that can be made using a phone number as the recipient.
 * Extends the Transfer class.
 */
public class BLIKTransfer extends Transfer {
    private final String phoneNumber;

    /**
     * Constructs a BLIKTransfer object with the specified amount and phone number.
     *
     * @param amount      the amount of money to transfer
     * @param phoneNumber the phone number of the recipient
     */
    public BLIKTransfer(double amount, String phoneNumber) {
        super(amount);
        this.phoneNumber = phoneNumber;
        this.recipientAccount = findRecipient();
    }

    /**
     * Returns the log message for the BLIK transfer.
     *
     * @return the log message
     */
    @Override
    public String getLogMessage() {
        String sb = "Transfer BLIK" +
                " od konta " + senderAccount.getId() +
                " na nr. " + phoneNumber;

        return sb;
    }

    /**
     * Returns the type of the transaction, which is BLIK.
     *
     * @return the transaction type
     */
    @Override
    protected TransactionType getType() {
        return TransactionType.BLIK;
    }

    /**
     * Finds the recipient account based on the phone number.
     *
     * @return the recipient account, or null if not found
     */
    @Override
    protected Account findRecipient() {
        Dao<User, String> userDao = DaoFactory.getDao(User.class);
        try {
            // business logic: pierwsze konto jest kontem głównym
            User user = userDao.queryForFirst(userDao.queryBuilder().where().eq("phoneNumber", phoneNumber).prepare());
            Dao<Account, String> accDao = DaoFactory.getDao(Account.class);
            return accDao.queryForFirst(accDao.queryBuilder().where().eq("userId", user.getId()).prepare());
        } catch (SQLException e) {
            return null;
        }
    }
}
