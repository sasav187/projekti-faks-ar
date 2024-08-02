import java.util.Arrays;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Processor {
    private long[] registers;
    private Memory memory;
    private int programCounter;

    private int memorySize;
    private boolean zeroFlag;
    private boolean isHalted;
    private int instructionCounter;

    public Processor() {
        this.registers = new long[4];
        this.memory = new Memory();
        this.programCounter = 0;

        this.zeroFlag = false;
        this.isHalted = false;
        this.instructionCounter = 0;
    }

    public void loadProgram(byte[] bytecode) {
        if (bytecode.length > memory.getSize()) {
            expandMemory();
        }

        // Učitavanje programa u memoriju
        for (int i = 0; i < bytecode.length; i++) {
            memory.write(i, bytecode[i]);
        }

        // Provjera da li je HALT instrukcija
        for (int i = 0; i < bytecode.length; i++) {
            if (bytecode[i] == 0) {
                isHalted = true;
            }
        }
    }

    public void expandMemory() {
        for (long address = memory.getSize(); address < 16; address++) {
            memory.expand(address);
        }
    }

    public void run() {
        while (programCounter < memory.getSize()) {
            byte instruction = memory.read(programCounter);
            switch (instruction) {
                case 0x01:
                    // ADD R1, R2
                    int reg1 = memory.read(programCounter + 1);
                    int reg2 = memory.read(programCounter + 2);
                    registers[reg1] += registers[reg2];
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x02:
                    // SUB R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    registers[reg1] -= registers[reg2];
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x03:
                    // MUL R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    registers[reg1] *= registers[reg2];
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x04:
                    // DIV R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    if (registers[reg2] != 0) {
                        registers[reg1] /= registers[reg2];
                    }
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x05:
                    // AND R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    registers[reg1] &= registers[reg2];
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x06:
                    // OR R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    registers[reg1] |= registers[reg2];
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x07:
                    // NOT R1
                    reg1 = memory.read(programCounter + 1);
                    registers[reg1] = ~registers[reg1];
                    programCounter += 2;
                    instructionCounter++;
                    break;

                case 0x08:
                    // XOR R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    registers[reg1] ^= registers[reg2];
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x09:
                    // MOV R1, value
                    reg1 = memory.read(programCounter + 1);
                    long value = getValue(programCounter + 2);
                    setRegister(reg1, value);
                    programCounter += 10;
                    instructionCounter++;
                    break;

                case 0x0A:
                    // MOV value, R1
                    value = getValue(programCounter + 1);
                    reg1 = memory.read(programCounter + 9);
                    memory.write(value, (byte) registers[reg1]);
                    programCounter += 10;
                    instructionCounter++;
                    break;

                case 0x0B:
                    // MOV R1, [R2]
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    value = memory.read(registers[reg2]);
                    setRegister(reg1, value);
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x0C:
                    // MOV [R1], R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    memory.write(registers[reg1], (byte) registers[reg2]);
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x0D:
                    // MOV [R1], const
                    reg1 = memory.read(programCounter + 1);
                    value = getValue(programCounter + 2);
                    memory.write(registers[reg1], (byte) value);
                    programCounter += 10;
                    instructionCounter++;
                    break;

                case 0x0E:
                    // JMP R1
                    reg1 = memory.read(programCounter + 1);
                    programCounter = (int) registers[reg1];
                    instructionCounter++;
                    break;

                case 0x0F:
                    // JMP address
                    value = getValue(programCounter + 1);
                    programCounter = (int) value;
                    instructionCounter++;
                    break;

                case 0x10:
                    // JMP [R1]
                    reg1 = memory.read(programCounter + 1);
                    value = memory.read(registers[reg1]);
                    programCounter = (int) value;
                    instructionCounter++;
                    break;

                case 0x11:
                    // JE R1
                    reg1 = memory.read(programCounter + 1);
                    if (zeroFlag) {
                        programCounter = (int) registers[reg1];
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x12:
                    // JE address
                    value = getValue(programCounter + 1);
                    if (zeroFlag) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 9;
                    }
                    instructionCounter++;
                    break;

                case 0x13:
                    // JE [R1]
                    reg1 = memory.read(programCounter + 1);
                    value = memory.read(registers[reg1]);
                    if (zeroFlag) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x14:
                    // JNE R1
                    reg1 = memory.read(programCounter + 1);
                    if (!zeroFlag) {
                        programCounter = (int) registers[reg1];
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x15:
                    // JNE address
                    value = getValue(programCounter + 1);
                    if (!zeroFlag) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 9;
                    }
                    instructionCounter++;
                    break;

                case 0x16:
                    // JNE [R1]
                    reg1 = memory.read(programCounter + 1);
                    value = memory.read(registers[reg1]);
                    if (!zeroFlag) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x17:
                    // JGE R1
                    reg1 = memory.read(programCounter + 1);
                    if (!zeroFlag || registers[0] >= registers[1]) {
                        programCounter = (int) registers[reg1];
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x18:
                    // JGE address
                    value = getValue(programCounter + 1);
                    if (!zeroFlag || registers[0] >= registers[1]) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 9;
                    }
                    instructionCounter++;
                    break;

                case 0x19:
                    // JGE [R1]
                    reg1 = memory.read(programCounter + 1);
                    value = memory.read(registers[reg1]);
                    if (!zeroFlag || registers[0] >= registers[1]) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x1A:
                    // JL R1
                    reg1 = memory.read(programCounter + 1);
                    if (!zeroFlag || registers[0] < registers[1]) {
                        programCounter = (int) registers[reg1];
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x1B:
                    // JL address
                    value = getValue(programCounter + 1);
                    if (!zeroFlag || registers[0] < registers[1]) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 9;
                    }
                    instructionCounter++;
                    break;

                case 0x1C:
                    // JL [R1]
                    reg1 = memory.read(programCounter + 1);
                    value = memory.read(registers[reg1]);
                    if (!zeroFlag || registers[0] < registers[1]) {
                        programCounter = (int) value;
                    }
                    else {
                        programCounter += 2;
                    }
                    instructionCounter++;
                    break;

                case 0x1D:
                    // CMP R1, R2
                    reg1 = memory.read(programCounter + 1);
                    reg2 = memory.read(programCounter + 2);
                    if (registers[reg1] == registers[reg2]) {
                        zeroFlag = true;
                    }
                    else {
                        zeroFlag = false;
                    }
                    programCounter += 3;
                    instructionCounter++;
                    break;

                case 0x1E:
                    // IN R1 char
                    reg1 = memory.read(programCounter + 1);

                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Unesite karakter: ");
                    String input = scanner.nextLine();
                    if (!input.isEmpty()) {
                        char inputRegister = input.charAt(0); // Učitavanje prvog unetog karaktera
                        setRegister(reg1, inputRegister);
                    }

                    programCounter += 2;
                    instructionCounter++;
                    break;

                case 0x1F:
                    // OUT R1
                    reg1 = memory.read(programCounter + 1);

                    char sign = (char) getRegister(reg1);
                    System.out.println("U registar R" + reg1 + " upisan je karakter " + sign);

                    programCounter += 2;
                    instructionCounter++;
                    break;

                case 0x00:
                    // HALT
                    System.out.println("Procesor je zaustavljen nakon " + instructionCounter + ". instrukcije.");
                    return;

                default:
                    throw new IllegalArgumentException("Neispravna instrukcija: " + instruction);
            }
        }
    }

    public long getValue(int index) {
        byte[] bytes = new byte[8];

        for (int i = 0; i < 8; i++) {
            bytes[i] = memory.read(index + i);
        }

        long value = convertToLong(bytes);
        return value;
    }

    public long convertToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();

        return buffer.getLong();
    }

    public long getAddress(int startIndex) {
        long address = 0;
        for (int i = 0; i < 8; i++) {
            address = (address << 8) | (memory.read(startIndex + i) & 0xFF);
        }
        return address;
    }

    public void printRegisters() {
        System.out.println();
        System.out.println("Registri: ");
        for (int i = 0; i < registers.length; i++) {
            System.out.println("R" + i + ": " + registers[i]);
        }
    }

    public void printMemory() {
        System.out.println();
        memory.print();
    }

    public void setRegister(int index, long value) {
        if (index < 0 || index >= registers.length) {
            throw new IllegalArgumentException("Neispravan indeks: " + index);
        }
        registers[index] = value;
    }

    public void setRegister(int index, char value) {
        if (index < 0 || index >= registers.length) {
            throw new IllegalArgumentException("Neispravan indeks: " + index);
        }
        registers[index] = value;
    }

    public long getRegister(int index) {
        if (index < 0 || index >= registers.length) {
            throw new IllegalArgumentException("Neispravan indeks: " + index);
        }
        return registers[index];
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public boolean getZeroFlag() {
        return zeroFlag;
    }

    public boolean getIsHalted() {
        return isHalted;
    }

    public int getInstructionCounter() {
        return instructionCounter;
    }

    public byte getValueFromAddress(long address) {
        return memory.read(address);
    }

    public int getMemorySize() { return memorySize; }
}