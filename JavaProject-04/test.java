import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class test {
    public static void main(String[] args) {

        //Making cards obj
        caards cards;
        try {
            cards = new caards();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class caards {
    String[] cards = {"1","2","3","4","5","6","7","8","9","10","J","Q","K","A"};
    String[] sixRandomCards = new String[6];
    final byte NUMBER_OF_CARDS = 2; //Max card 14 allowed
    final byte DECK_SIZE = NUMBER_OF_CARDS * 2;
    String deck[] = new String[DECK_SIZE]; //Main deck
    String starCard[] = new String[DECK_SIZE]; //Alternate blind card deck
    Scanner sc = new Scanner(System.in);
    byte memorizeTime = 3; // Memorizing time in second

    //Making Six Random Cards Deck
    caards() throws InterruptedException {
        takingCards(); // Select random card
        doublingDeck(); //Creates main deck
        shuffleDeck(); // Shuffles main deck
        starCards(); // Generates star card deck

        System.out.println("These cards will appear for "+memorizeTime +"s memorize this");
        System.out.println(Arrays.toString(deck));
        printCardNumber();
        sleep(memorizeTime*1000);
        printBlankLines();
        isAllCardsFlipped();
    }
    // Checks if all cards flipped. If not goes to selectTwoCards() function
    public void isAllCardsFlipped(){
        while (true) {
            if(Arrays.equals(starCard, deck)){
                System.out.println("Congrats! You did it.");
                break;
            }
            selectTwoCards();
        }
    }
    public String[] getCards(){
        return this.cards;
    }
    private void starCards(){
        for(int i=0;i<DECK_SIZE;i++){
            this.starCard[i] = "*";
        }
    }
    //Selects cards from cards array
    private void takingCards(){
        for (int i = 0; i < NUMBER_OF_CARDS; i++){
            int randomInt = (int) (Math.random() * cards.length) ;
            this.sixRandomCards[i] = cards[randomInt];
            reduceCards(randomInt);
        }
    }
    // Makes main deck from n cards
    private void doublingDeck(){
        for (int i = 0, k = 0; i < NUMBER_OF_CARDS; i++){
            for(int j = 0; j < 2; j++){
                deck[k] = sixRandomCards[i];
                k++;
            }
        }
    }
    
    private void shuffleDeck(){
        String temp;
        for (int i = deck.length - 1 ; i >= 0 ; i--){
            int randInt = (int) (Math.random() * (i+1));

            temp = deck[randInt];
            deck[randInt] = deck[i];
            deck[i] = temp;
        }
    }
    
    public int returnCardIndex(String card){
        for (int i = 0; i < sixRandomCards.length; i++){
            if(sixRandomCards[i].equals(card))
                return i;
        }
        return -1;
    }
    boolean cardsCompare(int firstCard , int secondCard ){
        if (firstCard == secondCard){
            return true;
        }
        return false;
    }
    // enter 2 card. show 2 cards. if not same return to star
    private void selectTwoCards(){
        System.out.println();
        System.out.println(Arrays.toString(starCard));
        System.out.println("Select two cards: ");
        int firstCard,secondCard;
        
        //input with error handling
        while (true) {
            try {
                firstCard = sc.nextInt();
                secondCard = sc.nextInt();

                if (firstCard <= deck.length && secondCard <= deck.length && firstCard > 0 && secondCard > 0) {
                    if (starCard[firstCard - 1] != "*" && starCard[secondCard - 1] != "*") {
                        System.out.println("Card no " + firstCard + " and " + secondCard + " is already unlocked. Enter valid number:");
                        break;
                    } else if (starCard[firstCard - 1] != "*") {
                        System.out.println("Card no " + firstCard + " is already unlocked. Enter valid number:");
                        break;
                    } else if (starCard[secondCard - 1] != "*"){
                        System.out.println("Card no " + secondCard + " is already unlocked. Enter valid number:");
                        break;
                    }
                    break;
                } else {
                    System.out.println("Enter valid numbers between 1 and " + deck.length);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter integers only.");
                sc.next(); // Clear the invalid input
            }
        }
        // if 2 card same change value of permanently. else temporarily
        if (deck[firstCard-1] == deck[secondCard-1]) {
            this.starCard[firstCard-1] = this.starCard[secondCard-1] = deck[firstCard-1];
            if (Arrays.equals(this.starCard, this.deck))
                System.out.println(Arrays.toString(this.starCard));
        }else if( starCard[firstCard-1] != "*" ^ starCard[secondCard-1] != "*"){
            if(starCard[firstCard-1] != "*"){
                this.starCard[secondCard-1] = deck[secondCard-1];
                System.out.println(Arrays.toString(this.starCard));
                try {
                    sleep(3000);
                    printBlankLines();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.starCard[secondCard-1] = "*";

            }else {
                this.starCard[firstCard-1] = deck[firstCard-1];
                System.out.println(Arrays.toString(this.starCard));
                try {
                    sleep(3000);
                    printBlankLines();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.starCard[firstCard-1] = "*";
            }
        }
        else {
            this.starCard[firstCard-1] = deck[firstCard-1];
            this.starCard[secondCard-1] = deck[secondCard-1];
            System.out.println(Arrays.toString(this.starCard));
            try {
                sleep(3000);
                printBlankLines();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.starCard[firstCard-1] = this.starCard[secondCard-1] = "*";
        }
    }

    private void printCardNumber(){
        int n = this.DECK_SIZE;
        for (int i = 0; i < n; i++){
            System.out.print(i+1 + ", ");
        }
    }
    private void printBlankLines() {
        int lines = 100;
        for (int i = 0; i < lines; i++) {
            System.out.println(); // Print a blank line
        }
    }
    //Deletes selected from deck Function
    private void reduceCards(int n){
        String newCards[] = new String[cards.length-1];
        for (int i = 0, j = 0; i < cards.length; i++){
            if (i != n)
                newCards[j++] = cards[i];
        }
        this.cards = newCards;
    }
}
