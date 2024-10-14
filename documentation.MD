Checkers Game Documentation

# Overview
This game is an implementation of the classic game of checkers in the form of a bitboard.
The game has been created through the use of a Board class and a Utility class, which both work together to implement and display the game.

# Classes

## Utility
The Utility class serves the purpose of handling operations for bits as well as conversions.
- Methods
    - setBit(long num, long bitPosition)
        - Will set the bit in the specified position in the long.
    - clearBit(long num, long bitPosition)
        - Will clear the bit in the specificed position in the long.
    - toggleBit(long num, long bitPosition)
        - Will toggle the bit in the specified position in the long.
    - getBit(long num, long bitPosition)
        - Will return the value of the bit in the specified position in the long.
    - add(long a, long b)
        - Will add two longs
    - subtract(long a, long b)
        - Will subtract two longs
    - multiply(long a , long b)
        - Will multiply two longs
    - divide(long a, long b)
        - Will divide two longs, checking as well that b != 0
    - decimalToBinary(long decimal)
        - Converts a decimal number into binary.
    - binaryToDecimal(String binary)
        - Converts a binary number in String form to a decimal number.
    - decimalToHex(long decimal)
        - Converts a decimal number to hex.
    - hexToDecimal(String hex)
        - Converts a hex number in String form to a decimal number.

## Board
The Board class handles a lot of the other necessary functionality needed for the Checkers game and ensures everything is running correctly and smoothly.
- Attributes
    - blackPieces
        - The pieces for black stored in a long.
    - whitePieces
        - The pieces for white stored in a long.
    - kings
        - All the kings in the game stored in a long.
    - isBlackTurn
        - Boolean representing whether or not it is black's turn.
    - VALID_SQUARES
        - All the valid squares in a game of Checkers
- Methods
    - play()
        - The main function which loops until the game is over and essentially runs things.
    - movePiece(int origin, int destination)
        - Makes a move for the current player from origin to destination.
    - isLegalMove(int origin, int destination)
        - Checks if the move from origin to destination is valid / legal given the current board state and rules of the game.
    - isGameOver()
        - Checks if the game is over by seeing if either players pieces are empty or if there are no legal moves to be made for the current player.
    - hasLegalMoves()
        - Checks to see if the current player has any legal moves that they can make.
    - displayBoard()
        - Displays the current state of the game in a 2 dimensional checkers-like board.
    - displayBinaryBoard()
        - Displays the current state of the game in binary.
    - displayHexBoard()
        - Displays the current state of the game in hex.