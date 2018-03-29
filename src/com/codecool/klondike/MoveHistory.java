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
        System.out.print(moveHistory.size());
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
        // ObservableList<Card> cards = FXCollections.observableArrayList();
        // Card card;
        // for(Card c: pile.getCards()) {
        //     card = new Card(c.getSuit(), c.getRank(), c.isFaceDown());
        //     card.setContainingPile(c.getContainingPile());
        //     cards.add(card);
        // }
        // oneMoveCards.add(cards);
        // return oneMoveCards;
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.addAll(pile.getCards());
        oneMoveCards.add(cards);
        return oneMoveCards;
    }

    protected void loadUndoMove() {
        Pile pile;
        int j;
        if(!moveHistory.isEmpty()) {
            List<ObservableList<Card>> lastMove = FXCollections.observableArrayList();
            lastMove.addAll(moveHistory.get(moveHistory.size() - 1));
            
            int i = 0;
            for(ObservableList<Card> cards: lastMove) {
                System.out.println(cards.size());
                if(i == 0) {
                    pile = game.getStockPile();                    
                } else if (i == 1) {
                    pile = game.getDiscardPile();
                } else if (i < 6) {
                    pile = game.getFoundationPiles().get(i-2);
                } else {
                    pile = game.getTableauPiles().get(i-6);
                }

                pile.setCards(cards);
                j = 0;
                for(Card c: pile.getCards()) {
                    c.setContainingPile(pile);
                    if(i == 0) {
                        c.DownFaceDown();
                    }
                    
                    c.setLayoutX(pile.getLayoutX());
                    c.setLayoutY(j * pile.getCardGap() + pile.getLayoutY());
                    c.setImage(c.isFaceDown() ? c.backFace : c.frontFace);
                    game.addMouseEventHandlers(c);                   
                    j++;
                }
                i++;
            }
            moveHistory.remove(lastMove);
        }
    }

}
