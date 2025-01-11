

package de.lmu.bio.ifi;

import szte.mi.Move;
import szte.mi.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SimpleAI implements Player {
    private Random rnd;
    private GameBoard gameBoard;
    private long t;
    public int order;
    private int depth = 3;

    public SimpleAI() {
    }
    /*public Move getMove() {
        ArrayList<Move> legalMoves = gameBoard.getLegalMoves(order);
        if (legalMoves.isEmpty()) {
            System.out.println("no possible moves : null ");
            return null;
        }
        return legalMoves.get(rnd.nextInt(legalMoves.size()));
    } */

    private Move findBestMove() {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        for (Move move : gameBoard.getLegalMoves(order)) {
            gameBoard.copy_old_state();
            gameBoard.makeMove(move, order);
            int value = minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            gameBoard.copy_old_state_back();
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || gameBoard.isGameOver(order)) {
            return evaluateBoard();
        }

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : gameBoard.getLegalMoves(order)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, order);
                int eval = minimax(depth - 1, alpha, beta, false);
                gameBoard.copy_old_state_back();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            int opponent = (order == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
            for (Move move : gameBoard.getLegalMoves(opponent)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, opponent);
                int eval = minimax(depth - 1, alpha, beta, true);
                gameBoard.copy_old_state_back();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private int evaluateBoard() {
        return gameBoard.getBlackScore() - gameBoard.getWhiteScore(); // Simplistic evaluation function}
}

    @Override
    public void init(int order, long t, Random rnd) {
        this.order = order;
        this.t = t;
        this.rnd = rnd;
        gameBoard = new GameBoard();
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        ArrayList<Move> legalMoves = gameBoard.getLegalMoves(order);
        if (legalMoves.isEmpty()) {
            System.out.println("no possible moves : null ");
            return null;
        }
        return findBestMove();
    }


    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getEvaluation(GameBoard gameBoard) {
        return gameBoard.getBlackScore() - gameBoard.getWhiteScore();
    }
}
