package com.example.demoihm;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stg;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        stg = stage; // Store the reference to the stage
    }

    public void changeScene(String pageName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
        Parent page = loader.load();
        Scene scene = new Scene(page);

        // Set the new scene on the stored stage reference
        stg.setScene(scene);
        stg.show();
    }

    public static void main(String[] args) {
        launch();
    }
}