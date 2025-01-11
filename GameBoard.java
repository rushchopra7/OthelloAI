package de.lmu.bio.ifi;

import szte.mi.Move;

import java.nio.file.Paths;
import java.util.ArrayList;

public class GameBoard {

    public static final int EMPTY = 2;
    public static final int BLACK = 0;
    public static final int WHITE = 1;

    private int[][] board =new int[8][8];

    private int[][] board_copy= new int[8][8];
   // private SimpleAI simpleAI = new SimpleAI();

    public GameBoard() {
        initializeBoard();

    }
    public GameBoard(GameBoard gameBoard){
        this.board= gameBoard.getBoard();
        //simpleAI = new SimpleAI();


    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
    }

    public boolean makeMove(Move move, int player) {
        if (isValidMove(move, player)) {
            flipOpponentDiscs(move, player);
            board[move.y][move.x] = player;
            return true;
        }
        return false;
    }
    public void copy_old_state(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board_copy[i][j]=board[i][j];
            }
        }
    }

    public void copy_old_state_back(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j]=board_copy[i][j];
            }
        }
    }

    public boolean isValidMove(Move move, int player) {

        if (board[move.y][move.x] != EMPTY) {
            return false;
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (canFlipInDirection(move, player, dx, dy)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canFlipInDirection(Move move, int player, int dx, int dy) {
        int x = move.x + dx;
        int y = move.y + dy;
        int opponent = (player == BLACK) ? WHITE : BLACK;

        if (x < 0 || x >= 8 || y < 0 || y >= 8 || board[y][x] != opponent) {
            return false;
        }

        x += dx;
        y += dy;

        while (x >= 0 && x < 8 && y >= 0 && y < 8) {
            if (board[y][x] == EMPTY) {
                return false;
            }
            if (board[y][x] == player) {
                return true;
            }
            x += dx;
            y += dy;
        }

        return false;
    }


    private void flipOpponentDiscs(Move move, int player) { // checking for all 7 direction show
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (canFlipInDirection(move, player, dx, dy)) {
                    flipInDirection(move, dx, dy, player);
                }
            }
        }
    }

    private void flipInDirection(Move move, int dx, int dy, int player) {
        int x = move.x + dx;
        int y = move.y + dy;

        while (x >= 0 && x < 8 && y >= 0 && y < 8 && board[y][x] != player) {
            board[y][x] = player;
            x += dx;
            y += dy;
        }
    }

    public int getBlackScore() {
        int blackCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == BLACK) {
                    blackCount++;
                }
            }
        }
        return blackCount;
    }

    public int getWhiteScore() {
        int whiteCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == WHITE) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }

    public boolean isGameOver(int currentPlayer) {
        int black_count= getBlackScore();
        int white_count= getWhiteScore();

        if(black_count+white_count == 64){
            return true;
        }
        else if(black_count==0 || white_count==0){
            return true;
        }
        else if(true){
            boolean black_whiteCantMove = true;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isValidMove(new Move(j, i), currentPlayer)) {
                        black_whiteCantMove = false;
                    }
                }
            }
            return black_whiteCantMove;
        }
        return false;
    }

    public int getWinner() {
        int blackCount = getBlackScore();
        int whiteCount = getWhiteScore();

        if (blackCount > whiteCount) {
            return BLACK;
        } else if (blackCount < whiteCount) {
            return WHITE;
        } else {
            return EMPTY;
        }
    }


    public int getCell(int x, int y) {
        return board[x][y];
    }

     public ArrayList<Move> getLegalMoves(int player) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        //System.out.println("yeahhhh game board reached");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Move move = new Move(i, j);
                if (isValidMove(move, player)) {
                    legalMoves.add(move);
                }
            }
        }
        return legalMoves;
    }

    /*public ArrayList<Move> getLegalMoves(int player) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        int opponentColor = (player == BLACK) ? WHITE : BLACK;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // Check if the cell is empty
                if (this.board[x][y] != 0) continue;

                boolean legal = false;
                outerLoop:
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue;

                        int i = x + dx;
                        int j = y + dy;
                        int length = 0;

                        // Look in direction (dx, dy) for a valid sequence
                        while (i >= 0 && i < 8 && j >= 0 && j < 8 && this.board[i][j] == opponentColor) {
                            i += dx;
                            j += dy;
                            length++;
                        }

                        // Check if the sequence leads to a piece of the player’s color
                        if (length > 0 && i >= 0 && i < 8 && j >= 0 && j < 8 && this.board[i][j] == player) {
                            legal = true;
                            legalMoves.add(new Move(x, y));
                            break outerLoop; // No need to check further directions for this cell
                        }
                    }
                }
            }
        }

        // Debug output for checking generated legal moves
        System.out.println("Legal moves for player " + (player == 0 ? "BLACK" : "WHITE") + ": ");
        for (Move move : legalMoves) {
            System.out.println("(" + move.x + ", " + move.y + ")");
        }
        if (legalMoves.isEmpty()) {
            System.out.println("No legal moves available for player " + (player == 0 ? "BLACK" : "WHITE"));
        }

        return legalMoves;
    } */


    public void reset() {
        initializeBoard();
    }

    public int[][] getBoard() {
        return board;
    }

    // In GameBoard class
    public int getCornerScore(int player) {
        int score = 0;
        int[][] corners = {{0, 0}, {0, 7}, {7, 0}, {7, 7}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == player) {
                score += 20; //  high score for corners
            }
        }
        return score;
    }

    public int getEdgeScore(int player) {
        int score = 0;
        for (int i = 1; i < 7; i++) {
            if (board[0][i] == player || board[7][i] == player || board[i][0] == player || board[i][7] == player) {
                score += 10; //  score for edges
            }
        }
        return score;
    }



}
