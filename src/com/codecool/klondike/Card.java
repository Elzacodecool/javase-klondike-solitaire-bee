package com.codecool.klondike;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.*;

import com.codecool.klondike.Pile.PileType;

public class Card extends ImageView {

    private Suit suit;
    private Rank rank;
    private boolean faceDown;

    protected Image backFace;
    protected Image frontFace;
    private Pile containingPile;
    private DropShadow dropShadow;

    static Image cardBackImage;
    private static final Map<String, Image> cardFaceImages = new HashMap<>();
    public static final int WIDTH = 150;
    public static final int HEIGHT = 215;

    public Card(Suit suit, Rank rank, boolean faceDown) {
        this.suit = suit;
        this.rank = rank;
        this.faceDown = faceDown;
        this.dropShadow = new DropShadow(2, Color.gray(0, 0.75));
        backFace = cardBackImage;
        frontFace = cardFaceImages.get(getShortName());
        setImage(faceDown ? backFace : frontFace);
        setEffect(dropShadow);
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isFaceDown() {
        return faceDown;
    }

    public void DownFaceDown() {
        this.faceDown = true;
    }

    public String getShortName() {
        return "S" + suit + "R" + rank;
    }

    public DropShadow getDropShadow() {
        return dropShadow;
    }

    public Pile getContainingPile() {
        return containingPile;
    }

    public void setContainingPile(Pile containingPile) {
        this.containingPile = containingPile;
    }

    public void moveToPile(Pile destPile) {
        this.getContainingPile().getCards().remove(this);
        destPile.addCard(this);
        
    }

    public void flip() {
        faceDown = !faceDown;
        setImage(faceDown ? backFace : frontFace);
    }

    @Override
    public String toString() {
        return "The " + "Rank" + rank + " of " + "Suit" + suit;
    }

    public static boolean isOppositeColor(Card card1, Card card2) {
        if (card1.suit == Suit.HEARTS || card1.suit == Suit.DIAMONDS) {
            if (card2.suit == Suit.SPADES || card2.suit == Suit.CLUBS) {
                return true;
            }
        } else if (card1.suit == Suit.SPADES || card1.suit == Suit.CLUBS) {
            if (card2.suit == Suit.HEARTS || card2.suit == Suit.DIAMONDS) {
                return true;
            }
        }
        return false;   
    }

    public static boolean isNextCorrect(Card cardOnTable, Card cardToCheck) {
        int difference = cardOnTable.getRank().ordinal() - cardToCheck.getRank().ordinal();
         
        if (cardOnTable.containingPile.getPileType() == PileType.FOUNDATION) {
            if(difference == -1) {
                return true;
            }
        } else if(difference == 1) {
            return true;
        }
        return false;
    }

    public static boolean isSameSuit(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit();
    }

    public static List<Card> createNewDeck() {
        List<Card> result = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                result.add(new Card(suit, rank, true));
            }
        }
        return result;
    }

    public static void loadCardImages() {
        String cardsDirectory = Common.loadThemeSettings().get(1);
        cardBackImage = new Image(cardsDirectory + "card_back.png");
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                String cardName = suit.toString() + rank.toString();
                String cardId = "S" + suit + "R" + rank;
                String imageFileName = cardsDirectory + cardName + ".png";
                cardFaceImages.put(cardId, new Image(imageFileName));
            }
        }
    }

    protected static boolean isPlaceForCardInFoundation(Card card, Pile pile) {
            if (pile.numOfCards() > 0) {
                if (Card.isSameSuit(card, pile.getTopCard()) 
                        && Card.isNextCorrect(pile.getTopCard(), card)) {
                            return true;
                } else {
                    return false;
                }
            } else if (card.getRank() == Card.Rank.ACE) {
               return true;
            } 
            return false;
    }
    
    public static enum Suit {
        DIAMONDS, HEARTS, CLUBS, SPADES;

        public String toString(){
            return this.name().toLowerCase();
        }
    }

    public static enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

        public String toString(){
            return String.valueOf(this.ordinal() + 1);
        }

    }

}
