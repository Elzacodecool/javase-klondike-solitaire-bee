package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;

public class MoveHistory{
    private Game game;
    private List<List<ObservableList<Card>>> moveHistory;
    

    public MoveHistory (Game game) {
        this.game = game;
        this.moveHistory = new ArrayList<>();
    }

    protected void addMoveToHistory() {
        List<ObservableList<Card>> oneMoveCards = new ArrayList<>();
        oneMoveCards = addCardsToList(game.getStockPile(), oneMoveCards);
        oneMoveCards = addCardsToList(game.getDiscardPile(), oneMoveCards);

        for(Pile pile: game.getFoundationPiles()) {
            oneMoveCards = addCardsToList(pile, oneMoveCards);
        }
        for(Pile pile: game.getTableauPiles()) {
            oneMoveCards = addCardsToList(pile, oneMoveCards);
        }
        moveHistory.add(oneMoveCards);
    }

    private List<ObservableList<Card>> addCardsToList(Pile pile, List<ObservableList<Card>> oneMoveCards) {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.addAll(pile.getCards());
        oneMoveCards.add(cards);
        return oneMoveCards;
    }

    protected void loadUndoMove() {
        Pile pile;

        if(!moveHistory.isEmpty()) {
            List<ObservableList<Card>> lastMove = FXCollections.observableArrayList();
            lastMove.addAll(moveHistory.get(moveHistory.size() - 1));

            for(int i = 0; i < lastMove.size(); i++) {
                pile = getPileByIndex(i);
                pile.setCards(lastMove.get(i));
                changePropertiesCards(pile);
            }
            moveHistory.remove(lastMove);
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
        Card card;

        for(int j = 0; j < pile.numOfCards(); j++) {
            card = pile.getCards().get(j);
            card.setContainingPile(pile);

            if(pile.getPileType() == Pile.PileType.STOCK) {
                card.DownFaceDown();
            } else if(pile.getPileType() == Pile.PileType.TABLEAU) {
                if(j < pile.numOfCards() - 1){
                    if (!Card.isNextCorrect(card, pile.getCards().get(j + 1))) {
                        card.DownFaceDown();
                    }
                }
            }
            
            card.setLayoutX(pile.getLayoutX());
            card.setLayoutY(j * pile.getCardGap() + pile.getLayoutY());
            card.setImage(card.isFaceDown() ? card.backFace : card.frontFace);
        }
    }

}
