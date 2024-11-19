package z80box.z80box.Opcodes;

import java.util.HashMap;

public class IYBitOpcodes {
    public static final HashMap<String, String> opCodes = new HashMap<>() {{
        // FD CB prefixed Opcodes
        put("FD CB 06", "RLC (IX+D)");
        put("FD CB 0E", "RRC (IX+D)");
        put("FD CB 16", "RL (IX+D)");
        put("FD CB 1E", "RR (IX+D)");
        put("FD CB 26", "SLA (IX+D)");
        put("FD CB 2E", "SRA (IX+D)");
        put("FD CB 3E", "SRL (IX+D)");

        // BIT operations
        put("FD CB 46", "BIT 0,(IX+D)");
        put("FD CB 4E", "BIT 1,(IX+D)");
        put("FD CB 56", "BIT 2,(IX+D)");
        put("FD CB 5E", "BIT 3,(IX+D)");
        put("FD CB 66", "BIT 4,(IX+D)");
        put("FD CB 6E", "BIT 5,(IX+D)");
        put("FD CB 76", "BIT 6,(IX+D)");
        put("FD CB 7E", "BIT 7,(IX+D)");

        // RES operations
        put("FD CB 86", "RES 0,(IX+D)");
        put("FD CB 8E", "RES 1,(IX+D)");
        put("FD CB 96", "RES 2,(IX+D)");
        put("FD CB 9E", "RES 3,(IX+D)");
        put("FD CB A6", "RES 4,(IX+D)");
        put("FD CB AE", "RES 5,(IX+D)");
        put("FD CB B6", "RES 6,(IX+D)");
        put("FD CB BE", "RES 7,(IX+D)");

        // SET operations
        put("FD CB C6", "SET 0,(IX+D)");
        put("FD CB CE", "SET 1,(IX+D)");
        put("FD CB D6", "SET 2,(IX+D)");
        put("FD CB DE", "SET 3,(IX+D)");
        put("FD CB E6", "SET 4,(IX+D)");
        put("FD CB EE", "SET 5,(IX+D)");
        put("FD CB F6", "SET 6,(IX+D)");
        put("FD CB FE", "SET 7,(IX+D)");
    }};
}
