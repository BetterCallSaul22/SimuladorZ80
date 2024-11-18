package z80box.z80box;

public class Miscellaneous {
    public static int calculateOverallStartValue(String hex){
        // Este metodo solo le hace un parse a una cadena hexadecimal obtenida
        // del campo de introducción de datos.
        return Integer.parseInt(hex, 16);
    }

    public static String returnHexEquivalent(int num){
        return switch (num) {
            case 10 -> "A";
            case 11 -> "B";
            case 12 -> "C";
            case 13 -> "D";
            case 14 -> "E";
            case 15 -> "F";
            default -> null;
        };
    }
}
