package com.codecool.klondike;

import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ThemesBox {

    public static void display(String title, String message, Game game) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Button changeRed = new Button("Red");
        changeRed.setOnAction(e -> game.setTableBackground(new Image("/table/red.png")));

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.getChildren().add(changeRed);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}