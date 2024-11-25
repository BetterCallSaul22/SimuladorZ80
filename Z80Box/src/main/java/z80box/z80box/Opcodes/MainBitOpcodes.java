package z80box.z80box.Opcodes;
import java.util.HashMap;
public class MainBitOpcodes {
    public static final HashMap<String, String> opCodes = new HashMap<>() {{
        put("CB 00", "RLC B");
        put("CB 01", "RLC C");
        put("CB 02", "RLC D");
        put("CB 03", "RLC E");
        put("CB 04", "RLC H");
        put("CB 05", "RLC L");
        put("CB 06", "RLC (HL)");
        put("CB 07", "RLC A");
        put("CB 08", "RRC B");
        put("CB 09", "RRC C");
        put("CB 0A", "RRC D");
        put("CB 0B", "RRC E");
        put("CB 0C", "RRC H");
        put("CB 0D", "RRC L");
        put("CB 0E", "RRC (HL)");
        put("CB 0F", "RRC A");
        put("CB 10", "RL B");
        put("CB 11", "RL C");
        put("CB 12", "RL D");
        put("CB 13", "RL E");
        put("CB 14", "RL H");
        put("CB 15", "RL L");
        put("CB 16", "RL (HL)");
        put("CB 17", "RL A");
        put("CB 18", "RR B");
        put("CB 19", "RR C");
        put("CB 1A", "RR D");
        put("CB 1B", "RR E");
        put("CB 1C", "RR H");
        put("CB 1D", "RR L");
        put("CB 1E", "RR (HL)");
        put("CB 1F", "RR A");
        put("CB 20", "SLA B");
        put("CB 21", "SLA C");
        put("CB 22", "SLA D");
        put("CB 23", "SLA E");
        put("CB 24", "SLA H");
        put("CB 25", "SLA L");
        put("CB 26", "SLA (HL)");
        put("CB 27", "SLA A");
        put("CB 28", "SRA B");
        put("CB 29", "SRA C");
        put("CB 2A", "SRA D");
        put("CB 2B", "SRA E");
        put("CB 2C", "SRA H");
        put("CB 2D", "SRA L");
        put("CB 2E", "SRA (HL)");
        put("CB 2F", "SRA A");
        put("CB 30", "SLL B");
        put("CB 31", "SLL C");
        put("CB 32", "SLL D");
        put("CB 33", "SLL E");
        put("CB 34", "SLL H");
        put("CB 35", "SLL L");
        put("CB 36", "SLL (HL)");
        put("CB 37", "SLL A");
        put("CB 38", "SRL B");
        put("CB 39", "SRL C");
        put("CB 3A", "SRL D");
        put("CB 3B", "SRL E");
        put("CB 3C", "SRL H");
        put("CB 3D", "SRL L");
        put("CB 3E", "SRL (HL)");
        put("CB 3F", "SRL A");
        put("CB 40", "BIT 0,B");
        put("CB 41", "BIT 0,C");
        put("CB 42", "BIT 0,D");
        put("CB 43", "BIT 0,E");
        put("CB 44", "BIT 0,H");
        put("CB 45", "BIT 0,L");
        put("CB 46", "BIT 0,(HL)");
        put("CB 47", "BIT 0,A");
        put("CB 48", "BIT 1,B");
        put("CB 49", "BIT 1,C");
        put("CB 4A", "BIT 1,D");
        put("CB 4B", "BIT 1,E");
        put("CB 4C", "BIT 1,H");
        put("CB 4D", "BIT 1,L");
        put("CB 4E", "BIT 1,(HL)");
        put("CB 4F", "BIT 1,A");
        put("CB 50", "BIT 2,B");
        put("CB 51", "BIT 2,C");
        put("CB 52", "BIT 2,D");
        put("CB 53", "BIT 2,E");
        put("CB 54", "BIT 2,H");
        put("CB 55", "BIT 2,L");
        put("CB 56", "BIT 2,(HL)");
        put("CB 57", "BIT 2,A");
        put("CB 58", "BIT 3,B");
        put("CB 59", "BIT 3,C");
        put("CB 5A", "BIT 3,D");
        put("CB 5B", "BIT 3,E");
        put("CB 5C", "BIT 3,H");
        put("CB 5D", "BIT 3,L");
        put("CB 5E", "BIT 3,(HL)");
        put("CB 5F", "BIT 3,A");
        put("CB 60", "BIT 4,B");
        put("CB 61", "BIT 4,C");
        put("CB 62", "BIT 4,D");
        put("CB 63", "BIT 4,E");
        put("CB 64", "BIT 4,H");
        put("CB 65", "BIT 4,L");
        put("CB 66", "BIT 4,(HL)");
        put("CB 67", "BIT 4,A");
        put("CB 68", "BIT 5,B");
        put("CB 69", "BIT 5,C");
        put("CB 6A", "BIT 5,D");
        put("CB 6B", "BIT 5,E");
        put("CB 6C", "BIT 5,H");
        put("CB 6D", "BIT 5,L");
        put("CB 6E", "BIT 5,(HL)");
        put("CB 6F", "BIT 5,A");
        put("CB 70", "BIT 6,B");
        put("CB 71", "BIT 6,C");
        put("CB 72", "BIT 6,D");
        put("CB 73", "BIT 6,E");
        put("CB 74", "BIT 6,H");
        put("CB 75", "BIT 6,L");
        put("CB 76", "BIT 6,(HL)");
        put("CB 77", "BIT 6,A");
        put("CB 78", "BIT 7,B");
        put("CB 79", "BIT 7,C");
        put("CB 7A", "BIT 7,D");
        put("CB 7B", "BIT 7,E");
        put("CB 7C", "BIT 7,H");
        put("CB 7D", "BIT 7,L");
        put("CB 7E", "BIT 7,(HL)");
        put("CB 7F", "BIT 7,A");
        put("CB 80", "RES 0,B");
        put("CB 81", "RES 0,C");
        put("CB 82", "RES 0,D");
        put("CB 83", "RES 0,E");
        put("CB 84", "RES 0,H");
        put("CB 85", "RES 0,L");
        put("CB 86", "RES 0,(HL)");
        put("CB 87", "RES 0,A");
        put("CB 88", "RES 1,B");
        put("CB 89", "RES 1,C");
        put("CB 8A", "RES 1,D");
        put("CB 8B", "RES 1,E");
        put("CB 8C", "RES 1,H");
        put("CB 8D", "RES 1,L");
        put("CB 8E", "RES 1,(HL)");
        put("CB 8F", "RES 1,A");
        put("CB 90", "RES 2,B");
        put("CB 91", "RES 2,C");
        put("CB 92", "RES 2,D");
        put("CB 93", "RES 2,E");
        put("CB 94", "RES 2,H");
        put("CB 95", "RES 2,L");
        put("CB 96", "RES 2,(HL)");
        put("CB 97", "RES 2,A");
        put("CB 98", "RES 3,B");
        put("CB 99", "RES 3,C");
        put("CB 9A", "RES 3,D");
        put("CB 9B", "RES 3,E");
        put("CB 9C", "RES 3,H");
        put("CB 9D", "RES 3,L");
        put("CB 9E", "RES 3,(HL)");
        put("CB 9F", "RES 3,A");
        put("CB A0", "RES 4,B");
        put("CB A1", "RES 4,C");
        put("CB A2", "RES 4,D");
        put("CB A3", "RES 4,E");
        put("CB A4", "RES 4,H");
        put("CB A5", "RES 4,L");
        put("CB A6", "RES 4,(HL)");
        put("CB A7", "RES 4,A");
        put("CB A8", "RES 5,B");
        put("CB A9", "RES 5,C");
        put("CB AA", "RES 5,D");
        put("CB AB", "RES 5,E");
        put("CB AC", "RES 5,H");
        put("CB AD", "RES 5,L");
        put("CB AE", "RES 5,(HL)");
        put("CB AF", "RES 5,A");
        put("CB B0", "RES 6,B");
        put("CB B1", "RES 6,C");
        put("CB B2", "RES 6,D");
        put("CB B3", "RES 6,E");
        put("CB B4", "RES 6,H");
        put("CB B5", "RES 6,L");
        put("CB B6", "RES 6,(HL)");
        put("CB B7", "RES 6,A");
        put("CB B8", "RES 7,B");
        put("CB B9", "RES 7,C");
        put("CB BA", "RES 7,D");
        put("CB BB", "RES 7,E");
        put("CB CB", "RES 7,H");
        put("CB BD", "RES 7,L");
        put("CB BE", "RES 7,(HL)");
        put("CB BF", "RES 7,A");
        put("CB C0", "SET 0,B");
        put("CB C1", "SET 0,C");
        put("CB C2", "SET 0,D");
        put("CB C3", "SET 0,E");
        put("CB C4", "SET 0,H");
        put("CB C5", "SET 0,L");
        put("CB C6", "SET 0,(HL)");
        put("CB C7", "SET 0,A");
        put("CB C8", "SET 1,B");
        put("CB C9", "SET 1,C");
        put("CB CA", "SET 1,D");
        put("CB CB", "SET 1,E");
        put("CB CC", "SET 1,H");
        put("CB CD", "SET 1,L");
        put("CB CE", "SET 1,(HL)");
        put("CB CF", "SET 1,A");
        put("CB D0", "SET 2,B");
        put("CB D1", "SET 2,C");
        put("CB D2", "SET 2,D");
        put("CB D3", "SET 2,E");
        put("CB D4", "SET 2,H");
        put("CB D5", "SET 2,L");
        put("CB D6", "SET 2,(HL)");
        put("CB D7", "SET 2,A");
        put("CB D8", "SET 3,B");
        put("CB D9", "SET 3,C");
        put("CB DA", "SET 3,D");
        put("CB DB", "SET 3,E");
        put("CB DC", "SET 3,H");
        put("CB DD", "SET 3,L");
        put("CB DE", "SET 3,(HL)");
        put("CB DF", "SET 3,A");
        put("CB E0", "SET 4,B");
        put("CB E1", "SET 4,C");
        put("CB E2", "SET 4,D");
        put("CB E3", "SET 4,E");
        put("CB E4", "SET 4,H");
        put("CB E5", "SET 4,L");
        put("CB E6", "SET 4,(HL)");
        put("CB E7", "SET 4,A");
        put("CB E8", "SET 5,B");
        put("CB E9", "SET 5,C");
        put("CB EA", "SET 5,D");
        put("CB EB", "SET 5,E");
        put("CB EC", "SET 5,H");
        put("CB ED", "SET 5,L");
        put("CB EE", "SET 5,(HL)");
        put("CB EF", "SET 5,A");
        put("CB F0", "SET 6,B");
        put("CB F1", "SET 6,C");
        put("CB F2", "SET 6,D");
        put("CB F3", "SET 6,E");
        put("CB F4", "SET 6,H");
        put("CB F5", "SET 6,L");
        put("CB F6", "SET 6,(HL)");
        put("CB F7", "SET 6,A");
        put("CB F8", "SET 7,B");
        put("CB F9", "SET 7,C");
        put("CB FA", "SET 7,D");
        put("CB FB", "SET 7,E");
        put("CB FC", "SET 7,H");
        put("CB FD", "SET 7,L");
        put("CB FE", "SET 7,(HL)");
        put("CB FF", "SET 7,A");
    }};
}
