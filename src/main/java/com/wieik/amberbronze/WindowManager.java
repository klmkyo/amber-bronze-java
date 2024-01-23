
package com.wieik.amberbronze;

import com.wieik.amberbronze.controller.dialog.DialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Function;

/**
 * The WindowManager class is responsible for managing the application's windows and dialogs.
 * It provides methods for setting the current stage, loading scenes and dialogs, and displaying dialog boxes.
 */
public class WindowManager {

    private static WindowManager instance;
    private Stage currentStage;

    private WindowManager() { }

    /**
     * Returns the singleton instance of the WindowManager class.
     *
     * @return The WindowManager instance.
     */
    public static WindowManager getInstance() {
        if (instance == null) {
            instance = new WindowManager();
        }
        return instance;
    }

    /**
     * Sets the current stage of the application.
     * The stage is configured with a fixed size of 800x600 and is not resizable.
     *
     * @param stage The current stage to be set.
     */
    public void setCurrentStage(Stage stage) {
        currentStage = stage;
        currentStage.setResizable(false);
        currentStage.setWidth(800);
        currentStage.setHeight(600);
    }

    /**
     * Loads a scene from the specified FXML file and sets it as the content of the current stage.
     *
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an I/O error occurs while loading the FXML file.
     */
    public void loadScene(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = fxmlLoader.load();

        currentStage.close();
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }

    /**
     * Loads a dialog from the specified FXML file and displays it as a separate stage.
     *
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an I/O error occurs while loading the FXML file.
     */
    public void loadDialog(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.initOwner(currentStage);
        dialogStage.setScene(new Scene(root));

        DialogController controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();
    }

    /**
     * Displays a dialog box with the specified title and message.
     * The dialog box contains an "OK" button.
     *
     * @param title   The title of the dialog box.
     * @param message The message to be displayed in the dialog box.
     */
    public void showDialog(String title, String message) {
        showDialog(title, message, (Void) -> null);
    }

    /**
     * Displays a dialog box with the specified title, message, and callback function.
     * The dialog box contains an "OK" button.
     *
     * @param title    The title of the dialog box.
     * @param message  The message to be displayed in the dialog box.
     * @param callback The callback function to be executed when the "OK" button is clicked.
     */
    public void showDialog(String title, String message, Function<Void, Void> callback) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setPrefWidth(300);
        label.setPrefHeight(160);
        label.setStyle("-fx-font-size: 16px; -fx-alignment: center;");
        Button okButton = new Button("OK");

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initOwner(currentStage);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, okButton);
        vBox.setStyle("-fx-alignment: center; -fx-padding: 20px; -fx-spacing: 20px;");
        dialogStage.setScene(new Scene(vBox));
        okButton.setOnAction(e -> {
            callback.apply(null);
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }

    /**
     * Returns the current stage of the application.
     *
     * @return The current stage.
     */
    public Stage getCurrentStage() {
        return currentStage;
    }
}
