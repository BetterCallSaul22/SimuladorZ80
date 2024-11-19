package z80box.z80box;

import z80box.z80box.Opcodes.MainBitOpcodes;
import z80box.z80box.Opcodes.MainTableOpcodes;

public class Instruction {
    // Buscar instrucción por clave
    public static String findMainTableInstruction(String clave) {
        if (MainTableOpcodes.opCodes.containsKey(clave)) {
            System.out.println(MainTableOpcodes.opCodes.get(clave));
            return MainTableOpcodes.opCodes.get(clave);
        } else {
            System.out.println("La clave '" + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findBitTableInstruction(String clave) {
        if (MainBitOpcodes.opCodes.containsKey("BC " + clave)) {
            System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return MainBitOpcodes.opCodes.get("BC " + clave);
        } else {
            System.out.println("La clave 'BC " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

}
