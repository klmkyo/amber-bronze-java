package com.wieik.amberbronze.controller.dialog;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * The DialogController class is responsible for managing the dialog window.
 */
public class DialogController {

    private Stage dialogStage;

    /**
     * Sets the dialog stage for the controller.
     *
     * @param dialogStage the stage representing the dialog window
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Closes the dialog window.
     */
    @FXML
    private void handleCloseButtonAction() {
        dialogStage.close();
    }

    /**
     * Closes the dialog window.
     * This method can be overridden by subclasses to provide custom behavior.
     */
    protected void closeDialog() {
        dialogStage.close();
    }
}

