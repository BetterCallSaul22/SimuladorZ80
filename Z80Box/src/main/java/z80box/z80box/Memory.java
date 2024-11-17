package z80box.z80box;

public class Memory {
    public String[][] m;

    public Memory() {
        // Crear un arreglo con 2048 filas y 16 columnas
        m = new String[2048][16];

        // Llenar el arreglo con "00" por defecto (esto es necesario para las celdas vacías)
        for (int i = 0; i < 2048; i++) {
            for (int j = 0; j < 16; j++) {
                m[i][j] = "00";  // Inicializamos todas las celdas con "00"
            }
        }
    }


    public void imprimirMemoria() {
        // Imprimir la primera fila (encabezado de columnas con valores hexadecimales del 0 al F)
        System.out.print("   ");  // Espacio vacío antes de los encabezados de las columnas
        for (int i = 0; i < 16; i++) {
            System.out.print(Integer.toHexString(i).toUpperCase() + "  "); // Imprimir la primera fila
        }
        System.out.println(); // Salto de línea

        // Imprimir las filas del arreglo
        for (int i = 0; i < 2048; i++) {
            // Imprimir la primera columna con los valores hexadecimales del 0 al 2047
            System.out.print(Integer.toHexString(i).toUpperCase() + " "); // Primera columna

            // Imprimir el resto de las celdas (siempre "00")
            for (int j = 0; j < 16; j++) {
                System.out.print(m[i][j] + " ");  // Imprime "00" o los valores en el arreglo
            }
            System.out.println(); // Salto de línea
        }
    }


}