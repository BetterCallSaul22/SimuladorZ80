package z80box.z80box;

public class Registers {
    // Registros principales
    public int A, B, C, D, E, H, L;

    // Registros de 16 bits
    public String IX, IY;

    // Program Counter y Stack Pointer
    public static int PC, SP;

    // Flags
    private boolean Z; // Zero Flag
    private boolean CF; // Carry Flag
    private boolean S; // Sign Flag
    private boolean P; // Parity/Overflow Flag
    private boolean HC; // Half Carry Flag
    private boolean N; // Subtract Flag
    private boolean PS; // Positive Flag

    public Registers(){
        setA(0);
        setB(0);
        setC(0);
        setD(0);
        setE(0);
        setH(0);
        setL(0);
        setIX("0000");
        setIY("0000");
        setFlagZero(false);
        setFlagCarry(false);
        setFlagHalfCarry(false);
        setFlagSign(false);
        setFlagParity(false);
        setFlagSubtract(false);
        setPositiveFlag(false);
    }

    // Getters y setters para los registros principales
    public int getA() { return A; }
    public void setA(int A) { this.A = A; }

    public int getB() { return B; }
    public void setB(int B) { this.B = B; }

    public int getC() { return C; }
    public void setC(int C) { this.C = C; }

    public int getD() { return D; }
    public void setD(int D) { this.D = D; }

    public int getE() { return E; }
    public void setE(int E) { this.E = E; }

    public int getH() { return H; }
    public void setH(int H) { this.H = H; }

    public int getL() { return L; }
    public void setL(int L) { this.L = L; }

    public int getPC() { return PC; }
    public void setPC(int PC) { this.PC = PC; }

    public int getSP() { return SP; }
    public void setSP(int SP) { this.SP = SP; }

    // Getters y setters para los flags
    public boolean getFlagZero() {
        return Z;
    }

    public void setFlagZero(boolean Z) {
        this.Z = Z;
    }

    public boolean getFlagCarry() {
        return CF;
    }

    public void setFlagCarry(boolean CF) {
        this.CF = CF;
    }

    public boolean getFlagSign() {
        return S;
    }

    public void setFlagSign(boolean S) {
        this.S = S;
    }

    public boolean getFlagParity() {
        return P;
    }

    public void setFlagParity(boolean P) {
        this.P = P;
    }

    public boolean getFlagHalfCarry() {
        return HC;
    }

    public void setFlagHalfCarry(boolean HC) {
        this.HC = HC;
    }

    public boolean getFlagSubtract() {
        return N;
    }

    public void setFlagSubtract(boolean N) {
        this.N = N;
    }

    public boolean getPositiveFlag() {
        return PS;
    }

    public void setPositiveFlag(boolean PS) {
        this.PS = PS;
    }

    public String getIX() {
        return IX;
    }

    public void setIX(String IX) {
        this.IX = IX;
    }

    public String getIY() {
        return IY;
    }

    public void setIY(String IY) {
        this.IY = IY;
    }

    // Método para imprimir el estado de los registros
    public void imprimirRegistros() {
        System.out.println("A: " + Integer.toHexString(A));
        System.out.println("B: " + Integer.toHexString(B));
        System.out.println("C: " + Integer.toHexString(C));
        System.out.println("D: " + Integer.toHexString(D));
        System.out.println("E: " + Integer.toHexString(E));
        System.out.println("H: " + Integer.toHexString(H));
        System.out.println("L: " + Integer.toHexString(L));
        System.out.println("PC: " + Integer.toHexString(PC));
        System.out.println("SP: " + Integer.toHexString(SP));
        System.out.println("Flags: Z=" + Z + " C=" + C + " S=" + S + " P=" + P + " H=" + H + " N=" + N);
    }
}
