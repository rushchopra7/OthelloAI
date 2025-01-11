package de.lmu.bio.ifi;

import de.lmu.bio.ifi.GameBoard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import szte.mi.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameRunner extends Application {
    //private SimpleAI ai= new SimpleAI();
    private MyAI ai = new MyAI();
    //private MyAI2 ai = new MyAI2();

    private boolean playWithAI=true;
    private GridPane root;

    private GridPane statsTable;
    private Label lblSystemWins;
    private Label lblUserWins;
    private int userColor;

    private GameBoard gameBoard;
    private int currentPlayer ;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new GridPane();
        gameBoard = new GameBoard();


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int finalI = i;
                final int finalJ = j;

                Button button = new Button("-");
                button.setMinSize(70, 70);

                // Default style for all buttons #009067
                String style = "-fx-background-color: #009067; -fx-border-color: black; -fx-border-radius: 0; -fx-background-radius: 0; -fx-border-width: 1px;";

                // Top-left corner: Top-left radius
                if (i == 0 && j == 0) {
                    style = "-fx-background-color: #009067; -fx-border-color: black; -fx-border-radius: 10 0 0 0; -fx-background-radius: 10 0 0 0; -fx-border-width: 1px;";
                }
                // Top-right corner: Top-right radius
                else if (i == 0 && j == 7) {
                    style = "-fx-background-color: #009067; -fx-border-color: black; -fx-border-radius: 0 10 0 0; -fx-background-radius: 0 10 0 0; -fx-border-width: 1px;";
                }
                // Bottom-left corner: Bottom-left radius
                else if (i == 7 && j == 0) {
                    style = "-fx-background-color: #009067; -fx-border-color: black; -fx-border-radius: 0 0 0 10; -fx-background-radius: 0 0 0 10; -fx-border-width: 1px;";
                }
                // Bottom-right corner: Bottom-right radius
                else if (i == 7 && j == 7) {
                    style = "-fx-background-color: #009067; -fx-border-color: black; -fx-border-radius: 0 0 10 0; -fx-background-radius: 0 0 10 0; -fx-border-width: 1px;";
                }

                button.setStyle(style);
                button.setOnAction(event -> makeMove(finalI, finalJ));
                root.add(button, finalJ, finalI);
            }
        }

        VBox rightBox = new VBox(50);
        rightBox.setPadding(new Insets(50, 50, 50, 50));

        /*Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            System.out.println("Resetting game...");

            // Reset the game board to initial state
            gameBoard.reset();
            System.out.println("Board reset. Initial pieces set.");

            // Reset the current player to start again
            currentPlayer = GameBoard.BLACK;
            System.out.println("Set current player to BLACK.");

            // Reset AI configuration if applicable
            if (userColor == GameBoard.BLACK) {
                currentPlayer =GameBoard.BLACK;
            } else {
                currentPlayer = GameBoard.WHITE;
            }
            ai.setGameBoard(gameBoard);
            System.out.println("AI color set and board updated.");

            // Reinitialize the board display
            displayBoard();
            System.out.println("Board display refreshed.");

            // Reinvoke color selection if needed
            chooseColor();
            System.out.println("Color selection reinvoked if applicable.");
            if (userColor == GameBoard.WHITE) {
                moveAI();
            }
        }); */




        statsTable = new GridPane();
        Label lblSystem = new Label("System");
        Label lblUser = new Label("User");
        lblSystemWins = new Label("0");
        lblUserWins = new Label("0");
        statsTable.add(lblSystem, 0, 0);
        statsTable.add(lblUser, 1, 0);
        statsTable.add(lblSystemWins, 0, 1);
        statsTable.add(lblUserWins, 1, 1);
        statsTable.setHgap(30);
        statsTable.setVgap(10);

        statsTable.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-border-width: 2px;");
        lblSystem.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2px 0; -fx-font-weight: bold; -fx-padding: 5 10 5 10;");
        lblUser.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2px 0; -fx-font-weight: bold; -fx-padding: 5 10 5 10;");
        lblSystemWins.setStyle("-fx-border-color: black; -fx-border-width: 0 2px 0 0; -fx-padding: 5 10 5 10;");
        lblUserWins.setStyle("-fx-border-color: black; -fx-border-width: 0 2px 0 0; -fx-padding: 5 10 5 10;");


        rightBox.setPadding(new Insets(50, 50, 50, 50));

        //rightBox.getChildren().addAll(statsTable, resetButton);
        rightBox.getChildren().addAll(statsTable);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);
        borderPane.setPadding(new Insets(5, 5, 5, 5));
        borderPane.setRight(rightBox);

        Scene scene = new Scene(borderPane, 800, 570);
        primaryStage.setTitle("Othello Game");
        primaryStage.getIcons().add(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcT3pUrDq671YtRKXsaUpVNoi6MIL6S9k1KbRWmxAo64MR44EDyu"));
        primaryStage.setScene(scene);
        primaryStage.show();

        displayBoard();
        chooseColor();
    }

    private void chooseColor() {
        Alert colorChoice = new Alert(Alert.AlertType.CONFIRMATION);
        colorChoice.setTitle("Choose Color");
        colorChoice.setHeaderText("Select Your Color");
        colorChoice.setContentText("Which color would you like to play with?");

        ButtonType blackButton = new ButtonType("Play as BLACK");
        ButtonType whiteButton = new ButtonType("Play as WHITE");
        colorChoice.getButtonTypes().setAll(blackButton, whiteButton);

        java.util.Optional<ButtonType> colorResult = colorChoice.showAndWait();
        if (colorResult.get() == blackButton) {
            userColor = GameBoard.BLACK;
            //currentPlayer = GameBoard.BLACK;
        } else
            userColor = GameBoard.WHITE;
            //currentPlayer = GameBoard.BLACK; // BLACK always starts first

        //currentPlayer = GameBoard.BLACK;
        ai.init(userColor == GameBoard.BLACK ? GameBoard.WHITE : GameBoard.BLACK, 0l, new Random());

        if (ai.order == GameBoard.BLACK) {
            //System.out.println("AI is playing as BLACK");
            moveAI();

        }
    }
        //displayBoard();  // Display the board after choosing color


//        Alert opponentChoice = new Alert(Alert.AlertType.CONFIRMATION);
//        opponentChoice.setTitle("Choose Opponent");
//        opponentChoice.setHeaderText("Choose your opponent");
//        opponentChoice.setContentText("Do you want to play against AI or a Human?");
//
//        ButtonType aiButton = new ButtonType("Play against AI");
//        ButtonType humanButton = new ButtonType("Play against Human");,
//        opponentChoice.getButtonTypes().setAll(aiButton, humanButton);
//
//        java.util.Optional<ButtonType> opponentResult = opponentChoice.showAndWait();
//        if (opponentResult.get() == aiButton) {
//            playWithAI = true;
//        } else {
//            playWithAI = false;
//        }


    /*private void moveAI(){
        if(currentPlayer != userColor) {
            System.out.println("AI's turn. Current player: " + currentPlayer);

            //System.out.println("AI is playing as " + ai.order);
            //Move aiMove = ai.nextMove(new Move(5,5),0l, 0l);
            Move aiMove = ai.nextMove(null,0l,0l);

            //Move aiMove1 = ai.getbestMove();
            //Move aiMove = ai.nextMove(new Move(5,5),0l, 0l);
            if (aiMove != null) {
                System.out.println("AI move: (" + aiMove.x + ", " + aiMove.y + ")");
                gameBoard.makeMove(aiMove, ai.order);
                //currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                displayBoard();
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK; // flipturn
                System.out.println("helloooooo, turns have been flipped hopefully");
                ai.setGameBoard(gameBoard);

                if (gameBoard.isGameOver(currentPlayer)) {
                    announceWinner();
                }
            } else {
                System.out.println("AI move is null.");
            }
        } else {
            System.out.println("It's not AI's turn. Current player: " + currentPlayer);
        } } */
    /*  trial trial 2
    private void moveAI() {

        if (currentPlayer != userColor) {
            System.out.println("AI's turn. Current player: " + currentPlayer);

            Move bestMove = null;
            int bestScore = Integer.MIN_VALUE;

            // Generate legal moves
            ArrayList<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);
            if (legalMoves.isEmpty()) {
                System.out.println("No legal moves available for AI.");
                announceWinner();
                return;
            }

            // Evaluate each legal move
            for (Move move : legalMoves) {
                gameBoard.copy_old_state();
                try {
                    gameBoard.makeMove(move, currentPlayer);
                    int score = ai.getEvaluation(gameBoard);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = move;
                    }
                } catch (Exception e) {
                    System.out.println("Exception in making move: " + e.getMessage());
                } finally {
                    gameBoard.copy_old_state_back();
                }
            }

            if (bestMove != null) {
                System.out.println("AI chooses move: (" + bestMove.x + ", " + bestMove.y + ")");
                gameBoard.makeMove(bestMove, currentPlayer);
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                displayBoard();
                ai.setGameBoard(gameBoard);

                if (gameBoard.isGameOver(currentPlayer)) {
                    announceWinner();
                }
            } else {
                System.out.println("No valid move chosen.");
            }
        } else {
            System.out.println("It's the user's turn.");
        }
    } */

    //trial in an attempt to handle illegal moves
    private void moveAI() {
        if (currentPlayer != userColor) {
            List<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);

            if (legalMoves.isEmpty()) {
                System.out.println("No legal moves available for AI.");
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                return;
            }

            Move bestMove = null;
            int bestScore = Integer.MIN_VALUE;

            for (Move move : legalMoves) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, ai.order);
                int score = ai.getEvaluation(gameBoard);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                gameBoard.copy_old_state_back();
            }

            if (bestMove != null && gameBoard.isValidMove(bestMove, currentPlayer)) {
                System.out.println("AI's selected move: (" + bestMove.x + ", " + bestMove.y + ")");
                gameBoard.makeMove(bestMove, ai.order);
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                displayBoard();
            } else {
                System.out.println("AI attempted an invalid move.");
            }

            if (gameBoard.isGameOver(currentPlayer)) {
                announceWinner();
            }
        }
    }





    //modified moveAI method to make it work with the new AI --this works
    /*private void moveAI() {
        if (currentPlayer != userColor) {
            System.out.println("AI's turn. Current player: " + currentPlayer);

            Move bestMove = null;
            int bestScore = Integer.MIN_VALUE;

            for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
                if(gameBoard.isValidMove(move, currentPlayer)){
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, ai.order);
                int score = ai.getEvaluation(gameBoard);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                gameBoard.copy_old_state_back();
            } }

            if (bestMove != null) {
                System.out.println("AI move: (" + bestMove.x + ", " + bestMove.y + ")");
                gameBoard.makeMove(bestMove, ai.order);
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                displayBoard();
                ai.setGameBoard(gameBoard);

                if (gameBoard.isGameOver(currentPlayer)) {
                    announceWinner();
                }
            } else {
                System.out.println("AI move is null.");
            }
        } else {
            System.out.println("It's the user's turn.");
        }
    } */
   /* it doesnt work..
   private void moveAI() {
        if (currentPlayer != userColor) { // Ensure it's AI's turn
            System.out.println("AI's turn. Current player: " + currentPlayer);

            Move bestMove = null;
            int bestScore = Integer.MIN_VALUE;

            // Retrieve legal moves for the AI's color
            ArrayList<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);

            // Check if legal moves are available
            if (!legalMoves.isEmpty()) {
                System.out.println("Legal moves for AI:");
                for (Move move : legalMoves) {
                    gameBoard.copy_old_state(); // Backup the current state

                    // Make the move for evaluation
                    gameBoard.makeMove(move, currentPlayer);

                    // Evaluate the board for the AI's advantage
                    int score = ai.getEvaluation(gameBoard);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = move;
                    }

                    // Revert to the original state
                    gameBoard.copy_old_state_back();
                }

                // Execute the best move if available
                if (bestMove != null) {
                    System.out.println("AI move: (" + bestMove.x + ", " + bestMove.y + ")");
                    gameBoard.makeMove(bestMove, currentPlayer); // Apply best move

                    // Toggle the player for the next turn
                    currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                    displayBoard();
                    ai.setGameBoard(gameBoard);

                    // Check if the game is over
                    if (gameBoard.isGameOver(currentPlayer)) {
                        announceWinner();
                    }
                } else {
                    System.out.println("AI has no valid moves, skipping turn.");
                    currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                }
            } else {
                System.out.println("No legal moves available for AI.");
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
            }
        } else {
            System.out.println("It's the user's turn.");
        }
    } */




    private void makeMove(int x, int y) {
        Move move = new Move(x, y);
        System.out.println("Move made at: (" + x + ", " + y + ") by " + (currentPlayer == GameBoard.BLACK ? "BLACK" : "WHITE"));
        if(currentPlayer==userColor){
            if (gameBoard.makeMove(move, currentPlayer)) {
                // flipturn();
                currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
                displayBoard();
                ai.setGameBoard(gameBoard);

                if (gameBoard.isGameOver(currentPlayer)) {
                    announceWinner();
//                return;
                }
                else {
                moveAI();
                ai.setGameBoard(gameBoard);
                }
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }




            // If playing with AI and it's AI's turn
//            if (playWithAI && currentPlayer != userColor) {
//                ai.init( currentPlayer,0L, new Random());
//                ai.setGameBoard(gameBoard);
//                int value= currentPlayer;
//                System.out.println("value: "+ value);
//                Move aiMove = ai.nextMove(new Move(6,5),0l, 0l);
//                if (aiMove != null) {
//                    gameBoard.makeMove(aiMove, currentPlayer);
//                    currentPlayer = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
//                    displayBoard();
//
//                    if (gameBoard.isGameOver()) {
//                        announceWinner();
//                    }
//                }
//            }


    }


    private void displayBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Button btn = (Button) root.getChildren().get(i * 8 + j);
                btn.setText("");
                btn.setShape(null);

                switch (gameBoard.getCell(i, j)) {
                    case GameBoard.BLACK:
                        Circle blackCircle = new Circle(25);
                        blackCircle.setFill(javafx.scene.paint.Color.BLACK);
                        btn.setGraphic(blackCircle);
                        break;
                    case GameBoard.WHITE:
                        Circle whiteCircle = new Circle(25);
                        whiteCircle.setFill(javafx.scene.paint.Color.WHITE);
                        btn.setGraphic(whiteCircle);
                        break;
                    default:
                        btn.setGraphic(null);
                        break;
                }

                int blackScore = gameBoard.getBlackScore();
                int whiteScore = gameBoard.getWhiteScore();
                if (userColor == GameBoard.BLACK) {
                    lblUserWins.setText(blackScore + " ");
                    lblSystemWins.setText(whiteScore + " ");
                } else {
                    lblUserWins.setText(whiteScore + "");
                    lblSystemWins.setText(blackScore + "");
                }
            }
        }
    }

    private void announceWinner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");

        int winner = gameBoard.getWinner();
        String message = "";

        if (playWithAI) {
            if (winner == userColor) {
                message = "You win!";
            } else if (winner == GameBoard.BLACK || winner == GameBoard.WHITE) {
                message = "System wins!";
            } else {
                message = "It's a draw!";
            }
        } else {
            if (winner == GameBoard.BLACK) {
                message = "User Black wins!";
            } else if (winner == GameBoard.WHITE) {
                message = "User White wins!";
            } else {
                message = "It's a draw!";
            }
        }

        alert.setContentText(message);
        alert.showAndWait();
    }


}
