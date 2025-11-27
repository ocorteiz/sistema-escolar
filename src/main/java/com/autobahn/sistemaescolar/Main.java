package com.autobahn.sistemaescolar;

import com.autobahn.sistemaescolar.dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gestão Escolar");

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/com/autobahn/sistemaescolar/view/main-view.fxml"));

            BorderPane rootLayout = loader.load();

            Scene scene = new Scene(rootLayout, 1280, 720);

            String css = this.getClass().getResource("/com/autobahn/sistemaescolar/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Database.inicializar();
        launch(args);
    }

}
