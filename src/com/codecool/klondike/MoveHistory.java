package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MoveHistory{
    private Game game;
    private List<List<ObservableList<Card>>> cardsHistory;
    private List<Map<Card, Boolean>> faceDownHistory;
    private List<Map<Card, Double>> discardLayoutXHistory;
    

    public MoveHistory (Game game) {
        this.game = game;
        this.cardsHistory = new ArrayList<>();
        this.faceDownHistory = new ArrayList<>();
        this.discardLayoutXHistory = new ArrayList<>();
    }

    protected void addMoveToHistory() {
        List<ObservableList<Card>> oneMoveCards = makeOneMoveCards();
        Map<Card, Boolean> oneMoveFaceDown = makeOneMoveFaceDown(oneMoveCards);
        Map<Card, Double> OneMoveDiscardLayoutX = makeOneMoveLayoutX(game.getDiscardPile());
        if(checkLastMoveIsDifferent(oneMoveCards)) {
            cardsHistory.add(oneMoveCards);
            faceDownHistory.add(oneMoveFaceDown);
            discardLayoutXHistory.add(OneMoveDiscardLayoutX);
        }
    }


    private Map<Card, Double> makeOneMoveLayoutX(Pile pile) {
        Map<Card, Double> oneMoveLayuoutX = new HashMap<>();
        Double gap;
        for(Card card: pile.getCards()) {
            gap = card.getLayoutX() - pile.getLayoutX();
            oneMoveLayuoutX.put(card, gap);
        }

        return oneMoveLayuoutX;
    }

    private List<ObservableList<Card>> makeOneMoveCards() {
        List<ObservableList<Card>> oneMoveCards = new ArrayList<>();
        oneMoveCards = addCardsToList(game.getStockPile(), oneMoveCards);
        oneMoveCards = addCardsToList(game.getDiscardPile(), oneMoveCards);

        for(Pile pile: game.getFoundationPiles()) {
            oneMoveCards = addCardsToList(pile, oneMoveCards);
        }
        for(Pile pile: game.getTableauPiles()) {
            oneMoveCards = addCardsToList(pile, oneMoveCards);
        }
        
        return oneMoveCards;
    }

    private Map<Card, Boolean> makeOneMoveFaceDown(List<ObservableList<Card>> oneMoveCards) {
        Map<Card, Boolean> oneMoveFaceDown = new HashMap<>();
        Boolean faceDown;

        for(ObservableList<Card> cards: oneMoveCards) {
            for(Card card: cards) {
                if(card.isFaceDown() == true){
                    faceDown = true;
                } else {
                    faceDown = false;
                }
                oneMoveFaceDown.put(card, faceDown);
            }
        }

        return oneMoveFaceDown;
    }

    private Boolean checkLastMoveIsDifferent(List<ObservableList<Card>> oneMoveCards) {
        if(cardsHistory.isEmpty()) {
            return true;
        }
        List<ObservableList<Card>> lastMoveCards = cardsHistory.get(cardsHistory.size() - 1);

        for(int i = 0; i < lastMoveCards.size(); i++) {
            if(oneMoveCards.get(i).size() != lastMoveCards.get(i).size()) {
                return true;
            }
        }

        return false;
    }

    private List<ObservableList<Card>> addCardsToList(Pile pile, List<ObservableList<Card>> oneMoveCards) {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.addAll(pile.getCards());
        oneMoveCards.add(cards);

        return oneMoveCards;
    }

    protected void loadUndoMove() {
        while(!checkLastMoveIsDifferent(makeOneMoveCards())) {
            cardsHistory.remove(cardsHistory.size() - 1);
            faceDownHistory.remove(faceDownHistory.size() - 1);
            discardLayoutXHistory.remove(discardLayoutXHistory.size() - 1);
        }
        this.undo();        
    }

    protected void undo() {
        Pile pile;

        if(!cardsHistory.isEmpty()) {
            List<ObservableList<Card>> lastMove = FXCollections.observableArrayList();
            lastMove.addAll(cardsHistory.get(cardsHistory.size() - 1));

            for(int i = 0; i < lastMove.size(); i++) {
                pile = getPileByIndex(i);
                pile.setCards(lastMove.get(i));
                changePropertiesCards(pile);
            }

            cardsHistory.remove(lastMove);
            faceDownHistory.remove(faceDownHistory.size() - 1);
            discardLayoutXHistory.remove(discardLayoutXHistory.size() - 1);
        }
    }

    private Pile getPileByIndex(int i) {
        Pile pile;

        if(i == 0) {
            pile = game.getStockPile();               
        } else if (i == 1) {
            pile = game.getDiscardPile();
        } else if (i < 6) {
            pile = game.getFoundationPiles().get(i-2);
        } else {
            pile = game.getTableauPiles().get(i-6);
        }

        return pile;
    }

    private void changePropertiesCards(Pile pile) {
        Map<Card, Boolean> oneMoveFaceDown = faceDownHistory.get(faceDownHistory.size() - 1);
        Map<Card, Double> oneDiscardLayoutX = discardLayoutXHistory.get(discardLayoutXHistory.size() - 1);
        Card card;
        Double gap;

        for(int i = 0; i < pile.numOfCards(); i++) {
            card = pile.getCards().get(i);
            card.setContainingPile(pile);

            if(card.isFaceDown() != oneMoveFaceDown.get(card)) {
                card.flip();
            }

            if(pile.getPileType() == Pile.PileType.DISCARD) {
                gap = oneDiscardLayoutX.get(card);
            } else {
                gap = (double) 0;
            }
            
            card.setLayoutX(pile.getLayoutX() + gap);
            card.setLayoutY(pile.getLayoutY() + i * pile.getCardGap());
            card.setImage(card.isFaceDown() ? card.backFace : card.frontFace);
            card.toFront();
        }
    }
}
