package z80box.z80box;

public class Miscellaneous {
    public static int calculateOverallStartValue(String hex){
        // Este metodo solo le hace un parse a una cadena hexadecimal obtenida
        // del campo de introducción de datos.
        return Integer.parseInt(hex, 16);
    }
}
