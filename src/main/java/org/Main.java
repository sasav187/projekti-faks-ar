

public static void main(String[] args) {
    Processor processor = new Processor();

    processor.setRegister(1, 10);
    processor.setRegister(2, 0);

    byte[] bytecode = {
            0x0B, 0x01, 0x02,
            0x00};

    processor.loadProgram(bytecode);
    processor.run();

    processor.printRegisters();
    processor.printMemory();
}