package com.wieik.amberbronze.logic.transfers;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.Transaction.TransactionType;
import com.wieik.amberbronze.entities.User;
import com.wieik.amberbronze.logic.Transfer;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Represents a BLIK transfer, which is a type of transfer that can be made using a phone number as the recipient.
 * Extends the Transfer class.
 */
public class CreditCardTransfer extends Transfer {
    private final String cardNumber;
    private final int pin;

    /**
     * Constructs a CreditCardTransfer object with the specified amount and phone number.
     *
     * @param amount      the amount of money to transfer
     * @param cardNumber
     * @param pin
     */
    public CreditCardTransfer(double amount, String cardNumber, int pin) {
        super(amount);
        this.cardNumber = cardNumber;
        this.pin = pin;
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
                " na nr. " + cardNumber;

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
        try {
            Dao<CreditCard, UUID> creditCardDao = DaoFactory.getDao(CreditCard.class);
            CreditCard creditCard = creditCardDao.queryForFirst(creditCardDao.queryBuilder().where().eq("number", cardNumber).prepare());

            if (creditCard.getPin() != pin) {
                return null;
            }

            Dao<Account, UUID> accountDao = DaoFactory.getDao(Account.class);
            return accountDao.queryForId(creditCard.getAccountId());
        } catch (Exception e) {
            return null;
        }
    }
}
