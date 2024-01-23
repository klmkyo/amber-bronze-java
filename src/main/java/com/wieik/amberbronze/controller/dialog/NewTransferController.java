package com.wieik.amberbronze.controller.dialog;

import com.wieik.amberbronze.WindowManager;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.Transaction;
import com.wieik.amberbronze.helpers.NodeHelper;
import com.wieik.amberbronze.helpers.Validator;
import com.wieik.amberbronze.logic.Transfer;
import com.wieik.amberbronze.logic.transfers.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * This class represents a controller for the new transfer dialog.
 * It extends the DialogController class and handles the logic for selecting and executing different types of transfers.
 */
public class NewTransferController extends DialogController {
    @FXML
    public VBox depositTransferView;

    @FXML
    public VBox withdrawalTransferView;
    /**
     * The choice box for selecting the payment type.
     */
    @FXML
    ChoiceBox<String> paymentTypeChoiceBox;

    /**
     * The view containing the payment selection.
     */
    @FXML
    public VBox selectPaymentView;

    /**
     * The view containing the transfer views.
     */
    @FXML
    public VBox transferViews;

    /**
     * The view for BLIK transfer.
     */
    @FXML
    public VBox blikTransferView;

    /**
     * The view for direct transfer.
     */
    @FXML
    public VBox directTransferView;

    /**
     * The view for credit card transfer.
     */
    public VBox creditCardTransferView;

    /**
     * The label for displaying messages.
     */
    @FXML
    public Label messageLabel;

    /**
     * Populates the fields in the given box and returns a map of field IDs to text fields.
     *
     * @param box The box containing the fields.
     * @return A map of field IDs to text fields.
     */
    public Map<String, TextField> populateFields(Pane box) {
        Map<String, TextField> textFields = new HashMap<>();

        for (Node child : box.getChildren()) {
            if (child instanceof TextField field) {
                textFields.put(field.getId(), field);
            }
        }
        return textFields;
    }

    /**
     * Handles the action when the transaction button is clicked.
     * Mounts the appropriate transfer view based on the selected payment type.
     */
    @FXML
    private void handleTransactionButtonAction() {
        String choice = paymentTypeChoiceBox.getValue();
        NodeHelper.unmount(selectPaymentView);
        NodeHelper.mount(transferViews);

        switch(choice) {
            case "BLIK" -> NodeHelper.mount(blikTransferView);
            case "Karta kredytowa" -> NodeHelper.mount(creditCardTransferView);
            case "Wypłata" -> NodeHelper.mount(withdrawalTransferView);
            case "Wpłata" -> NodeHelper.mount(depositTransferView);
        }
    }

    /**
     * Handles the action when the send button is clicked.
     * Validates the inputs, creates the appropriate transfer object, and executes the transfer.
     */
    @FXML
    public void sendAction() {
        VBox managed = (VBox) transferViews.getChildren().stream()
                .filter(node -> node instanceof VBox vbox && vbox.isManaged())
                .findFirst()
                .orElseThrow();

        Map<String, TextField> textFields = populateFields(managed);
        String error = switch(managed.getId()) {
            case "blikTransferView" -> Validator.validateInputs(textFields, Transaction.TransactionType.BLIK);
            case "creditCardTransferView" -> Validator.validateInputs(textFields, Transaction.TransactionType.CREDIT_CARD);
            case "directTransferView" -> Validator.validateInputs(textFields, Transaction.TransactionType.DIRECT);
            case "depositTransferView" -> Validator.validateInputs(textFields, Transaction.TransactionType.DEPOSIT);
            case "withdrawalTransferView" -> Validator.validateInputs(textFields, Transaction.TransactionType.WITHDRAWAL);
            default -> throw new RuntimeException("Unknown payment method: " + managed.getId());
        };

        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
        messageLabel.setText(error);

        if(error == null) {
            double amount = Double.parseDouble(textFields.get("amount").getText());


            Transfer transfer  = switch(managed.getId()) {
                case "blikTransferView" -> {
                    String number = textFields.get("telephoneNumber").getText();
                    yield new BLIKTransfer(amount, number);
                }
                case "creditCardTransferView" -> {
                    // TODO: policja
                    String cardNumber = textFields.get("cardNumber").getText();
                    String pin = textFields.get("pin").getText();
                    yield new CreditCardTransfer(amount, cardNumber, Integer.parseInt(pin));
                }
                case "directTransferView" -> {
                    String number = textFields.get("accountNumber").getText();
                    yield new DirectTransfer(amount, number);
                }
                case "depositTransferView" -> {
                    String cardNumber = textFields.get("cardNumber").getText();
                    String pin = textFields.get("pin").getText();
                    yield new DepositTransfer(amount, cardNumber, Integer.parseInt(pin));
                }
                case "withdrawalTransferView" -> {
                    String cardNumber = textFields.get("cardNumber").getText();
                    String pin = textFields.get("pin").getText();
                    yield new WithdrawTransfer(amount, cardNumber, Integer.parseInt(pin));
                }
                default -> null;
            };

            if(transfer == null) {
                return;
            }

            executeTransfer(transfer);
        }
    }

    void executeTransfer(Transfer transfer) {
        String error = transfer.execute();

        Function<Void, Void> handleClose = (Void) -> {
            closeDialog();
            return null;
        };

        if(error == null) {
            WindowManager.getInstance().showDialog("Sukces", "Transakcja przebiegła pomyślnie", handleClose);
            return;
        }

        WindowManager.getInstance().showDialog("Błąd", error);
    }
}
