package com.codecool.klondike;

import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.ArrayList;

public class WinBox {

    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(250);

        Button newGame = new Button("Start new game");
        newGame.setOnAction(e -> {
            System.out.println("Started new game");
            window.close();
        });

        Label label = new Label();
        label.setText(message);

        VBox layout = new VBox(20);
        layout.getChildren().add(label);
        layout.getChildren().add(newGame);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}