package com.wieik.amberbronze.controller.dialog;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.UserContext;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.User;
import com.wieik.amberbronze.helpers.Validator;
import com.wieik.amberbronze.helpers.randomDigitsString;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * This class represents the controller for the dialog used to create a new credit card.
 * It extends the DialogController class.
 */
public class NewCreditCardController extends DialogController {

    /**
     * The text field for entering the expiration date of the credit card.
     */
    @FXML
    public TextField expirationDate;

    /**
     * The text field for entering the card number of the credit card.
     */
    @FXML
    public TextField cardNumber;

    /**
     * The text field for entering the CVC (Card Verification Code) of the credit card.
     */
    @FXML
    public TextField cvc;

    /**
     * The text field for entering the PIN (Personal Identification Number) of the credit card.
     */
    @FXML
    public TextField pin;

    /**
     * The label used to display messages related to the credit card creation process.
     */
    public Label messageLabel;

    /**
     * Initializes the dialog by setting default values for the expiration date, card number, and CVC fields.
     */
    public void initialize() {
        LocalDate sevenYearsFromNow = LocalDate.now().plusYears(7);
        String expirationDateFormatted = String.format("%d-%d", sevenYearsFromNow.getMonthValue(), sevenYearsFromNow.getYear());
        expirationDate.setText(expirationDateFormatted);

        String randomCardNumber = randomDigitsString.generate(4) + " " + randomDigitsString.generate(4) + " " + randomDigitsString.generate(4) + " " + randomDigitsString.generate(4);
        cardNumber.setText(randomCardNumber);

        String randomCvc = String.format("%d%d%d", (int)(Math.random() * 10), (int)(Math.random() * 10), (int)(Math.random() * 10));
        cvc.setText(randomCvc);
    }

    /**
     * Sets the message to be displayed in the message label.
     * 
     * @param text The text of the message.
     */
    public void setMessage(String text) {
        setMessage(text, true);
    }

    /**
     * Sets the message to be displayed in the message label, along with the specified style.
     * 
     * @param text      The text of the message.
     * @param isSuccess Indicates whether the message represents a success or failure.
     */
    public void setMessage(String text, Boolean isSuccess) {
        if(text == null) {
            messageLabel.setVisible(false);
            messageLabel.setManaged(false);
            return;
        }

        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
        messageLabel.setText(text);
        if (isSuccess) {
            messageLabel.setStyle("-fx-text-fill: green");
        } else {
            messageLabel.setStyle("-fx-text-fill: red");
        }
    }

    /**
     * Handles the event when the "Create New Credit Card" button is clicked.
     * Retrieves the necessary information from the input fields and creates a new CreditCard object.
     * The CreditCard object is then saved to the database using the Dao class.
     */
    @FXML
    private void handleCreateNewCredditCard() {
        Dao<CreditCard,String> ccDao = DaoFactory.getDao(CreditCard.class);

        Account account = UserContext.getInstance().getCurrentAccount();

        int pin = Integer.parseInt(this.pin.getText());
        String error = Validator.validateInput(String.valueOf(pin), "pin");
        if (error != null) {
            setMessage(error, false);
            return;
        }

        String number = cardNumber.getText().replaceAll("\\s+", "");
        int cvv = Integer.parseInt(cvc.getText());
        String[] expirationDateParts = expirationDate.getText().split("-");
        int expirationMonth = Integer.parseInt(expirationDateParts[0]);
        int expirationYear = Integer.parseInt(expirationDateParts[1]);

        CreditCard creditCard = new CreditCard(account.getId(), number, cvv, expirationMonth, expirationYear, pin);

        System.out.println(creditCard);

        try {
            ccDao.create(creditCard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDialog();
    }
}
