package application;

import cards.Suit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.GameModel;
import model.GameModelListener;
import model.SuitStackManager.SuitStack;
import model.WorkingStackManager.Workingstack;


public class Main extends Application {

    // Constants for the width, height, and title of the application window
    private static final int WIDTH = 630;
    private static final int HEIGHT = 500;
    private static final String TITLE = "Solitaire";

    // Views for the deck and waste piles
    private DeckView deckView = new DeckView();
    private WasteView wasteView = new WasteView();
    private ScoreView scoreView = new ScoreView();
    private Button showRulesButton= new Button("Scoring Rules");

    // Arrays of views for the suit stacks and working stacks
    private SuitStackView[] suitStacks = new SuitStackView[Suit.values().length];
    private WorkingStackView[] stacks = new WorkingStackView[Workingstack.values().length];

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle(TITLE);

            // Create the root layout as a GridPane and set its style
            GridPane root = new GridPane();
            buildLayout(root);

            // Configure the stage and display it
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
            primaryStage.show();

            // Register a listener to the GameModel to check win condition and update view
            GameModel.getInstance().addListener((GameModelListener) () -> {
                if (GameModel.getInstance().hasWon()) {
                    buildWinLayout(root);
                }
            });

            GameModel.getInstance().addListener((GameModelListener) () -> {
                if (GameModel.getInstance().isRequireReset() == true) {
                    buildLayout(root);
                }
            });

            // Register a listener to the GameModel to check lose condition and update view
            GameModel.getInstance().addListener((GameModelListener) () -> {
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

        // Add the deck and waste views to the root layout
        root.add(deckView, 0, 0);
        root.add(wasteView, 1, 0);
        root.add(scoreView, 7, 0);
        root.add(showRulesButton, 7,1);

        // Add the space with specified width and height
        Region space = new Region();
        space.setPrefSize(70, 90);
        root.add(space, 2, 0);


        // Add the suit stack views to the root layout
        for (SuitStack index : SuitStack.values()) {
            suitStacks[index.ordinal()] = new SuitStackView(index);
            root.add(suitStacks[index.ordinal()], 3 + index.ordinal(), 0);
        }

        // Add the working stack views to the root layout
        for (Workingstack index : Workingstack.values()) {
            stacks[index.ordinal()] = new WorkingStackView(index);
            root.add(stacks[index.ordinal()], index.ordinal(), 1);
        }

        root.add(new ResetView(), 7, 2);
    }

    private void buildWinLayout(GridPane root) {
        root.getChildren().clear();  // Remove existing views

        // Create and add the FinishView to the root layout
        WinView finishView = new WinView(WIDTH, HEIGHT);
        root.add(finishView, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        ScoreView.score.setScore(0);

    }

    private void buildLoseLayout(GridPane root) {
        root.getChildren().clear();  // Remove existing views

        // Create and add the FinishView to the root layout
        LoseView finishView = new LoseView(WIDTH, HEIGHT);
        root.add(finishView, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        ScoreView.score.setScore(0);

    }

    public void showRules(Button button) {
        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: #faf496; -fx-padding: 20px;");
        Button closeButton = new Button("Close");
        popupContent.getChildren().add(new Text("+10 points for each card moved to a suit stack."));
        popupContent.getChildren().add(new Text("+5 points for each card moved from the deck to a row stack."));
        popupContent.getChildren().add(new Text("+5 points for each card turned face-up in a row stack."));
        popupContent.getChildren().add(new Text("No points for each card moved from one row stack to another."));
        popupContent.getChildren().add(new Text("-15 points for each card moved from a suit stack to a row stack."));
        popupContent.getChildren().add(new Text("-50 points for each reset of deck pile."));
        popupContent.getChildren().add(new Text("When scores reach -50, you lose.\n"));
        popupContent.getChildren().add(closeButton);

        Popup popup = new Popup();
        popup.getContent().add(popupContent);
        closeButton.setOnAction(e->popup.hide());

        // Position the popup relative to the showPopupButton
        popup.show(button, button.getLayoutX()+50, button.getLayoutY()+100);
    }

}
