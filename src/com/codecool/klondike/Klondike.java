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
    Button newGame;
    Button undo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        startGame(primaryStage);
    }

    public void startGame(Stage primaryStage){
        Card.loadCardImages();
        Game game = new Game();
        game.setTableBackground(new Image(Common.loadThemeSettings().get(0)));

        initializeButtons(game, primaryStage);
        
        primaryStage.setTitle("Klondike Solitaire");
        primaryStage.setScene(new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

    public void initializeButtons(Game game, Stage primaryStage){
        initializeNewGameButton(game, primaryStage);
        initializeThemesButton(game);
        initializeUndoButton(game);
    }

    private void initializeNewGameButton(Game game, Stage stage){
        newGame = new Button("New Game");
        newGame.setLayoutY(10);
        newGame.setLayoutX(10);
        newGame.setOnAction(e -> restartGame(stage));
        game.getChildren().add(newGame);
    }

    public void restartGame(Stage stage){
        stage.close();
        startGame(stage);
    }

    private void initializeThemesButton(Game game){
        changeThemeButton = new Button("Themes");
        changeThemeButton.setLayoutY(40);
        changeThemeButton.setLayoutX(10);
        changeThemeButton.setOnAction(e -> {
            ThemesBox.display("Themes", "", game);
        });
        game.getChildren().add(changeThemeButton);
    }

    private void initializeUndoButton(Game game){
        undo = new Button("Undo");
        undo.setLayoutY(70);
        undo.setLayoutX(10);
        undo.setOnAction(e -> {
            System.out.println("Undo");
            game.getMoves().loadUndoMove();
        });
        game.getChildren().add(undo);
    }
}
