package com.codecool.klondike;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;


public class Klondike extends Application {

    private static final double WINDOW_WIDTH = 1400;
    private static final double WINDOW_HEIGHT = 900;
    Button changeThemeButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Card.loadCardImages();
        Game game = new Game();
        game.setTableBackground(new Image("/table/green.png"));

        initializeButtons(game);
        
        primaryStage.setTitle("Klondike Solitaire");
        primaryStage.setScene(new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT));
        if(game.isGameWon()) {
            System.exit(0);
        }
        primaryStage.show();
    }

    public void swapThemes(Game game){
        game.setTableBackground(new Image("/table/red.png"));
    }

    public void initializeButtons(Game game){
        changeThemeButton = new Button("red theme");
        changeThemeButton.setLayoutY(30);
        changeThemeButton.setOnAction(e -> {
            swapThemes(game);
        });
        game.getChildren().add(changeThemeButton);
    }
}
