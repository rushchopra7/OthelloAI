package de.lmu.bio.ifi;

import szte.mi.Move;
import szte.mi.Player;

import java.util.ArrayList;
import java.util.Random;

public class MyAI implements Player {
    private Random rnd;
    private GameBoard gameBoard;
    private long t;
    public int order;
    private int currentPlayer;

    // Improved weights for strategy
    private final int CORNER_WEIGHT = 25;
    private final int EDGE_WEIGHT = 5;
    private final int MOBILITY_WEIGHT = 3;
    private final int NEAR_CORNER_PENALTY = -8;

    public MyAI() {
    }

    public void setGameBoard(GameBoard gameBoard) {

        this.gameBoard = gameBoard;
    }


    @Override
    public void init(int order, long t, Random rnd) {
        this.rnd = rnd;
        this.t = t;
        this.order = order;
        this.currentPlayer = (order == 0) ? GameBoard.BLACK : GameBoard.WHITE;
        this.gameBoard = new GameBoard();
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        if (prevMove != null) {
            gameBoard.makeMove(prevMove, (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK);
        }
        ArrayList<EvaluatingMoves> listOfMoves = new ArrayList<>();
        int depth = determineDepth();

        for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
            gameBoard.copy_old_state();
            gameBoard.makeMove(move, currentPlayer);
            int score = minimax(gameBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            listOfMoves.add(new EvaluatingMoves(move, score));
            gameBoard.copy_old_state_back();
        }

        return getBestMove(listOfMoves);
    }

    private int determineDepth() {
        int legalMoves = gameBoard.getLegalMoves(currentPlayer).size();
        return (legalMoves < 5) ? 6 : (legalMoves < 10) ? 5 : 4;
    }

    private Move getBestMove(ArrayList<EvaluatingMoves> moves) {
        /*if(moves.size() == 0) {
            return null;
        }
        int max = Integer.MIN_VALUE;
        Move bestMove = null;
        for (EvaluatingMoves move : moves) {
            if (move.score > max) {
                max = move.score;
                bestMove = move.move;
            }
        }
        return bestMove;*/
        return moves.stream().max((m1, m2) -> Integer.compare(m1.getScore(), m2.getScore())).get().getMove();
    }
 //strategy help from https://github.com/codelucas/OthelloAI/blob/master/src/OthelloAI27404511.java
    private int minimax(GameBoard gameBoard, int depth, int alpha, int beta, boolean  isMaxiPlayer) {
        if (depth == 0 || gameBoard.isGameOver(currentPlayer)) {
            return getEvaluation(gameBoard);
        }

        int evaulator;
        if (isMaxiPlayer) {
             evaulator = Integer.MIN_VALUE;
            for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, currentPlayer);
                evaulator = Math.max( evaulator, minimax(gameBoard, depth - 1, alpha, beta, false));
                gameBoard.copy_old_state_back();
                alpha = Math.max(alpha, evaulator);
                if (beta <= alpha) break;
            }
            return  evaulator;
        } else {
             evaulator = Integer.MAX_VALUE;
            int opponent = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
            for (Move move : gameBoard.getLegalMoves(opponent)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, opponent);
                 evaulator = Math.min( evaulator, minimax(gameBoard, depth - 1, alpha, beta, true));
                gameBoard.copy_old_state_back();
                beta = Math.min(beta,  evaulator);
                if (beta <= alpha) break;
            }
            return  evaulator;
        }
    }
    private int calculateEdgeScore(GameBoard gameBoard){
        int blackEdgeScore = gameBoard.getEdgeScore(GameBoard.BLACK);
        int whiteEdgeScore = gameBoard.getEdgeScore(GameBoard.WHITE);
        return blackEdgeScore - whiteEdgeScore * EDGE_WEIGHT;
    }
    private int calculateCornerScore(GameBoard gameBoard){
        int blackCornerScore = gameBoard.getCornerScore(GameBoard.BLACK);
        int whiteCornerScore = gameBoard.getCornerScore(GameBoard.WHITE);
        return blackCornerScore - whiteCornerScore * CORNER_WEIGHT;
    }
    private int calculateMobilityScore(GameBoard gameBoard){
        int blackMobilityScore  = gameBoard.getLegalMoves(GameBoard.BLACK).size();
        int whiteMobilityScore = gameBoard.getLegalMoves(GameBoard.WHITE).size();
        return blackMobilityScore  - whiteMobilityScore * MOBILITY_WEIGHT;
    }

    public int getEvaluation(GameBoard gameBoard) {
        int blackScore = gameBoard.getBlackScore();
        int whiteScore = gameBoard.getWhiteScore();
        int score = (blackScore - whiteScore)
                + calculateCornerScore(gameBoard)
                + calculateEdgeScore(gameBoard)
                + calculateMobilityScore(gameBoard);

        // Penalty for positions near corners if they are not corner-controlled
        score -= penaltyNearCorners(GameBoard.BLACK) - penaltyNearCorners(GameBoard.WHITE);
        return (order == GameBoard.BLACK) ? score : -score;
    }

    private int penaltyNearCorners(int color) {
        int penalty = 0;
        int[][] nearCorners = {{0, 1}, {1, 0}, {1, 1}, {0, 6}, {1, 6}, {1, 7}, {6, 0}, {6, 1}, {7, 1}, {6, 6}, {6, 7}, {7, 6}};
        for (int[] pos : nearCorners) {
            if (gameBoard.getCell(pos[0], pos[1]) == color) {
                penalty += NEAR_CORNER_PENALTY;
            }
        }
        return penalty;
    }

    public static class EvaluatingMoves {
        public Move move;
        public int score;

        public EvaluatingMoves(Move move, int score) {
            this.move = move;
            this.score = score;
        }

        public Move getMove() {
            return move;
        }

        public int getScore() {
            return score;
        }
    }
}
