package z80box.z80box.Opcodes;

import java.util.HashMap;

public class IYOpcodes {
    public static final HashMap<String, String> opCodes = new HashMap<>() {{
        // FD prefiYed Opcodes
        put("FD 09", "ADD IY,BC");
        put("FD 19", "ADD IY,DE");
        put("FD 21", "LD IY,NN");
        put("FD 22", "LD (NN),IY");
        put("FD 23", "INC IY");
        put("FD 29", "ADD IY,IY");
        put("FD 2A", "LD IY,(NN)");
        put("FD 2B", "DEC IY");
        put("FD 34", "INC (IY+D)");
        put("FD 35", "DEC (IY+D)");
        put("FD 36", "LD (IY+D),N");
        put("FD 39", "ADD IY,SP");

        // Load operations
        put("FD 46", "LD B,(IY+D)");
        put("FD 4E", "LD C,(IY+D)");
        put("FD 56", "LD D,(IY+D)");
        put("FD 5E", "LD E,(IY+D)");
        put("FD 66", "LD H,(IY+D)");
        put("FD 6E", "LD L,(IY+D)");
        put("FD 70", "LD (IY+D),B");
        put("FD 71", "LD (IY+D),C");
        put("FD 72", "LD (IY+D),D");
        put("FD 73", "LD (IY+D),E");
        put("FD 74", "LD (IY+D),H");
        put("FD 75", "LD (IY+D),L");
        put("FD 77", "LD (IY+D),A");
        put("FD 7E", "LD A,(IY+D)");

        // Arithmetic operations
        put("FD 86", "ADD A,(IY+D)");
        put("FD 8E", "ADC A,(IY+D)");
        put("FD 96", "SUB (IY+D)");
        put("FD 9E", "SBC A,(IY+D)");
        put("FD A6", "AND (IY)");
        put("FD AE", "XOR (IY+D)");
        put("FD B6", "OR (IY+D)");
        put("FD BE", "CP (IY+D)");

        // Stack and control operations
        put("FD E1", "POP IY");
        put("FD E3", "EX (SP),IY");
        put("FD E5", "PUSH IY");
        put("FD E9", "JP (IY)");
        put("FD F9", "LD SP,IY");
    }};
}
