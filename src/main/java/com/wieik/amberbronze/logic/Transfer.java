package com.wieik.amberbronze.logic;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.UserContext;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.Transaction;

import java.sql.SQLException;

/**
 * The Transfer class represents an abstract transfer operation between two accounts.
 * It extends the LoggedOperation class and provides common functionality for transferring funds.
 */
public abstract class Transfer implements LoggedOperation {
    protected Account senderAccount;
    protected Account recipientAccount;
    protected double amount;

    /**
     * Returns the type of the transaction.
     *
     * @return The type of the transaction.
     */
    protected abstract Transaction.TransactionType getType();

    /**
     * Creates a new Transfer instance with the specified amount.
     *
     * @param amount The amount to transfer.
     */
    public Transfer(double amount) {
        this.senderAccount = UserContext.getInstance().getCurrentAccount();
        this.amount = amount;
    }

    /**
     * Finds the recipient account for the transfer.
     *
     * @return The recipient account.
     */
    protected abstract Account findRecipient();

    /**
     * Executes the transfer operation.
     *
     * @return A message indicating the result of the transfer operation.
     */
    public String execute() {
        if (recipientAccount == null) {
            String message = "Nie znaleziono konta";
            this.logAction(message);
            return message;
        }
        if (senderAccount.getBalance() < amount && getType() != Transaction.TransactionType.DEPOSIT) {
            String message = "Brak wystarczajacych srodkow na koncie";
            this.logAction(message);
            return message;
        }

        if(amount < 0) {
            String message = "Nie mozna wykonac przelewu o ujemnej kwocie";
            this.logAction(message);
            return message;
        }

        Transaction transaction = new Transaction(senderAccount.getId(), recipientAccount.getId(), amount, getType());

        if(getType() != Transaction.TransactionType.DEPOSIT) {
            senderAccount.setBalance(senderAccount.getBalance() - amount);
        }

        if(getType() != Transaction.TransactionType.WITHDRAWAL) {
            recipientAccount.setBalance(recipientAccount.getBalance() + amount);
        }

        Dao<Transaction, String> transactionDao = DaoFactory.getDao(Transaction.class);
        try {
            transactionDao.create(transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.logAction(getLogMessage());
        return null;
    }

}