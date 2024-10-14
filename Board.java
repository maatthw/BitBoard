import java.util.Scanner;
 class Board {
    private long blackPieces;
    private long whitePieces;
    private long kings;
    private boolean isBlackTurn;
    private static final long VALID_SQUARES = 0x55AA55AA55AA55AAL;

    public Board(){
        this.blackPieces = 0x000000000055AA55L;
        this.whitePieces = 0xAA55AA0000000000L;
        this.kings = 0L;
        isBlackTurn = true;
    }

    public void play() {
        Scanner input = new Scanner(System.in);
        while (!isGameOver()){
            displayBoard();
            System.out.println((isBlackTurn ? "Black" : "White") + "'s turn.");
            System.out.println("Enter move (Such as 'C3 D4'): ");
            String move = input.nextLine().toUpperCase();
            int origin = parsePosition(move.substring(0, 2));
            int destination = parsePosition(move.substring(3));

            if (isLegalMove(origin, destination)){
                movePiece(origin, destination);
                isBlackTurn = !isBlackTurn;
            } else {
                System.out.println("Illegal Move. Please try a different move.");
            }
            displayBinaryBoard();
            displayHexBoard();
        }
        displayBoard();
        System.out.println("Game over. " + (isBlackTurn ? "White" : "Black") + " wins.");
        input.close();
    }

     private void movePiece(int origin, int destination) {
         long pieces = isBlackTurn ? blackPieces : whitePieces;

         pieces = Utility.clearBit(pieces, origin);

         pieces = Utility.setBit(pieces, destination);

         if (isBlackTurn) {
             blackPieces = pieces;
         } else {
             whitePieces = pieces;
         }
     }


     private boolean isLegalMove(int origin, int destination) {
         long currentPieces = isBlackTurn ? blackPieces : whitePieces;

         if (!Utility.getBit(currentPieces, origin)) {
             return false;
         }

         if (destination < 0 || destination >= 64 || Utility.getBit(blackPieces | whitePieces, destination)) {
             return false;
         }

         long opponentPieces = isBlackTurn ? whitePieces : blackPieces;
         boolean isKing = Utility.getBit(kings, origin);

         long rowOrigin = Utility.divide(origin, 8);
         long colOrigin = origin % 8;
         long rowDestination = Utility.divide(destination, 8);
         long colDestination = destination % 8;

         if (Math.abs(rowOrigin - rowDestination) != Math.abs(colOrigin - colDestination)) {
             return false;
         }

         int direction = isBlackTurn ? 1 : -1;

         if (Math.abs(rowOrigin - rowDestination) == 1) {
             if (!isKing && ((rowDestination - rowOrigin) != direction)) {
                 return false;
             }
             return true;
         }

         if (Math.abs(rowOrigin - rowDestination) == 2) {
             long middleRow = Utility.divide((rowOrigin + rowDestination), 2);
             long middleCol = Utility.divide((colDestination + colOrigin), 2);
             long middlePos = Utility.add((Utility.multiply(middleRow, 8)), middleCol);

             if (!Utility.getBit(opponentPieces, middlePos)) {
                 return false;
             }

             if (!isKing && ((Utility.subtract(rowDestination, rowOrigin)) != 2 * direction)) {
                 return false;
             }

             return true;
         }

         return false;
     }


     private boolean isGameOver() {
         return blackPieces == 0 || whitePieces == 0 || !hasLegalMoves();
     }

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

     private int parsePosition(String pos) {
         int col = pos.charAt(0) - 'A';
         int row = pos.charAt(1) - '1';
         return row * 8 + col;
     }

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

     private void displayBinaryBoard(){
         System.out.println("Black pieces in binary: " + Utility.decimalToBinary(blackPieces));
         System.out.println("White pieces in binary: " + Utility.decimalToBinary(whitePieces));
         System.out.println("Kings in binary: " + Utility.decimalToBinary(kings));
     }

     private void displayHexBoard() {
         System.out.println("Black pieces in hexadecimal: " + Utility.decimalToHex(blackPieces));
         System.out.println("White pieces in hexadecimal: " + Utility.decimalToHex(whitePieces));
         System.out.println("Kings in hexadecimal: " + Utility.decimalToHex(kings));
     }

     public static void main(String[] args) {
         Board game = new Board();
         game.play();
     }
}