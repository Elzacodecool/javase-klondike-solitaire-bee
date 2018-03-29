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
        // List<List<Pile>> oneMovePiles = new ArrayList<>();
        // List<Pile> stockList = new ArrayList<>();
        // List<Pile> discardList = new ArrayList<>();
        // try {
        //     stockList.add((Pile) this.stockPile.clone());
        //     discardList.add((Pile) this.discardPile.clone());
        // } catch (CloneNotSupportedException e) {
        //     System.err.print("You can't clone this object.");
        // }
        
        
        // oneMovePiles.add(stockList);
        // oneMovePiles.add(discardList);
        // oneMovePiles.add(this.foundationPiles);
        // oneMovePiles.add(this.tableauPiles);
        // this.moveHistory.add(oneMovePiles);
        // System.out.print(moveHistory.size());
    }

    private List<ObservableList<Card>> addCardsToList(Pile pile, List<ObservableList<Card>> oneMoveCards) {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.addAll(pile.getCards());
        oneMoveCards.add(cards);
        return oneMoveCards;
    }

    protected void loadUndoMove() {
        // System.out.print(moveHistory.size());
        // if(!moveHistory.isEmpty()) {
        //     List<List<Pile>> lastMovePiles = moveHistory.get(moveHistory.size() - 1);
        //     if(lastMovePiles.get(0).get(0)==stockPile) {
        //         System.out.println("noooo");
        //     }
        //     System.out.println(discardPile.numOfCards());
        //     System.out.println(lastMovePiles.get(1).get(0).numOfCards());
        //     try {
        //         this.stockPile = (Pile) lastMovePiles.get(0).get(0).clone();
        //         this.discardPile = (Pile) lastMovePiles.get(1).get(0).clone();
        //     } catch (CloneNotSupportedException e) {
        //         System.err.print("You can't clone this object.");
        //     }
            
        //     this.foundationPiles = lastMovePiles.get(2);
        //     this.tableauPiles = lastMovePiles.get(3);

        //     moveHistory.remove(lastMovePiles);
        //     System.out.println(discardPile.numOfCards());
        //     System.out.println(moveHistory.get(0).get(1).size());
        //     System.out.println(moveHistory.get(1).get(1).size());
        // // }
        Pile pile;
        int j;
        if(!moveHistory.isEmpty()) {
            List<ObservableList<Card>> lastMove = FXCollections.observableArrayList();
            lastMove.addAll(moveHistory.get(moveHistory.size() - 1));
            for(Card c: game.getTableauPiles().get(1).getCards()) {
                System.out.println(c.getLayoutX());
                System.out.println(c.getTranslateX());
                System.out.println(c.getContainingPile().getLayoutX());
                System.out.println(game.getTableauPiles().get(1).getLayoutX());
            }
            
            System.out.println("mmmmm");
            
            int i = 0;
            for(ObservableList<Card> cards: lastMove) {
                
                
                System.out.println(cards.size());
                if(i == 0) {
                    game.getStockPile().setCards(cards);
                } else if (i == 1) {
                    game.getDiscardPile().setCards(cards);
                    
                } else if (i < 6) {
                    pile = game.getFoundationPiles().get(i-2);
                    pile.setCards(cards);
                    System.out.print("test1111");
                    for(Card c: pile.getCards()) {
                        System.out.print("test");
                        c.setLayoutX(pile.getLayoutX());
                    }
                } else {
                    pile = game.getTableauPiles().get(i-6);
                    pile.setCards(cards);
                    System.out.print("test222");
                    j = 0;
                    for(Card c: pile.getCards()) {
                        System.out.print("test");
                        c.setLayoutX(pile.getLayoutX());
                        c.setLayoutY(j * pile.getCardGap() + pile.getLayoutY());
                        j++;
                    }
                }
                i++;
            }
            for(Card c: game.getTableauPiles().get(1).getCards()) {
                System.out.println(c.getLayoutX());
                System.out.println(c.getTranslateX());
            }
            // for(Pile pile: game.getTableauPiles()){
            //     for(Card card : pile.getCards()) {
            //         card.setImage(card.isFaceDown() ? card.backFace : card.frontFace);
            //     }
            // }
            moveHistory.remove(lastMove);
        }
    }

}
