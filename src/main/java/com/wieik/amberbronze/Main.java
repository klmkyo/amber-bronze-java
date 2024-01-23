package com.wieik.amberbronze;

import com.j256.ormlite.dao.Dao;
import com.wieik.amberbronze.dao.DaoFactory;
import com.wieik.amberbronze.entities.User;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class Main extends Application {
    @Override
    public void start(Stage mainStage) throws IOException, SQLException {
        WindowManager wm = WindowManager.getInstance();
        wm.setCurrentStage(mainStage);
        wm.loadScene("login.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}