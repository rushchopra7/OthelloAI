package de.lmu.bio.ifi;

import szte.mi.Move;
import szte.mi.Player;

import java.util.ArrayList;
import java.util.Random;

public class MyAI2 implements Player {

    private Random rnd;
    private GameBoard gameBoard;
    private long t;
    private int currentPlayer;
    private boolean isBlack;
    private int[][] scoreBoard = new int[8][8];
    public int order;

    public MyAI2() {
        // Constructor
    }
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }



    @Override
    public void init(int order, long t, Random rnd) {
        this.rnd = rnd;
        this.t = t;
        this.gameBoard = new GameBoard();
        this.currentPlayer = (order == 0) ? GameBoard.BLACK : GameBoard.WHITE;
        this.isBlack = (currentPlayer == GameBoard.BLACK);
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        ArrayList<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);
        if (legalMoves.isEmpty()) {
            return null;
        }
        return legalMoves.get(new Random().nextInt(legalMoves.size()));
    }
}






