public class Utility {

    public static long setBit(long num, long bitPosition) {
        return num | (1L << bitPosition);
    }

    public static long clearBit(long num, long bitPosition) {
        return num & ~(1L << bitPosition);
    }

    public static long toggleBit(long num, long bitPosition) {
        return num ^ (1L << bitPosition);
    }

    public static boolean getBit(long num, long bitPosition) {
        return ((num >> bitPosition) & 1) == 1;
    }

    public static long add(long a, long b) {
        return a + b;
    }

    public static long subtract(long a, long b) {
        return a - b;
    }

    public static long multiply(long a, long b) {
        return a * b;
    }

    public static long divide(long a, long b) {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    }

    public static String decimalToBinary(long decimal) {
        return Long.toBinaryString(decimal);
    }

    public static long binaryToDecimal(String binary) {
        return Long.parseLong(binary, 2);
    }

    public static String decimalToHex(long decimal) {
        return Long.toHexString(decimal);
    }

    public static long hexToDecimal(String hex) {
        return Long.parseLong(hex, 16);
    }

}