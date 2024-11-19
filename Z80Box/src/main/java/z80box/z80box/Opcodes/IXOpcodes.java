package z80box.z80box.Opcodes;

import java.util.HashMap;

public class IXOpcodes {
    public static final HashMap<String, String> opCodes = new HashMap<>() {{

        // DD prefixed Opcodes
        put("DD 09", "ADD IX,BC");
        put("DD 19", "ADD IX,DE");
        put("DD 21", "LD IX,NN");
        put("DD 22", "LD (NN),IX");
        put("DD 23", "INC IX");
        put("DD 29", "ADD IX,IX");
        put("DD 2A", "LD IX,(NN)");
        put("DD 2B", "DEC IX");
        put("DD 34", "INC (IX+D)");
        put("DD 35", "DEC (IX+D)");
        put("DD 36", "LD (IX+D),N");
        put("DD 39", "ADD IX,SP");
        put("DD 46", "LD B,(IX+D)");
        put("DD 4E", "LD C,(IX+D)");
        put("DD 56", "LD D,(IX+D)");
        put("DD 5E", "LD E,(IX+D)");
        put("DD 66", "LD H,(IX+D)");
        put("DD 6E", "LD L,(IX+D)");
        put("DD 70", "LD (IX+D),B");
        put("DD 71", "LD (IX+D),C");
        put("DD 72", "LD (IX+D),D");
        put("DD 73", "LD (IX+D),E");
        put("DD 74", "LD (IX+D),H");
        put("DD 75", "LD (IX+D),L");
        put("DD 77", "LD (IX+D),A");
        put("DD 7E", "LD A,(IX+D)");
        put("DD 86", "ADD A,(IX+D)");
        put("DD 8E", "ADC A,(IX+D)");
        put("DD 96", "SUB (IX+D)");
        put("DD 9E", "SBC A,(IX+D)");
        put("DD A6", "AND (IX)");
        put("DD AE", "XOR (IX+D)");
        put("DD B6", "OR (IX+D)");
        put("DD BE", "CP (IX+D)");
        put("DD E1", "POP IX");
        put("DD E3", "EX (SP),IX");
        put("DD E5", "PUSH IX");
        put("DD E9", "JP (IX)");
        put("DD F9", "LD SP,IX");
    }};
}
