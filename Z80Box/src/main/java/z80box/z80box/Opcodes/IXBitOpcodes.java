package z80box.z80box.Opcodes;

import java.util.HashMap;

public class IXBitOpcodes {
    public static final HashMap<String, String> opCodes = new HashMap<>() {{
        // DD CB prefixed Opcodes
        put("DD CB 06", "RLC (IX+D)");
        put("DD CB 0E", "RRC (IX+D)");
        put("DD CB 16", "RL (IX+D)");
        put("DD CB 1E", "RR (IX+D)");
        put("DD CB 26", "SLA (IX+D)");
        put("DD CB 2E", "SRA (IX+D)");
        put("DD CB 3E", "SRL (IX+D)");

        // BIT operations
        put("DD CB 46", "BIT 0,(IX+D)");
        put("DD CB 4E", "BIT 1,(IX+D)");
        put("DD CB 56", "BIT 2,(IX+D)");
        put("DD CB 5E", "BIT 3,(IX+D)");
        put("DD CB 66", "BIT 4,(IX+D)");
        put("DD CB 6E", "BIT 5,(IX+D)");
        put("DD CB 76", "BIT 6,(IX+D)");
        put("DD CB 7E", "BIT 7,(IX+D)");

        // RES operations
        put("DD CB 86", "RES 0,(IX+D)");
        put("DD CB 8E", "RES 1,(IX+D)");
        put("DD CB 96", "RES 2,(IX+D)");
        put("DD CB 9E", "RES 3,(IX+D)");
        put("DD CB A6", "RES 4,(IX+D)");
        put("DD CB AE", "RES 5,(IX+D)");
        put("DD CB B6", "RES 6,(IX+D)");
        put("DD CB BE", "RES 7,(IX+D)");

        // SET operations
        put("DD CB C6", "SET 0,(IX+D)");
        put("DD CB CE", "SET 1,(IX+D)");
        put("DD CB D6", "SET 2,(IX+D)");
        put("DD CB DE", "SET 3,(IX+D)");
        put("DD CB E6", "SET 4,(IX+D)");
        put("DD CB EE", "SET 5,(IX+D)");
        put("DD CB F6", "SET 6,(IX+D)");
        put("DD CB FE", "SET 7,(IX+D)");
    }};
}
