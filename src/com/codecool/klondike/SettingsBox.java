package com.codecool.klondike;

import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.ArrayList;

public class SettingsBox {

    public static void display(String title, String message, Game game) {
        ArrayList<String> settings = new ArrayList<String>();

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(500);

        Button darkTheme = new Button("DARK THEME");
        darkTheme.setOnAction(e -> {
            System.out.println("Pressed dark theme button");
            settings.add("table/red.png\n");
            settings.add("card_images/darkTheme/");
            Common.saveThemeSettings(settings);
            game.changeCardsSkin();
            game.setTableBackground(new Image(Common.loadThemeSettings().get(0)));
            window.close();
        });

        Button lightTheme = new Button("LIGHT THEME");
        lightTheme.setOnAction(e -> {
            System.out.println("Pressed light theme button");
            settings.add("table/green.png\n");
            settings.add("card_images/lightTheme/");
            Common.saveThemeSettings(settings);
            game.changeCardsSkin();
            game.setTableBackground(new Image(Common.loadThemeSettings().get(0)));
            window.close();
        });

        Button singleCardMode = new Button("Single card");
        singleCardMode.setOnAction(e -> {
            System.out.println("Set single card game mode");
            Common.saveGameMode(1);
            window.close();
        });

        Button doubleCardMode = new Button("Two cards");
        doubleCardMode.setOnAction(e -> {
            System.out.println("Set double cards game mode");
            Common.saveGameMode(2);
            window.close();
        });

        Button threeCardMode = new Button("Three cards");
        threeCardMode.setOnAction(e -> {
            System.out.println("Set three cards game mode");
            Common.saveGameMode(3);
            window.close();
        });

        Label label = new Label();
        label.setText("Themes settings:");

        Label gameModeSelect = new Label();
        gameModeSelect.setText("Select game mode (requires restart):");

        VBox layout = new VBox(10);
        layout.getChildren().add(label);
        layout.getChildren().add(darkTheme);
        layout.getChildren().add(lightTheme);
        layout.getChildren().add(gameModeSelect);
        layout.getChildren().add(singleCardMode);
        layout.getChildren().add(doubleCardMode);
        layout.getChildren().add(threeCardMode);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}