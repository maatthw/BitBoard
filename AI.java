import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {
    private static final Random random = new Random();

    // Right now, it is just selecting a random move.
    public static int[] getBestMove(Board board, boolean isBlack) {
        List<int[]> legalMoves = getAllLegalMoves(board, isBlack);
        if (legalMoves.isEmpty()) {
            return null;
        }
        
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }

    // Generates all the possible moves for the AI to use.
    private static List<int[]> getAllLegalMoves(Board board, boolean isBlack) {
        List<int[]> legalMoves = new ArrayList<>();
        long pieces = isBlack ? board.getBlackPieces() : board.getWhitePieces();
        
        for (int origin = 0; origin < 64; origin++) {
            if (Utility.getBit(pieces, origin)) {
                for (int destination = 0; destination < 64; destination++) {
                    if (board.isLegalMove(origin, destination)) {
                        legalMoves.add(new int[]{origin, destination});
                    }
                }
            }
        }
        
        return legalMoves;
    }
}