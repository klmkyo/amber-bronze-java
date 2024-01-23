package com.wieik.amberbronze.logic;

import com.wieik.amberbronze.entities.Transaction;
import com.wieik.amberbronze.helpers.DateFromISO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Raport {
    List<Transaction> transactions;
    Date date;

    public static class RaportException extends Exception {
        public RaportException(String message) {
            super(message);
        }
    }

    public Raport(List<Transaction> transactions, Date date) {
        this.transactions = transactions;
        this.date = date;
    }

    public List<Transaction> getTransfers() {
        return transactions;
    }

    public Date getDate() {
        return date;
    }

    // Convert Raport to JSON
    public JSONObject toJson() throws RaportException {
        try {
            class JSONEncoder {
                static public JSONObject encode(Transaction[] transactions, Date date) throws RaportException {
                    try {
                        JSONObject json = new JSONObject();
                        JSONArray jsonTransactions = new JSONArray();

                        for (Transaction transaction : transactions) {
                            jsonTransactions.put(transaction.toJson()); // Assuming Transaction class has a toJson method
                        }

                        json.put("transactions", jsonTransactions);
                        String dateISO = date.toInstant().toString();
                        json.put("date", dateISO);
                        return json;
                    } catch (Exception e) {
                        throw new RaportException("Failed to serialize transaction to JSON");
                    }
                }
            }

            return JSONEncoder.encode(transactions.toArray(new Transaction[0]), date);
        } catch (Exception e) {
            throw new RaportException("Failed to serialize raport to JSON");
        }
    }

    // Convert JSON to Raport
    public static Raport fromJson(JSONObject jsonObject) throws RaportException {
        try {

            JSONArray jsonTransactions = jsonObject.getJSONArray("transactions");
            List<Transaction> transactions = new ArrayList<>();

            for (int i = 0; i < jsonTransactions.length(); i++) {
                transactions.add(Transaction.fromJson(jsonTransactions.getJSONObject(i))); // Assuming Transaction class has a fromJson method
            }

            String dateString = jsonObject.getString("date");
            Date date = DateFromISO.fromISO(dateString);

            return new Raport(transactions, date);
        } catch (Exception e) {
            throw new RaportException("Failed to deserialize raport from JSON");
        }
    }
}
