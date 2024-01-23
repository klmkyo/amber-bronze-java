package com.wieik.amberbronze.entities;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.wieik.amberbronze.UserContext;
import com.wieik.amberbronze.dao.DaoFactory;

import com.wieik.amberbronze.helpers.DateFromISO;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;


@DatabaseTable(tableName = "transaction")
public class Transaction {
    @DatabaseField(generatedId = true)
    private UUID id;

    @DatabaseField(canBeNull = false)
    private UUID senderAccountId;

    @DatabaseField(canBeNull = false)
    private UUID recipientAccountId;

    @DatabaseField(canBeNull = false)
    private double amount;

    @DatabaseField(canBeNull = false)
    private double title;

    @DatabaseField(canBeNull = false)
    private TransactionType type;

    public Date getDate() {
        return date;
    }

    @DatabaseField(canBeNull = false)
    private Date date;

    /**
     * Enumeration representing the type of transaction.
     */
    public enum TransactionType {
        BLIK("blik"),
        CREDIT_CARD("credit_card"),
        DIRECT("direct"),
        WITHDRAWAL("withdrawal"),
        DEPOSIT("deposit");

        public final String label;

        TransactionType(String label) {
            this.label = label;
        }
    }

    /**
     * Default constructor required by ORMLite.
     */
    public Transaction() {
        // ORMLite needs a no-arg constructor
    }

    /**
     * Constructs a Transaction object with the specified sender account ID, recipient account ID, amount, and type.
     *
     * @param senderAccountId    the ID of the sender account
     * @param recipientAccountId the ID of the recipient account
     * @param amount             the amount of the transaction
     * @param type               the type of the transaction
     */
    public Transaction(UUID senderAccountId, UUID recipientAccountId, double amount, TransactionType type) {
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.amount = amount;
        this.type = type;
        this.date = new Date();
    }

    /**
     * Returns a string representation of the Transaction object.
     *
     * @return a string representation of the Transaction object
     */
    @Override
    public String toString() {
        Dao<Account, UUID> accDao = DaoFactory.getDao(Account.class);
        Dao<User, UUID> userDao = DaoFactory.getDao(User.class);
        Account currentUserAccount = UserContext.getInstance().getCurrentAccount();

        try {
            if (this.senderAccountId.equals(currentUserAccount.getId())) {
                return formatTransferMessage(accDao, userDao, this.recipientAccountId, "Do", this.type.label, this.amount);
            } else if (this.type == TransactionType.DEPOSIT) {
                return formatTransferMessage(accDao, userDao, this.senderAccountId, "Wpłata od", "", this.amount);
            } else if (this.type == TransactionType.WITHDRAWAL) {
                return formatTransferMessage(accDao, userDao, this.recipientAccountId, "Wypłata na konto", "", this.amount);
            } else {
                return formatTransferMessage(accDao, userDao, this.senderAccountId, "Od", this.type.label, this.amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Transfer " + this.type.label + ": Od " + this.senderAccountId + " do " + this.recipientAccountId + " kwota " + this.amount;
        }
    }

    /**
     * Formats a transfer message with the given parameters.
     *
     * @param accDao The DAO for accessing Account objects.
     * @param userDao The DAO for accessing User objects.
     * @param accountId The ID of the account.
     * @param prefix The prefix for the transfer message.
     * @param transactionTypeLabel The label for the transaction type.
     * @param amount The amount of the transaction.
     * @return The formatted transfer message.
     * @throws SQLException If an error occurs while accessing the database.
     */
    private String formatTransferMessage(Dao<Account, UUID> accDao, Dao<User, UUID> userDao, UUID accountId, String prefix, String transactionTypeLabel, double amount) throws SQLException {
        Account account = accDao.queryForId(accountId);
        User user = userDao.queryForId(account.getUserId());
        return transactionTypeLabel.toUpperCase() + " | " + prefix + ": " + user.getName() + " kwota " + amount + "zł";
    }

    // Convert Transaction to JSON
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id != null ? id.toString() : null);
        json.put("senderAccountId", senderAccountId.toString());
        json.put("recipientAccountId", recipientAccountId.toString());
        json.put("amount", amount);
        json.put("type", type.label);
        String dateISO = date.toInstant().toString();
        json.put("date", dateISO);
        return json;
    }

    // Convert JSON to Transaction
    public static Transaction fromJson(JSONObject jsonObject) throws JSONException {
        UUID id = jsonObject.has("id") && !jsonObject.isNull("id") ? UUID.fromString(jsonObject.getString("id")) : null;
        UUID senderAccountId = UUID.fromString(jsonObject.getString("senderAccountId"));
        UUID recipientAccountId = UUID.fromString(jsonObject.getString("recipientAccountId"));
        double amount = jsonObject.getDouble("amount");
        TransactionType type = TransactionType.valueOf(jsonObject.getString("type").toUpperCase());
        String dateISO = jsonObject.getString("date");

        Transaction transaction = new Transaction();
        transaction.id = id;
        transaction.senderAccountId = senderAccountId;
        transaction.recipientAccountId = recipientAccountId;
        transaction.amount = amount;
        transaction.type = type;
        transaction.date = DateFromISO.fromISO(dateISO);
        return transaction;
    }
}
