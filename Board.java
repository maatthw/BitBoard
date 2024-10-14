import java.util.Scanner;
 class Board {
    private long blackPieces; // Bitboard for the black pieces
    private long whitePieces; //  Bitboard for the white pieces
    private long kings; // Bitboard for the kings
    private boolean isBlackTurn; // Boolean for whose turn it is
    private static final long VALID_SQUARES = 0x55AA55AA55AA55AAL; // Valid squares for the board
    private boolean isAIGame; // Boolean for AI game or not
    private boolean isAIBlack; // Boolean for AI side/color

    public Board(boolean isAIGame, boolean isAIBlack){
        // Starting positions for black and white, as well as setting zero kings and letting black go first. Also sets whether or not it is an User vs AI game or User vs User
        this.blackPieces = 0x000000000055AA55L;
        this.whitePieces = 0xAA55AA0000000000L;
        this.kings = 0L;
        isBlackTurn = true;
        this.isAIGame = isAIGame;
        this.isAIBlack = isAIBlack;
    }


    // Loop for the game
    public void play() {
        Scanner input = new Scanner(System.in);

        while (!isGameOver()) {
            displayBoard();
            System.out.println((isBlackTurn ? "Black" : "White") + "'s turn.");

            if (isAIGame && isBlackTurn == isAIBlack) {
                int[] aiMove = AI.getBestMove(this, isBlackTurn);
                if (aiMove != null) {
                    System.out.println("AI moves: " + positionToString(aiMove[0]) + " to " + positionToString(aiMove[1]));
                    movePiece(aiMove[0], aiMove[1]);
                    isBlackTurn = !isBlackTurn;
                } else {
                    System.out.println("AI has no legal moves. Game over.");
                    break;
                }
            } else {
                System.out.println("Enter move (Such as 'C3 D4'): ");
                String move = input.nextLine().toUpperCase();

                int origin = parsePosition(move.substring(0, 2));
                int destination = parsePosition(move.substring(3));
                
                if (isLegalMove(origin, destination)) {
                    movePiece(origin, destination);
                    isBlackTurn = !isBlackTurn;
                } else {
                    System.out.println("Illegal Move. Please try a different move.");
                }
            }

            displayBinaryBoard();
            displayHexBoard();
        }
        displayBoard();
        System.out.println("Game over. " + (isBlackTurn ? "White" : "Black") + " wins.");
        input.close();
    }

    // Method for making a move
    private void movePiece(int origin, int destination) {
         long pieces = isBlackTurn ? blackPieces : whitePieces; // Select either black or white bitboard based on whose turn it is

         pieces = Utility.clearBit(pieces, origin);

         pieces = Utility.setBit(pieces, destination);
        
         // Switching turns
         if (isBlackTurn) {
             blackPieces = pieces;
         } else {
             whitePieces = pieces;
         }
     }

     // Method for checking whether a move is legal or not
     public boolean isLegalMove(int origin, int destination) {
         long currentPieces = isBlackTurn ? blackPieces : whitePieces; // Select either black or white bitboard based on whose turn it is

         // First, we need to check that the user is moving a piece that is actually on the origin square
         if (!Utility.getBit(currentPieces, origin)) {
             return false;
         }

         // Next, we need to make sure that the user isn't trying to move to a square outside the bounds of the 8x8 board
         if (destination < 0 || destination >= 64 || Utility.getBit(blackPieces | whitePieces, destination)) {
             return false;
         }


         long opponentPieces = isBlackTurn ? whitePieces : blackPieces;
         boolean isKing = Utility.getBit(kings, origin);
        
         // Operations to get the rows and columns for both the origin and destination positions
         long rowOrigin = Utility.divide(origin, 8);
         long colOrigin = origin % 8;
         long rowDestination = Utility.divide(destination, 8);
         long colDestination = destination % 8;

         // Now, we need to check that the user is making a diagonal move. We can do this by checking the difference between the row and column is the same for both the origin and destination
         if (Math.abs(rowOrigin - rowDestination) != Math.abs(colOrigin - colDestination)) {
             return false;
         }

         // Selects the direction based on whose turn it is.
         int direction = isBlackTurn ? 1 : -1;

         // This is checking in the case of a single jump move, so only going to an adjacent square.
         if (Math.abs(rowOrigin - rowDestination) == 1) {
            // Need to make sure that if the piece isn't a king, it is moving in the correct direction.
             if (!isKing && ((rowDestination - rowOrigin) != direction)) {
                 return false;
             }
             return true;
         }

         // This is checking in the case of a more than 1 jump move, such as when you capture an opponents piece.
         if (Math.abs(rowOrigin - rowDestination) == 2) {
             long middleRow = Utility.divide((rowOrigin + rowDestination), 2);
             long middleCol = Utility.divide((colDestination + colOrigin), 2);
             long middlePos = Utility.add((Utility.multiply(middleRow, 8)), middleCol);

             // If the square between the origin square and destination square doesn't contain an opponent piece, then it's not a valid jump.
             if (!Utility.getBit(opponentPieces, middlePos)) {
                 return false;
             }
            
             // If the moving piece isn't a king, we need to check that it is moving in the correct direction.
             if (!isKing && ((Utility.subtract(rowDestination, rowOrigin)) != 2 * direction)) {
                 return false;
             }

             return true;
         }

         return false;
     }


     // Game is over if there are no pieces for any of the sides or if there are no legal moves to make for the current player
     private boolean isGameOver() {
         return blackPieces == 0 || whitePieces == 0 || !hasLegalMoves();
     }

     
     // Method to check for legal moves
     private boolean hasLegalMoves() {
         long currentPieces = isBlackTurn ? blackPieces : whitePieces;
         for (int origin = 0; origin < 64; origin++) {
             if ((currentPieces & (1L << origin)) != 0) {
                 for (int destination = 0; destination < 64; destination++) {
                     if (isLegalMove(origin, destination)) {
                         return true;
                     }
                 }
             }
         }
         return false;
     }

     // Converts the position in String form to an index in the bitboard
     private int parsePosition(String pos) {
         int col = pos.charAt(0) - 'A';
         int row = pos.charAt(1) - '1';
         return row * 8 + col;
     }

     private String positionToString(int position) {
        char col = (char) ('A' + (position % 8));
        int row = (position / 8) + 1;
        return "" + col + row;
    }


     // Displays the board in a user-friendly form
     private void displayBoard() {
         System.out.println("   A   B   C   D   E   F   G   H");
         System.out.println(" +---+---+---+---+---+---+---+---+");
         for (int row = 7; row >= 0; row--) {
             System.out.print(row + 1 + "|");
             for (int col = 0; col < 8; col++) {
                 int pos = row * 8 + col;
                 long posBit = 1L << pos;
                 if (Utility.getBit(blackPieces, pos)) {
                     System.out.print(Utility.getBit(kings, pos) ? " B |" : " b |");
                 } else if (Utility.getBit(whitePieces, pos)) {
                     System.out.print(Utility.getBit(kings, pos) ? " W |" : " w |");
                 } else {
                     System.out.print(Utility.getBit(VALID_SQUARES, pos) ? "   |" : "   |");
                 }
             }
             System.out.println(row + 1);
             System.out.println(" +---+---+---+---+---+---+---+---+");
         }
         System.out.println("   A   B   C   D   E   F   G   H");
     }

     // Provides a binary representation of the board
     private void displayBinaryBoard(){
         System.out.println("Black pieces in binary: " + Utility.decimalToBinary(blackPieces));
         System.out.println("White pieces in binary: " + Utility.decimalToBinary(whitePieces));
         System.out.println("Kings in binary: " + Utility.decimalToBinary(kings));
     }

     // Provides a hexadecimal representation of the board
     private void displayHexBoard() {
         System.out.println("Black pieces in hexadecimal: " + Utility.decimalToHex(blackPieces));
         System.out.println("White pieces in hexadecimal: " + Utility.decimalToHex(whitePieces));
         System.out.println("Kings in hexadecimal: " + Utility.decimalToHex(kings));
     }

    public long getBlackPieces() {
        return blackPieces;
    }

    public long getWhitePieces() {
        return whitePieces;
    }

     public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Do you want to play against AI? (y/n)");
        boolean isAIGame = input.nextLine().trim().toLowerCase().startsWith("y");
        
        boolean isAIBlack = false;
        if (isAIGame) {
            System.out.println("Do you want to play as Black or White? (b/w)");
            isAIBlack = !input.nextLine().trim().toLowerCase().startsWith("b");
        }
        
        Board game = new Board(isAIGame, isAIBlack);
        game.play();
    }
}