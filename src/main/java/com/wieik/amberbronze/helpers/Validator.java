package com.wieik.amberbronze.helpers;

import com.wieik.amberbronze.entities.Transaction.TransactionType;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * The Validator class provides methods for validating input fields based on predefined rules.
 */
public class Validator {
    /**
     * Represents a validation rule with a checker function and an error message.
     */
    static final class Validation {
        public Function<String, Boolean> checker;
        public String errorMessage;

        public Validation(Function<String, Boolean> checker, String errorMessage) {
            this.checker = checker;
            this.errorMessage = errorMessage;
        }

        /**
         * Validates the given input based on the checker function.
         *
         * @param input the input to be validated
         * @return the error message if validation fails, null otherwise
         */
        public String validate(String input) {
            if (checker.apply(input)) {
                return null;
            } else {
                return errorMessage;
            }
        }
    }

    /**
     * A map of field names to an array of validation rules.
     */
    static final HashMap<String, Validation[]> validators = new HashMap<>() {{
        put("telephoneNumber", new Validation[] {
                new Validation((String telephoneNumber) -> telephoneNumber.startsWith("+48"), "Numer telefonu musi zaczynać się od +48"),
                new Validation((String telephoneNumber) -> telephoneNumber.replace("+48", "").length() == 9, "Numer telefonu musi mieć 9 cyfr"),
        });
        put("cardNumber", new Validation[] {
                new Validation((String cardNumber) -> cardNumber.length() == 16, "Numer karty musi mieć 16 cyfr"),
        });
        put("expirationMonth", new Validation[] {
                new Validation((String expirationMonth) -> expirationMonth.length() == 2, "Miesiąc musi mieć 2 cyfry"),
                new Validation((String expirationMonth) -> Integer.parseInt(expirationMonth) >= 1 && Integer.parseInt(expirationMonth) <= 12, "Miesiąc musi być z zakresu 1-12")
        });
        put("expirationYear", new Validation[] {
                new Validation((String expirationYear) -> expirationYear.length() == 2, "Rok musi mieć 2 cyfry"),
                new Validation((String expirationYear) -> Integer.parseInt(expirationYear) >= 21 && Integer.parseInt(expirationYear) <= 99, "Rok musi być z zakresu 21-99")
        });
        put("cvv", new Validation[] {
                new Validation((String cvv) -> cvv.length() == 3, "CVV musi mieć 3 cyfry")
        });
        put("pin", new Validation[] {
                new Validation((String pin) -> pin.length() == 4, "PIN musi mieć 4 cyfry")
        });
        put("amount", new Validation[] {
                new Validation((String amount) -> !amount.isEmpty(), "Wpisz kwotę"),
                new Validation((String amount) -> Double.parseDouble(amount) > 0, "Kwota musi być większa od 0")
        });

    }};

    /**
     * A map of transaction types to an array of field names that need to be validated.
     */
    static final HashMap<TransactionType, String[]> fieldsToValidate = new HashMap<>() {{
        put(TransactionType.CREDIT_CARD, new String[] {"cardNumber", "expirationMonth", "expirationYear", "cvv", "pin", "amount"});
        put(TransactionType.BLIK, new String[] {"telephoneNumber", "pin", "amount"});
        put(TransactionType.DEPOSIT, new String[] {"cardNumber", "amount", "pin"});
        put(TransactionType.WITHDRAWAL, new String[] {"cardNumber", "amount", "pin"});

    }};

    /**
     * Validates the input for a specific field.
     *
     * @param input the input to be validated
     * @param field the field name to be validated
     * @return the error message if validation fails, null otherwise
     */
    public static String validateInput(String input, String field) {
        Validation[] validators = Validator.validators.get(field);
        for (Validation validator : validators) {
            String errorMessage = validator.validate(input);
            if (errorMessage != null) {
                return errorMessage;
            }
        }
        return null;
    }

    /**
     * Validates multiple inputs for a specific transaction type.
     *
     * @param inputs the map of field names to text fields
     * @param methodToVerify the transaction type to be validated
     * @return the error message if validation fails, null otherwise
     */
    public static String validateInputs(Map<String, TextField> inputs, TransactionType methodToVerify) {
        String[] fields = fieldsToValidate.get(methodToVerify);

        for (String field : fields) {
            TextField textField = inputs.get(field);
            String errorMessage = null;

            if (textField != null) {
                errorMessage = validateInput(textField.getText(), field);
            }

            if (errorMessage != null) {
                return errorMessage;
            }
        }
        return null;
    }
}
