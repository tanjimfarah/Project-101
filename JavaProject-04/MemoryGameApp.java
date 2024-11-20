import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font; // Import Font for setting font size
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameApp extends Application {

    String[] allCards = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    List<String> selectedCards = new ArrayList<>();
    List<Button> buttons = new ArrayList<>();
    String[] gameDeck = new String[8]; // 4 unique cards duplicated to form 4 pairs (8 total cards)

    Button firstSelected = null;
    Button secondSelected = null;
    boolean isChecking = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Memory Card Matching Game");

        // Select 4 unique random cards from the allCards array
        selectRandomCards();

        // Shuffle the gameDeck containing the pairs of selected cards
        shuffleDeck();

        // Create the grid to hold the cards (2x4 grid for 4 pairs)
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        // Create buttons for each card and add them to the grid
        for (int i = 0; i < gameDeck.length; i++) {
            Button cardButton = new Button("*"); // Initially, all cards are blank
            cardButton.setMinSize(100, 100);
            cardButton.setFont(new Font(20)); // Set initial font size for hidden cards
            int index = i; // Keep track of card index

            cardButton.setOnAction(e -> {
                if (isChecking || cardButton.getText() != "*") {
                    return; // Prevent clicking when already checking or card is already matched
                }
                cardButton.setText(gameDeck[index]); // Show the card value
                cardButton.setFont(new Font(30)); // Increase font size when the card is revealed
                handleCardSelection(cardButton);
            });

            buttons.add(cardButton);
            grid.add(cardButton, i % 4, i / 4); // Add button to grid (4 columns for 2x4 grid)
        }

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handles the card selection logic
    private void handleCardSelection(Button selectedCard) {
        if (firstSelected == null) {
            firstSelected = selectedCard;
        } else {
            secondSelected = selectedCard;
            isChecking = true;

            // Use a PauseTransition to delay the card flip-back logic
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                // Compare the two selected cards
                if (!firstSelected.getText().equals(secondSelected.getText())) {
                    // If cards don't match, flip them back and reset font size
                    firstSelected.setText("*");
                    firstSelected.setFont(new Font(20)); // Reset to smaller font size
                    secondSelected.setText("*");
                    secondSelected.setFont(new Font(20)); // Reset to smaller font size
                }

                // Reset selections
                firstSelected = null;
                secondSelected = null;
                isChecking = false;

                // Check if the game is finished
                if (isGameFinished()) {
                    showGameFinishedPopup(); // Show popup when the game finishes
                }
            });

            pause.play(); // Start the delay before checking and potentially flipping back
        }
    }

    // Check if all the cards are matched
    private boolean isGameFinished() {
        for (Button button : buttons) {
            if (button.getText().equals("*")) {
                return false; // If any card is still hidden, the game is not finished
            }
        }
        return true;
    }

    // Shows a popup when the game finishes
    private void showGameFinishedPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Finished");
        alert.setHeaderText(null);
        alert.setContentText("Congrats! You matched all the cards.");
        alert.show(); // Use show() instead of showAndWait() to avoid IllegalStateException
    }

    // Randomly selects 4 unique cards from the main deck and duplicates them for matching pairs
    private void selectRandomCards() {
        List<String> cardList = new ArrayList<>();
        while (cardList.size() < 4) { // We need only 4 unique cards for 4 pairs
            int randomIndex = (int) (Math.random() * allCards.length);
            String selectedCard = allCards[randomIndex];
            if (!cardList.contains(selectedCard)) {
                cardList.add(selectedCard); // Ensure no duplicates
            }
        }
        selectedCards.addAll(cardList);
        selectedCards.addAll(cardList); // Duplicate for matching
    }

    // Shuffles the deck of 8 cards (4 pairs)
    private void shuffleDeck() {
        Collections.shuffle(selectedCards);
        for (int i = 0; i < selectedCards.size(); i++) {
            gameDeck[i] = selectedCards.get(i);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
