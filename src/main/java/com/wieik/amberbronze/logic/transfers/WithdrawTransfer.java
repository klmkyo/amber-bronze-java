package com.wieik.amberbronze.logic.transfers;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.Transaction;
import com.wieik.amberbronze.logic.Transfer;

import java.util.UUID;

/**
 * Represents a withdrawal transfer from an account.
 * Extends the Transfer class.
 */
public class WithdrawTransfer extends Transfer {
    private final String cardNumber;
    private final int pin;
    /**
     * Returns the type of the transaction as Withdrawal.
     *
     * @return The transaction type as Withdrawal.
     */
    @Override
    protected Transaction.TransactionType getType() {
        return Transaction.TransactionType.WITHDRAWAL;
    }

    /**
     * Constructs a new WithdrawTransfer object with the specified amount.
     *
     * @param amount The amount to be withdrawn.
     */
    public WithdrawTransfer(double amount, String cardNumber, int pin) {
        super(amount);
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.recipientAccount = findRecipient();
    }

    /**
     * Finds the recipient account for the withdrawal transfer.
     * In this case, the recipient is the same as the sender account.
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
     * Returns the log message for the withdrawal transfer.
     *
     * @return The log message for the withdrawal transfer.
     */
    @Override
    public String getLogMessage() {
        String sb = "Wyplata z bankomatu " +
                " do konta " + senderAccount.getId() +
                " za " + amount;
        return sb;
    }

}
