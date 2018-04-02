package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MoveHistory{
    private Game game;
    protected List<List<ObservableList<Card>>> cardsHistory;
    protected List<Map<Card, Boolean>> faceDownHistory;
    

    public MoveHistory (Game game) {
        this.game = game;
        this.cardsHistory = new ArrayList<>();
        this.faceDownHistory = new ArrayList<>();
    }

    protected void addMoveToHistory() {
        List<ObservableList<Card>> oneMoveCards = makeOneMoveCards();
        Map<Card, Boolean> oneMoveFaceDown = makeOneMoveFaceDown(oneMoveCards);


        if(checkLastMoveIsDifferent(oneMoveCards)) {
            cardsHistory.add(oneMoveCards);
            faceDownHistory.add(oneMoveFaceDown);
        }
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
        }
        undo();        
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
        Card card;

        for(int j = 0; j < pile.numOfCards(); j++) {
            card = pile.getCards().get(j);
            card.setContainingPile(pile);

            if(card.isFaceDown() != oneMoveFaceDown.get(card)) {
                card.flip();
            }
            
            card.setLayoutX(pile.getLayoutX());
            card.setLayoutY(j * pile.getCardGap() + pile.getLayoutY());
            card.setImage(card.isFaceDown() ? card.backFace : card.frontFace);
            card.toFront();
          
        }
    }

}
