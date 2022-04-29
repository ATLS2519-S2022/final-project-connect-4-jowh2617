/**
 * A Connect-4 player that uses a minimax algorithm to make good valid moves.
 * 
 * @author John Whiteman
 *
 */
public class MinimaxPlayer implements Player
{
    //private static java.util.Random rand = new java.util.Random();

    @Override
    public String name() {
        return "Minnie";
    }
    int bestMove;
    int bestScore;
    int ID;
    int opp_ID;
    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	ID = id;
    	opp_ID = 3 - ID;
    }

    @Override
    public void calcMove(
        Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        //use minimax to find best choice of move
        int cutOffDepth = 1;
        while(!arb.isTimeUp() && cutOffDepth <= board.numEmptyCells()) {
        	bestScore = minimax(board, cutOffDepth, true, arb);
        	cutOffDepth++;
        }
        if(board.isValidMove(bestMove)) {
        	arb.setMove(bestMove);
        }
        
//        for(int i = 0; i < 7; i++) {
//        	int hereDepth = 1;
//        	while(!arb.isTimeUp() && hereDepth <= board.numEmptyCells())
//        	{
//        		if(board.isValidMove(i))
//        		{
//            		int temp = minimax(board, hereDepth, true, arb);
//            		hereDepth++;
//            		if(temp == bestScore) 
//            		{
//            			bestMove = i;
//            			
//            		}
//            	}
//        	}
//        }
//        arb.setMove(bestMove);
    }
    
    
    //simple heuristic to determine score of the game
    public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}
    
    
    //minimax algorithm to evaluate future moves within the game to minimize low scores in worst-case scenario
    public int minimax(Connect4Board board, int depth, boolean isMaximizing, Arbitrator arb) 
    {
    	
    	if(depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) 
    	{
    		return calcScore(board, ID);
    	}
    	if(isMaximizing)
    	{
    		//int bestMove = -1;
    		int bestScore = -1000;
    		for(int i = 0; i < 7; i++)
    		{
    			if(board.isValidMove(i)) {
            		board.move(i, ID);
            		if(minimax(board, depth-1, false, arb) > bestScore)
            		{
            			bestMove = i;
            		}
            		bestScore = Math.max(bestScore,  minimax(board, depth-1, false, arb));
            		board.unmove(i, ID);
            	}
            }
    		return bestScore;
    	}else
    	{
    		int bestScore = 1000;
    		for(int i = 0; i < 7; i++) {
    			if(board.isValidMove(i)) 
    			{
    				board.move(i, opp_ID);
    				if(minimax(board, depth-1, false, arb) < bestScore)
            		{
            			bestMove = i;
            		}
    				bestScore = Math.min(bestScore, minimax(board, depth - 1, true, arb));
    				board.unmove(i, opp_ID);
    			}
    		}
    		return bestScore;
    	}
    }
}