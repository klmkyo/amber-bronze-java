package com.wieik.amberbronze.logic.transfers;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.Transaction;
import com.wieik.amberbronze.logic.Transfer;

import java.util.UUID;

/**
 * Represents a deposit transfer in the banking system.
 * Extends the Transfer class.
 */
public class DepositTransfer extends Transfer {

    private final String cardNumber;
    private final int pin;

    /**
     * Returns the type of the transaction as DEPOSIT.
     *
     * @return The transaction type as DEPOSIT.
     */
    @Override
    protected Transaction.TransactionType getType() {
        return Transaction.TransactionType.DEPOSIT;
    }

    /**
     * Constructs a new DepositTransfer object with the specified amount.
     *
     * @param amount The amount to be deposited.
     */
    public DepositTransfer(double amount, String cardNumber, int pin) {
        super(amount);

        this.cardNumber = cardNumber;
        this.pin = pin;
        this.recipientAccount = findRecipient();
    }

    /**
     * Finds the recipient account for the deposit transfer.
     *
     * @return The sender account.
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

    /**
     * Returns the log message for the deposit transfer.
     *
     * @return The log message for the deposit transfer.
     */
    @Override
    public String getLogMessage() {
        String sb = "Wplata w bankomacie " +
                " do konta " + senderAccount.getId() +
                " za " + amount;

        return sb;
    }

}
