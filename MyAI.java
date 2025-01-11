package de.lmu.bio.ifi;

import szte.mi.Move;
import szte.mi.Player;

import java.util.*;

public class MyAI implements Player {
    private Random rnd;
    private GameBoard gameBoard;
    private long t;
    public int order;
    private int currentPlayer;

    // Improved weights for strategic elements
    private final int CORNER_WEIGHT = 25;
    private final int EDGE_WEIGHT = 5;
    private final int MOBILITY_WEIGHT = 3;
    private final int NEAR_CORNER_PENALTY = -8;
    public int score;

    public MyAI() {
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }



      /*  ArrayList<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);
        if (legalMoves.isEmpty()) {
            return null;
        }
        Move selectedMove = legalMoves.get(rnd.nextInt(legalMoves.size()));
        return selectedMove;
    } */



    @Override
    public void init(int order, long t, Random rnd) {
        this.rnd = rnd;
        this.t = t;
        this.order = order;
        this.currentPlayer = (order == 0) ? GameBoard.BLACK : GameBoard.WHITE;
        //this.currentPlayer = (order == 0) ? 1 : 2;

        this.gameBoard = new GameBoard();
    }

    /*@Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        System.out.println("nextMove method reached " );
        printLegalMoves(gameBoard.getLegalMoves(currentPlayer));
        if(prevMove != null){
            gameBoard.makeMove(prevMove, (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK);
        }
        Move bestMove = getBestMove(gameBoard.getLegalMoves(currentPlayer));
        if(bestMove != null){
            gameBoard.makeMove(bestMove, currentPlayer);
        }
        return bestMove;
    }*/ // this one works but still gives illegal moves

    /*@Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        if (prevMove != null) {
            gameBoard.makeMove(prevMove, (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK);
        }

        ArrayList<Move> listOfMoves = new ArrayList<>();
        //ArrayList<Move> listOfMoves = new ArrayList<>();
        int depth = 3;

        for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
            gameBoard.copy_old_state();
            gameBoard.makeMove(move, currentPlayer);
            int score = minimax(gameBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            //int score = getEvaluation(gameBoard);
            listOfMoves.add((move);
            gameBoard.copy_old_state_back();
        }


        return getBestMove(listOfMoves);
    } */

    /*@Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        if (prevMove != null) {
            gameBoard.makeMove(prevMove, (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK);
        }

            ArrayList<EvaluatingMoves> listOfMoves = new ArrayList<>();
            //ArrayList<Move> listOfMoves = new ArrayList<>();
            int depth = determineDepth();

            for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, currentPlayer);
                int score = minimax(gameBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                listOfMoves.add(new EvaluatingMoves(move, score));
                gameBoard.copy_old_state_back();
            }


        return getBestMove(listOfMoves); }

     */
    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        if (prevMove != null) {
            gameBoard.makeMove(prevMove, opponentPlayer(currentPlayer));
        }

        ArrayList<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);

        if (legalMoves.isEmpty()) {
            System.out.println("No legal moves available for AI.");
            return null; // Pass turn or handle accordingly
        }

        Move bestMove = getBestMove(legalMoves);

        if (bestMove != null) {
            gameBoard.makeMove(bestMove, currentPlayer);
            return bestMove;
        }

        return null; // Handle case where no best move is found
    }




    private int determineDepth() {
        int legalMoves = gameBoard.getLegalMoves(currentPlayer).size();
        return (legalMoves < 5) ? 6 : (legalMoves < 10) ? 5 : 4;
    }

    /*private Move getBestMove(ArrayList<EvaluatingMoves> moves) {
        int max = Integer.MIN_VALUE;
        Move bestMove = null;
        //Optional<Move> bestMove = moves.stream().max(Comparator.comparingInt(EvaluatingMoves::getScore)).map(EvaluatingMoves::getMove);
        for (EvaluatingMoves move : moves) {
            if (move.getScore() > max) {
                max = move.getScore();
                bestMove = move.getMove();
            }
        }
        return bestMove;

        //return moves.stream().max((m1, m2) -> Integer.compare(m1.getScore(), m2.getScore())).get().getMove();
    } */

    /*private Move  getBestMove(ArrayList<EvaluatingMoves> moves){
        Optional<EvaluatingMoves> bestMove = moves.stream().max(Comparator.comparingInt(EvaluatingMoves::getScore));
        return bestMove.isPresent() ? bestMove.get().getMove() : null;
    }*/

    /*private Move getBestMove( int currentPlayer){
        ArrayList<Move> legalMoves = gameBoard.getLegalMoves(currentPlayer);
        if(legalMoves.isEmpty()){
            return null;
        }
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        for(Move move : legalMoves){
            gameBoard.copy_old_state();
            gameBoard.makeMove(move, currentPlayer);
            int score = getEvaluation(gameBoard);
            //int score = minimax(gameBoard, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if(score > bestScore){
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    } */
//this one works with evaulatingmoves class
    /*private Move getBestMove(ArrayList<Move> moves) {
        if(moves.isEmpty()){
            return null;
        }
        int max = Integer.MIN_VALUE;
        Move bestMove = null;
        for (Move move : moves) {
            if (move.getScore() > max) {
                max = move.getScore();
                bestMove = move.getMove();
            }
        }
        return bestMove;
    } */

    private Move getBestMove(ArrayList<Move> moves) {
        System.out.println("getBestMove method reached " );
        printLegalMoves(moves);
        if(moves.isEmpty()) {
            System.out.println("moves is empty");
            return null;
        }
        System.out.println("moves is not empty");
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        boolean isMaximizingPlayer = true;

        for (Move move : moves) {
            gameBoard.copy_old_state();
            gameBoard.makeMove(move, currentPlayer);
            int deep = determineDepth();
            System.out.println("deep: " + deep);
            int score = minimax(gameBoard,  deep-1 , Integer.MIN_VALUE, Integer.MAX_VALUE, !isMaximizingPlayer);
            gameBoard.copy_old_state_back();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        System.out.println("bestMove: " + bestMove.x + " " + bestMove.y);
        return bestMove;
    }






    /*private int minimax(GameBoard gameBoard, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || gameBoard.isGameOver(currentPlayer)) {
            System.out.println("getEvaluation method reached ");
            return getEvaluation(gameBoard);
        }

        int eval;
        if (isMaximizingPlayer) {
            eval = Integer.MIN_VALUE;
            for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, currentPlayer);
                eval = Math.max(eval, minimax(gameBoard, depth - 1, alpha, beta, false));
                gameBoard.copy_old_state_back();
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return eval;
        } else {
            eval = Integer.MAX_VALUE;
            int opponent = (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
            for (Move move : gameBoard.getLegalMoves(opponent)) {
                gameBoard.copy_old_state();
                gameBoard.makeMove(move, opponent);
                eval = Math.min(eval, minimax(gameBoard, depth - 1, alpha, beta, true));
                gameBoard.copy_old_state_back();
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return eval;
        }
    } */
    private int opponentPlayer(int currentPlayer) {
        return (currentPlayer == GameBoard.BLACK) ? GameBoard.WHITE : GameBoard.BLACK;
    }
    private int minimax(GameBoard gameBoard, int depth, int alpha, int beta, boolean isMaxiPlayer) {
        if (depth == 0 || gameBoard.isGameOver(currentPlayer)) {
            return getEvaluation(gameBoard);
        }

        if (isMaxiPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : gameBoard.getLegalMoves(currentPlayer)) {
                GameBoard newGameState;
                try {
                    newGameState = (GameBoard) gameBoard.clone(); // Use clone method
                    newGameState.makeMove(move, currentPlayer);
                    int eval = minimax(newGameState, depth-1 , alpha, beta, false);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace(); // Handling exception
                }
            }
            return maxEval;

        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : gameBoard.getLegalMoves(opponentPlayer(currentPlayer))) {
                GameBoard newGameState;
                try {
                    newGameState = (GameBoard) gameBoard.clone(); // Use clone method
                    newGameState.makeMove(move, opponentPlayer(currentPlayer));
                    int eval = minimax(newGameState, depth-1, alpha, beta, true);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace(); // Handling exception
                }
            }
            return minEval;
        }
    }
    public void printLegalMoves(ArrayList<Move> moves) {
        System.out.println("Legal moves:");
        for (Move move : moves) {
            System.out.println("(" + move.x + ", " + move.y + ")");
        }
    }

    /*public int getEvaluation(GameBoard gameBoard) {
        int blackScore = gameBoard.getBlackScore();
        int whiteScore = gameBoard.getWhiteScore();
        int blackCornerScore = gameBoard.getCornerScore(GameBoard.BLACK);
        int whiteCornerScore = gameBoard.getCornerScore(GameBoard.WHITE);
        int blackEdgeScore = gameBoard.getEdgeScore(GameBoard.BLACK);
        int whiteEdgeScore = gameBoard.getEdgeScore(GameBoard.WHITE);
        int blackMobility = gameBoard.getLegalMoves(GameBoard.BLACK).size();
        int whiteMobility = gameBoard.getLegalMoves(GameBoard.WHITE).size();

        int score = (blackScore - whiteScore)
                + (blackCornerScore - whiteCornerScore) * CORNER_WEIGHT
                + (blackEdgeScore - whiteEdgeScore) * EDGE_WEIGHT
                + (blackMobility - whiteMobility) * MOBILITY_WEIGHT;

        // Penalty for positions near corners if not corner-controlled
        score -= penaltyNearCorners(GameBoard.BLACK) - penaltyNearCorners(GameBoard.WHITE);

        return (order == GameBoard.BLACK) ? score : -score;
        return (currentPlayer == GameBoard.BLACK) ? score : -score;
    }*/
  //for ai to function properly
    private HashMap<String, Integer> evaluationMap = new HashMap<>();

    public int getEvaluation(GameBoard gameBoard) {
        String boardKey = gameBoard.toString();
        // Check if the evaluation is already cached
        if (evaluationMap.containsKey(boardKey)) {
            return evaluationMap.get(boardKey);

        }

        //int blackScore = gameBoard.getBlackScore();
        //int whiteScore = gameBoard.getWhiteScore();


        int score = calculateScore(gameBoard)
                + calculateCornerScore(gameBoard)
                + calculateEdgeScore(gameBoard)
                + calculateMobilityScore(gameBoard);

        // Penalty for positions near corners if not corner-controlled
        score -= penaltyNearCorners(GameBoard.BLACK) - penaltyNearCorners(GameBoard.WHITE);

        // Cache the result
        evaluationMap.put(boardKey, score);

        return score;
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
    private int calculateScore(GameBoard gameBoard){
        int blackScore = gameBoard.getBlackScore();
        int whiteScore = gameBoard.getWhiteScore();
        return blackScore - whiteScore;
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

    /*public static class EvaluatingMoves {
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
    } */
}