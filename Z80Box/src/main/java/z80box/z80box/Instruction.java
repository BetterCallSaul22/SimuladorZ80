package z80box.z80box;

import z80box.z80box.Opcodes.*;

public class Instruction {

    // Buscar instrucción por clave
    public static String findMainTableInstruction(String clave) {
        if (MainTableOpcodes.opCodes.containsKey(clave)) {
            //System.out.println(MainTableOpcodes.opCodes.get(clave));
            return MainTableOpcodes.opCodes.get(clave);
        } else {
            System.out.println("La clave '" + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findBitTableInstruction(String clave) {
        if (MainBitOpcodes.opCodes.containsKey("BC " + clave)) {
            //System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return "BC " +  MainBitOpcodes.opCodes.get("BC " + clave);
        } else {
            System.out.println("La clave 'BC " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findIXTableInstruction(String clave) {
        if (IXOpcodes.opCodes.containsKey("DD " + clave)) {
            //System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return "DD " + " " + clave + " " +  IXOpcodes.opCodes.get("DD " + clave);
        } else {
            System.out.println("La clave 'DD " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findIXBitTableInstruction(String clave) {
        if (IXBitOpcodes.opCodes.containsKey("DD CB" + clave)) {
            //System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return "DD CB " + " " + clave + " " +  IXBitOpcodes.opCodes.get("DD CB " + clave);
        } else {
            System.out.println("La clave 'DD CB " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findIYTableInstruction(String clave) {
        if (IYOpcodes.opCodes.containsKey("FD " + clave)) {
            //System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return "FD " + " " + clave + " " +  IYOpcodes.opCodes.get("FD " + clave);
        } else {
            System.out.println("La clave 'FD " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findIYBitTableInstruction(String clave) {
        if (IYBitOpcodes.opCodes.containsKey("FD CB " + clave)) {
            //System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return "FD CB " + " " + clave + " " +  IYBitOpcodes.opCodes.get("FD CB " + clave);
        } else {
            System.out.println("La clave 'FD CB " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

    public static String findMiscTableInstruction(String clave) {
        if (MiscellaneousOpcodes.opCodes.containsKey("ED " + clave)) {
            //System.out.println(MainBitOpcodes.opCodes.get("BC " + clave));
            return "ED " + " " + clave + " " +  MiscellaneousOpcodes.opCodes.get("ED " + clave);
        } else {
            System.out.println("La clave 'ED " + clave + "' no existe en las instrucciones.");
            return null; // Retorna un valor por defecto
        }
    }

}
