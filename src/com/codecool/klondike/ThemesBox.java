package com.codecool.klondike;

import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.ArrayList;

public class ThemesBox {

    public static void display(String title, String message, Game game) {
        ArrayList<String> settings = new ArrayList<String>();

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Button changeRed = new Button("DARK THEME");
        changeRed.setOnAction(e -> {
            settings.add("table/red.png\n");
            settings.add("card_images/darkTheme/");
            Common.saveThemeSettings(settings);
            window.close();
        });

        Button lightTheme = new Button("LIGHT THEME");
        lightTheme.setOnAction(e -> {
            settings.add("table/green.png\n");
            settings.add("card_images/lightTheme/");
            Common.saveThemeSettings(settings);
            window.close();
        });

        Label label = new Label();
        label.setText(message);

        VBox layout = new VBox(10);
        layout.getChildren().add(label);
        layout.getChildren().add(changeRed);
        layout.getChildren().add(lightTheme);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}