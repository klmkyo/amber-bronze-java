package com.wieik.amberbronze.controller;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.UserContext;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.Account;
import com.wieik.amberbronze.entities.CreditCard;
import com.wieik.amberbronze.entities.Transaction;
import com.wieik.amberbronze.entities.User;
import com.wieik.amberbronze.logic.Raport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.wieik.amberbronze.WindowManager;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The DashboardController class is responsible for controlling the dashboard view of the application.
 * It handles user interactions and updates the UI accordingly.
 */
public class DashboardController {
    
    @FXML
    public VBox accountList;
    @FXML
    public VBox creditCardList;

    public ToggleGroup accountsToggleGroup;
    public ToggleGroup creditCardToggleGroup;

    @FXML
    public Label creditCardNumberLabel;
     @FXML
    public Label creditCardExpirationDateLabel;
     @FXML
    public Label creditCardCvvLabel;
     @FXML
    public Label creditCardPinLabel;
     @FXML
    public VBox transferList;
    @FXML
    public VBox creditCardNoneBox;
    @FXML
    public VBox creditCardBox;
    @FXML
    public Button setDefaultAccountButton;


    @FXML
    private Label greetingLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label accountNameLabel;


    private User user;
    /**
     * Initializes the dashboard view.
     * Sets the title, width, and height of the stage.
     * 
     */
    public void initialize() {
        Stage stage = WindowManager.getInstance().getCurrentStage();
        stage.setTitle("Dashboard");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(true);

        user = UserContext.getInstance().getUser();
        try {
            Dao<Account, String> accDao = DaoFactory.getDao(Account.class);
            Account acc = accDao.queryForFirst(accDao.queryBuilder().where().eq("userId", user.getId()).prepare());
            UserContext.getInstance().setCurrentAccount(acc);

            Dao<CreditCard, String> creditCardDao = DaoFactory.getDao(CreditCard.class);
            CreditCard creditCard = creditCardDao.queryForFirst(creditCardDao.queryBuilder().where().eq("accountId", acc.getId()).prepare());
            UserContext.getInstance().setCurrentCreditCard(creditCard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        render();
    }
    /**
     * Renders the dashboard view.
     *
     */
    public void render() {
        greetingLabel.setText("Witaj, " + user.getName() + "!");
        phoneNumberLabel.setText(user.getPhoneNumber());

        refetchAccount();

        updateAccountBalance();
        updateAccountList();
        updateSetDefaultAccountButton();
        updateAccountCreditCards();
        updateCreditCardDetails();
        updateTransactionsList();
    }

    void refetchAccount() {
        Dao<Account, UUID> accDao = DaoFactory.getDao(Account.class);
        try {
            Account currAcc = UserContext.getInstance().getCurrentAccount();
            Account acc = accDao.queryForId(currAcc.getId());
            UserContext.getInstance().setCurrentAccount(acc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the account balance.
     *
     */
    private void updateAccountBalance() {
        Account account = UserContext.getInstance().getCurrentAccount();
        balanceLabel.setText(String.valueOf(account.getBalance()));
        accountNameLabel.setText(account.getName());
    }
    /**
     * Updates the credit card details.
     *
     */
    private void updateCreditCardDetails() {
        CreditCard creditCard = UserContext.getInstance().getCurrentCreditCard();

        if(creditCard == null) {
            creditCardNoneBox.setManaged(true);
            creditCardNoneBox.setVisible(true);
            creditCardBox.setManaged(false);
            creditCardBox.setVisible(false);

            return;
        }

        creditCardNoneBox.setManaged(false);
        creditCardNoneBox.setVisible(false);
        creditCardBox.setManaged(true);
        creditCardBox.setVisible(true);

        creditCardNumberLabel.setText(String.valueOf(creditCard.getNumber()));
        creditCardExpirationDateLabel.setText(String.valueOf(creditCard.getExpirationDate()));
        creditCardCvvLabel.setText(String.valueOf(creditCard.getCvv()));
        creditCardPinLabel.setText(String.valueOf(creditCard.getPin()));
    }

    /**
     * Updates the account list.
     *
     */
    private void updateAccountList()  {
        accountList.getChildren().clear();
        Dao<Account, String> accDao = DaoFactory.getDao(Account.class);
        accountsToggleGroup = new ToggleGroup();
        List<RadioButton> radioButtons = new ArrayList<>();
        List<Account> accounts;

        try {
            accounts = accDao.query(accDao.queryBuilder().where().eq("userId", user.getId()).prepare());
            for(Account acc : accounts) {
                RadioButton rb = new RadioButton(acc.getName());
                rb.setToggleGroup(accountsToggleGroup);
                radioButtons.add(rb);
                accountList.getChildren().add(rb);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Account selectedAccount = UserContext.getInstance().getCurrentAccount();
        int selectedAccountIndex = accounts.stream().map(Account::getId).toList().indexOf(selectedAccount.getId());
        radioButtons.get(selectedAccountIndex).setSelected(true);

        accountsToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                int selectedIndex = radioButtons.indexOf(newToggle);
                UserContext.getInstance().setCurrentAccount(accounts.get(selectedIndex));
                updateAccountBalance();
                render();
            }
        });
    }

    /**
     * Updates the set default account button.
     *
     */
    private void updateSetDefaultAccountButton() {
        User user = UserContext.getInstance().getUser();
        Account account = UserContext.getInstance().getCurrentAccount();

        boolean isDefault = user.getDefaultAccountId().equals(account.getId());

        if(isDefault) {
            setDefaultAccountButton.setText("Konto domyślne");
            setDefaultAccountButton.setDisable(true);
        } else {
            setDefaultAccountButton.setText("Ustaw jako domyślne");
            setDefaultAccountButton.setDisable(false);
        }

        setDefaultAccountButton.setOnAction((ActionEvent event) -> {
            user.setDefaultAccountId(account.getId());

            try {
                Dao<User, String> userDao = DaoFactory.getDao(User.class);
                userDao.update(user);

                UserContext.getInstance().setUser(user);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            render();
        });
    }


    /**
     * Updates the transactions list.
     *
     */     
    private void updateTransactionsList() {
        // query all transactions for current account below
        Dao<Transaction, String> transactionDao = DaoFactory.getDao(Transaction.class);
        Account account = UserContext.getInstance().getCurrentAccount();
        List<Transaction> transactions;
        try {
            transactions = transactionDao.query(transactionDao.queryBuilder().where().eq("senderAccountId", account.getId()).prepare());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        transferList.getChildren().clear();
        for(Transaction transaction : transactions) {
            Label label = new Label(transaction.toString());
            transferList.getChildren().add(label);
        }

    }

    /**
     * Updates the account credit cards.
     *
     */
    private void updateAccountCreditCards() {
        creditCardList.getChildren().clear();
        Dao<CreditCard, String> creditCardDao = DaoFactory.getDao(CreditCard.class);
        creditCardToggleGroup = new ToggleGroup();
        List<RadioButton> radioButtons = new ArrayList<>();

        List<CreditCard> creditCards;
        try {
            Account account = UserContext.getInstance().getCurrentAccount();
            System.out.println(account.getId());
            creditCards = creditCardDao.query(creditCardDao.queryBuilder().where().eq("accountId", account.getId()).prepare());

            if(creditCards.isEmpty()) {
                creditCardList.getChildren().add(new Label("Brak kart kredytowych!"));
            }

            for(CreditCard creditCard : creditCards) {
                RadioButton rb = new RadioButton(String.valueOf(creditCard.getNumber()));
                rb.setToggleGroup(creditCardToggleGroup);
                radioButtons.add(rb);
                creditCardList.getChildren().add(rb);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        CreditCard selectedCreditCard = UserContext.getInstance().getCurrentCreditCard();
        System.out.println(selectedCreditCard);
        if(selectedCreditCard != null) {
            // wybrana karta może należeć do innego rachunku
            // w takim wypadku należy znullować selectedCreditCard
            int selectedCreditCardIndex = creditCards.stream().map(CreditCard::getId).toList().indexOf(selectedCreditCard.getId());

            if(selectedCreditCardIndex == -1) {
                UserContext.getInstance().setCurrentCreditCard(null);
                return;
            }

            radioButtons.get(selectedCreditCardIndex).setSelected(true);
        }

        creditCardToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                int selectedIndex = radioButtons.indexOf(newToggle);
                UserContext.getInstance().setCurrentCreditCard(creditCards.get(selectedIndex));
                updateCreditCardDetails();
                render();
            }
        });
    }
    /**
     * Handles the action when the new account button is clicked.
     * @throws IOException
     */
    @FXML
    private void handleNewAccountButtonAction() throws IOException {
        WindowManager.getInstance().loadDialog("new-account.fxml");

        render();
    }
    /**
     * Handles the action when the new credit card button is clicked.
     * @throws IOException
     */
    @FXML
    private void handleNewCreditCardButtonAction() throws IOException {
        WindowManager.getInstance().loadDialog("new-credit-card.fxml");

        render();
    }
    /**
     * Handles the action when the transfer button is clicked.
     * @throws IOException
     */
    @FXML
    private void handleTransferButtonAction() throws IOException {
        WindowManager.getInstance().loadDialog("new-transaction.fxml");
        render();
    }
    /**
     * Handles the action when the logout button is clicked.
     * @throws IOException
     */
    
    @FXML
    private void handleLogoutButtonAction() throws IOException {
        WindowManager.getInstance().loadScene("login.fxml");
        UserContext.getInstance().clearUser();
        render();
    }

//    show a file picker, and save transactions from the day to a json
    public void handleGenerateReportButtonAction(ActionEvent actionEvent) {
        Dao<Transaction, String> transactionDao = DaoFactory.getDao(Transaction.class);
        Account account = UserContext.getInstance().getCurrentAccount();
        List<Transaction> transactions;
        try {
//            compare with senderAccountId, and date
            transactions = transactionDao.query(transactionDao.queryBuilder().where().eq("senderAccountId", account.getId()).prepare());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> transactionsFromToday = new ArrayList<>();
        for(Transaction transaction : transactions) {
            if(transaction.getDate().getDay() == new java.util.Date().getDay()) {
                transactionsFromToday.add(transaction);
            }
        }

        Raport raport = new Raport(transactionsFromToday, new java.util.Date());
        try {
            String json = raport.toJson().toString();
        } catch (Raport.RaportException e) {
            System.out.println("Failed to generate report");
        }

        String dateISO = raport.getDate().toInstant().toString();

        // write to file in format "raport-<date>.json"
        String fileName = "raport-" + dateISO + ".json";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(raport.toJson().toString());
            writer.close();
            WindowManager.getInstance().showDialog("Raport wygenerowany", "Raport został wygenerowany i zapisany do pliku " + fileName);
        } catch (Raport.RaportException e) {
            System.out.println("Failed to create Raport");
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public void handleReadReportButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz raport");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        Stage stage = WindowManager.getInstance().getCurrentStage();
        java.io.File file = fileChooser.showOpenDialog(stage);
        
        if(file == null) {
            return;
        }
        
        Raport raport = null;
        try {
            String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            raport = Raport.fromJson(new JSONObject(json));
            System.out.println(raport);
        } catch (IOException e) {
            System.out.println("Failed to read file");
        } catch (Raport.RaportException e) {
            System.out.println("Failed to parse JSON");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (raport == null) {
            return;
        }
        
        String raportAsString = "";
        raportAsString += "Transakcje: \n";
        for(Transaction transaction : raport.getTransfers()) {
            raportAsString += transaction.toString() + "\n";
        }
        
        // display raport in a new window
        WindowManager.getInstance().showDialog("Raport z dnia " + new java.util.Date().toString(), raportAsString);
    }
}
