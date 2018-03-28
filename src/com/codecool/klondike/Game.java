package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import java.util.Iterator;
import java.util.List;

import com.codecool.klondike.Pile.PileType;

import java.util.Collections;

public class Game extends Pane {

    private List<Card> deck = new ArrayList<>();

    private Pile stockPile;
    private Pile discardPile;
    private List<Pile> foundationPiles = FXCollections.observableArrayList();
    private List<Pile> tableauPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<Card> draggedCards = FXCollections.observableArrayList();

    private static double STOCK_GAP = 1;
    private static double FOUNDATION_GAP = 0;
    private static double TABLEAU_GAP = 30;

    private List<List<List<Pile>>> moveHistory = new ArrayList<>();

    private void addMoveToHistory() {
        List<List<Pile>> oneMovePiles = new ArrayList<>();
        List<Pile> stockList = new ArrayList<>();
        List<Pile> discardList = new ArrayList<>();
        stockList.add(this.stockPile);
        discardList.add(this.discardPile);
        oneMovePiles.add(stockList);
        oneMovePiles.add(discardList);
        oneMovePiles.add(this.foundationPiles);
        oneMovePiles.add(this.tableauPiles);
        this.moveHistory.add(oneMovePiles);
    }

    public void loadUndoMove() {
        List<List<Pile>> lastMovePiles = moveHistory.get(moveHistory.size() - 1);
        this.stockPile = lastMovePiles.get(0).get(0);
        this.discardPile = lastMovePiles.get(1).get(0);
        this.foundationPiles = lastMovePiles.get(2);
        this.tableauPiles = lastMovePiles.get(3);
        moveHistory.remove(lastMovePiles);
    }

    private EventHandler<MouseEvent> onMouseClickedHandler = e -> {

        Card card = (Card) e.getSource();
        if (card.getContainingPile().getPileType() == Pile.PileType.STOCK) {
            card.moveToPile(discardPile);
            card.flip();
            card.setMouseTransparent(false);
            System.out.println("Placed " + card + " to the waste.");
            
        } 
    };

    private EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
        refillStockFromDiscard();
    };

    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();        
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
        Card card = (Card) e.getSource();
        Pile activePile = card.getContainingPile();
        if (activePile.getPileType() == Pile.PileType.STOCK)
            return;
        if (!card.isFaceDown() || activePile.getPileType() == Pile.PileType.DISCARD) {
            double offsetX = e.getSceneX() - dragStartX;
            double offsetY = e.getSceneY() - dragStartY;
            
            draggedCards.clear();
            addCards(card);
        
            for(Card draggedCard: draggedCards) {
                draggedCard.getDropShadow().setRadius(20);
                draggedCard.getDropShadow().setOffsetX(10);
                draggedCard.getDropShadow().setOffsetX(10);
                draggedCard.getDropShadow().setOffsetY(10);

                draggedCard.toFront();
                draggedCard.setTranslateX(offsetX);
                draggedCard.setTranslateY(offsetY);
            }
        }   
        };

    private void addCards(Card firstCard) {
        Boolean hasCard = false;
        for(Card card: firstCard.getContainingPile().getCards()) {
            if(card.getSuit().equals(firstCard.getSuit()) && card.getRank().equals(firstCard.getRank())) {
                hasCard = true;
            }
            if(hasCard) {
                draggedCards.add(card);
            }
        }
    }

    private EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
        if (draggedCards.isEmpty())
            return;
        Card card = (Card) e.getSource();
        Pile pile = getValidIntersectingPile(card, tableauPiles);
        if (pile == null) {
            pile = getValidIntersectingPile(card, foundationPiles);
        }
        if (pile == null) {
            pile = card.getContainingPile();
        }
        
        handleValidMove(card, pile);
        draggedCards.clear();     
    };

    public boolean isGameWon() {
        for(Pile pile: foundationPiles) {
            if (pile.numOfCards() != 2) {
                return false;
            }
        }       
        return true;
    }

    public void flipIfNeeded() {
        for(Pile element: tableauPiles) {
            for (Card item : element.getCards()) {
                if(item.isFaceDown() & isCardlastOnPile(item)) {
                    item.flip();
                }
            }
        }
    }

    public boolean isCardlastOnPile(Card card) {
        for(Pile pile: tableauPiles) {
            if (pile.getCards().indexOf(card) == (pile.getCards().size()-1)){
                return true;
            }   
        }
        return false;
    }

    public Game() {
        deck = getRandomDeck();        
        initPiles();
        dealCards();
    }

    private List <Card> getRandomDeck() {
        List <Card> deck = Card.createNewDeck();
        Collections.shuffle(deck);
        return deck;
    } 

    public void addMouseEventHandlers(Card card) {
        card.setOnMousePressed(onMousePressedHandler);
        card.setOnMouseDragged(onMouseDraggedHandler);
        card.setOnMouseReleased(onMouseReleasedHandler);
        card.setOnMouseClicked(onMouseClickedHandler);
    }

    public void refillStockFromDiscard() {
        //TODO
        System.out.println("Stock refilled from discard pile.");
    }

    public boolean isMoveValid(Card card, Pile destPile) {
        if (destPile.getPileType() == PileType.TABLEAU) {
            if (destPile.numOfCards()> 0) {
                if (Card.isOppositeColor(card, destPile.getTopCard()) 
                    && Card.isNextCorrect(destPile.getTopCard(), card)) {
                        return true;
                }
                return false;

            } else if (card.getRank() == Card.Rank.KING) {
               return true;
            }
        } else if (destPile.getPileType() == PileType.FOUNDATION) {
            if(draggedCards.size() > 1) {
                return false;
            } else if (destPile.numOfCards()> 0) {
                if (Card.isSameSuit(card, destPile.getTopCard()) 
                    && Card.isNextCorrect(destPile.getTopCard(), card)) {
                        return true;
                }
                return false;

            } else if (card.getRank() == Card.Rank.ACE) {
               return true;
            }
        }
        return false;
    }
        
    private Pile getValidIntersectingPile(Card card, List<Pile> piles) {
        Pile result = null;
        for (Pile pile : piles) {
            if (!pile.equals(card.getContainingPile()) &&
                    isOverPile(card, pile) &&
                    isMoveValid(card, pile))
                this.addMoveToHistory();
                result = pile;
        }
        return result;
    }

    private boolean isOverPile(Card card, Pile pile) {
        if (pile.isEmpty())
            return card.getBoundsInParent().intersects(pile.getBoundsInParent());
        else
            return card.getBoundsInParent().intersects(pile.getTopCard().getBoundsInParent());
    }

    private void handleValidMove(Card card, Pile destPile) {
        String msg = null;
        if (destPile.isEmpty()) {
            if (destPile.getPileType().equals(Pile.PileType.FOUNDATION))
                msg = String.format("Placed %s to the foundation.", card);
            if (destPile.getPileType().equals(Pile.PileType.TABLEAU))
                msg = String.format("Placed %s to a new pile.", card);
        } else {
            msg = String.format("Placed %s to %s.", card, destPile.getTopCard());
        }
        System.out.println(msg);
        MouseUtil.slideToDest(draggedCards, destPile, this);
        draggedCards.clear();
    }


    private void initPiles() {
        stockPile = new Pile(Pile.PileType.STOCK, "Stock", STOCK_GAP);
        stockPile.setBlurredBackground();
        stockPile.setLayoutX(95);
        stockPile.setLayoutY(20);
        stockPile.setOnMouseClicked(stockReverseCardsHandler);
        getChildren().add(stockPile);

        discardPile = new Pile(Pile.PileType.DISCARD, "Discard", STOCK_GAP);
        discardPile.setBlurredBackground();
        discardPile.setLayoutX(285);
        discardPile.setLayoutY(20);
        getChildren().add(discardPile);

        for (int i = 0; i < 4; i++) {
            Pile foundationPile = new Pile(Pile.PileType.FOUNDATION, "Foundation " + i, FOUNDATION_GAP);
            foundationPile.setBlurredBackground();
            foundationPile.setLayoutX(610 + i * 180);
            foundationPile.setLayoutY(20);
            foundationPiles.add(foundationPile);
            getChildren().add(foundationPile);
        }
        for (int i = 0; i < 7; i++) {
            Pile tableauPile = new Pile(Pile.PileType.TABLEAU, "Tableau " + i, TABLEAU_GAP);
            tableauPile.setBlurredBackground();
            tableauPile.setLayoutX(95 + i * 180);
            tableauPile.setLayoutY(275);
            tableauPiles.add(tableauPile);
            getChildren().add(tableauPile);
        }
    }

    public void dealCards() {
        Iterator<Card> deckIterator = deck.iterator();
        for (int i = 0; i < 7 ; i++ ){
            for (int j = 0; j < i+1; j++ ) {
                Card card = deckIterator.next();
                tableauPiles.get(i).addCard(card);
                addMouseEventHandlers(card);
                getChildren().add(card);
            }
        }
        for (Pile tableauPile : tableauPiles) {
            tableauPile.flipTopCard();
        }
        deckIterator.forEachRemaining(card -> {
            stockPile.addCard(card);
            addMouseEventHandlers(card);
            getChildren().add(card);
        });

    }

    public void setTableBackground(Image tableBackground) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

}
