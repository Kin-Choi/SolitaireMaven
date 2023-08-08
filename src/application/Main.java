package application;

import cards.Mode;
import cards.Suit;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.GameModel;
import model.SuitStackManager.SuitStack;
import model.WorkingStackManager.Workingstack;
import javafx.scene.Scene;


public class Main extends Application {

    // Constants for the width, height, and title of the application window
    private static final int WIDTH = 750;
    private static final int HEIGHT = 500;
    private static final String TITLE = "Solitaire";

    // Views for the deck and waste piles
    private final DeckView deckView = new DeckView();
    private final WasteView wasteView = new WasteView();
    public static ScoreView scoreView = new ScoreView();
    private final Button showRulesButton= new Button("Scoring Rules");
    private final Button ChangeModeButton= new Button("Change Mode");
    private final Button resetButton= new Button("Start New Game");
    private final Text modeText = new Text();

    // Arrays of views for the suit stacks and working stacks
    private final SuitStackView[] suitStacks = new SuitStackView[Suit.values().length];
    private final WorkingStackView[] stacks = new WorkingStackView[Workingstack.values().length];

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle(TITLE);

            // Create the root layout as a GridPane and set its style
            GridPane root = new GridPane();
            root.setHgap(10); // Set the horizontal gap (space) between elements to 10 pixels
            root.setVgap(10); // Set the vertical gap (space) between elements to 10 pixels

            buildLayout(root);

            // Configure the stage and display it
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
            primaryStage.show();

            // Register a listener to the GameModel to check win condition and update view
            GameModel.getInstance().addListener(() -> {
                if (GameModel.getInstance().hasWon()) {
                    buildWinLayout(root);
                }
            });

            GameModel.getInstance().addListener( () -> {
                if (GameModel.getInstance().isRequireReset()) {
                    buildLayout(root);
                }
            });

            // Register a listener to the GameModel to check lose condition and update view
            GameModel.getInstance().addListener(() -> {
                if (GameModel.getInstance().lose()) {
                    buildLoseLayout(root);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildLayout(GridPane root) {
        root.setStyle("-fx-background-color: green;");
        root.getChildren().clear();  // Remove existing views
        showRulesButton.setOnAction(e -> showRules(showRulesButton));
        ChangeModeButton.setOnAction(e->showModes(ChangeModeButton));
        if (GameModel.getInstance().getMode().equals(Mode.VEGAS)) {
            modeText.setText("VEGAS MODE");
        } else {
            modeText.setText("NORMAL MODE");
        }
        modeText.setFont(Font.font("Arial", FontWeight.BOLD,20));
        modeText.setFill(Color.ANTIQUEWHITE);
        resetButton.setOnAction((e->GameModel.getInstance().reset()));

        // Add the deck and waste views to the root layout
        root.add(deckView, 0, 1);
        root.add(wasteView, 1, 1);
        root.add(modeText,7,0);
        root.add(scoreView, 7, 1);
        root.add(showRulesButton, 7,3);
        root.add(ChangeModeButton, 7,4);



        // Add the space with specified width and height
        Region space = new Region();
        space.setPrefSize(70, 90);
        root.add(space, 2, 0);


        // Add the suit stack views to the root layout
        for (SuitStack index : SuitStack.values()) {
            suitStacks[index.ordinal()] = new SuitStackView(index);
            root.add(suitStacks[index.ordinal()], 3 + index.ordinal(), 1);
        }

        // Add the working stack views to the root layout
        for (Workingstack index : Workingstack.values()) {
            stacks[index.ordinal()] = new WorkingStackView(index);
            root.add(stacks[index.ordinal()], index.ordinal(), 2);
        }
        root.add(resetButton, 7, 5);
    }

    private void buildWinLayout(GridPane root) {
        root.getChildren().clear();  // Remove existing views

        // Create and add the FinishView to the root layout
        WinView finishView = new WinView(WIDTH, HEIGHT);
        root.add(finishView, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
    }

    private void buildLoseLayout(GridPane root) {
        root.getChildren().clear();  // Remove existing views

        // Create and add the FinishView to the root layout
        LoseView finishView = new LoseView(WIDTH, HEIGHT);
        root.add(finishView, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
    }

    public void showRules(Button button) {
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: #faf496; -fx-padding: 20px;");
        Button closeButton = new Button("Close");
        if (GameModel.getInstance().getMode().equals(Mode.VEGAS)) {
            popupContent.getChildren().add(new Text("VEGAS MODE"));
            popupContent.getChildren().add(new Text("Your game will begin with -50 scores."));
            popupContent.getChildren().add(new Text("+5 points for each card moved to a suit stack."));
            popupContent.getChildren().add(new Text("+5 points for each card moved from the deck to a row stack."));
            popupContent.getChildren().add(new Text("+5 points for each card turned face-up in a row stack."));
            popupContent.getChildren().add(new Text("No points for each card moved from one row stack to another."));
            popupContent.getChildren().add(new Text("-15 points for each card moved from a suit stack to a row stack."));
            popupContent.getChildren().add(new Text("-50 points for each reset of deck pile."));
            popupContent.getChildren().add(new Text("When scores reach -100, you lose.\n"));
        }
        else {
            popupContent.getChildren().add(new Text("NORMAL MODE"));
            popupContent.getChildren().add(new Text("+10 points for each card moved to a suit stack."));
            popupContent.getChildren().add(new Text("+5 points for each card moved from the deck to a row stack."));
            popupContent.getChildren().add(new Text("+5 points for each card turned face-up in a row stack."));
            popupContent.getChildren().add(new Text("No points for each card moved from one row stack to another."));
            popupContent.getChildren().add(new Text("-15 points for each card moved from a suit stack to a row stack."));
            popupContent.getChildren().add(new Text("-50 points for each reset of deck pile."));
            popupContent.getChildren().add(new Text("When scores reach -50, you lose.\n"));
        }
            popupContent.getChildren().add(closeButton);

        Popup popup = new Popup();
        popup.getContent().add(popupContent);
        closeButton.setOnAction(e->popup.hide());

        // Position the popup relative to the showPopupButton
        popup.show(button, button.getLayoutX()+50, button.getLayoutY()+100);
    }

    public void showModes(Button button) {
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: #faf496; -fx-padding: 20px;");
        HBox box = new HBox();
        Button normal = new Button("Normal");
        Button vegas = new Button("Vegas");
        box.getChildren().add(normal);
        box.getChildren().add(vegas);
        box.setSpacing(10);
        popupContent.getChildren().add(new Text("Change mode and reset the game.\n"));
        popupContent.getChildren().add(box);

        Popup popup = new Popup();
        popup.getContent().add(popupContent);
        normal.setOnAction(e-> {
            GameModel.getInstance().changeMode(Mode.NORMAL);
            popup.hide();
        });
        vegas.setOnAction(e-> {
            GameModel.getInstance().changeMode(Mode.VEGAS);
            popup.hide();
        });

        // Position the popup relative to the showPopupButton
        popup.show(button, button.getLayoutX()+50, button.getLayoutY()+100);
    }

}
