import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessorTest {

    private Processor processor;

    @BeforeEach
    void setUp() {
        processor = new Processor();
    }

    @Test
    void testADDInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x01, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(15, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testSUBInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x02, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(5, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testMULInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x03, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(50, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testDIVInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x04, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(2, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testANDInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x05, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(0, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testORInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x06, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(15, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testNOTInstruction() {
        processor.setRegister(1, 10);

        byte[] bytecode = {0x07, 0x01};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(-11, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testXORInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);

        byte[] bytecode = {0x08, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(15, processor.getRegister(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testMOVInstruction1() {
        byte[] bytecode = {0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getRegister(0));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testMOVInstruction2() {
        processor.setRegister(1, 10);

        byte[] bytecode = {0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getValueFromAddress(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testMOVIndirectInstruction1() {
        byte[] bytecode = {
                0x09, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                0x09, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x0B, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(9, processor.getRegister(1));
        assertEquals(3, processor.getInstructionCounter());
    }

    @Test
    void testMOVIndirectInstruction2() {
        processor.setRegister(1, 1);
        processor.setRegister(2, 10);

        byte[] bytecode = {
                0x0C, 0x01, 0x02};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getValueFromAddress(1));
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testMOVIndirectInstruction3() {

        byte[] bytecode = {
                0x09, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                0x0D, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(1, processor.getValueFromAddress(10));
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJMPInstruction1() {
        processor.setRegister(1, 10);

        byte[] bytecode = {0x0E, 0x01};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getProgramCounter());
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testJMPInstruction2() {
        byte[] bytecode = {0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC8  };

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(200, processor.getProgramCounter());
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testJMPInstruction3() {
        processor.setRegister(1, 0);

        byte[] bytecode = {0x10, 0x01};

        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(16, processor.getProgramCounter());
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testJEInstruction1() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 10);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x11, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJEInstruction2() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 10);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC8};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(200, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJEInstruction3() {
        processor.setRegister(1, 3);
        processor.setRegister(2, 3);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x13, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(19, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJNEInstruction1() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 20);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x14, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJNEInstruction2() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 20);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x15, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC8};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(200, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJNEInstruction3() {
        processor.setRegister(1, 3);
        processor.setRegister(2, 20);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x16, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(22, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJGEInstruction1() {
        processor.setRegister(1, 20);
        processor.setRegister(2, 10);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x17, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(20, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJGEInstruction2() {
        processor.setRegister(1, 20);
        processor.setRegister(2, 10);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x18, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC8};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(200, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJGEInstruction3() {
        processor.setRegister(1, 3);
        processor.setRegister(2, 2);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x19, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(25, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJLInstruction1() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 20);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x1A, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(10, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJLInstruction2() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 20);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x1B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xC8};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(200, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testJLInstruction3() {
        processor.setRegister(1, 3);
        processor.setRegister(2, 20);

        byte[] bytecode = {
                0x1D, 0x01, 0x02,
                0x1C, 0x01};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(28, processor.getProgramCounter());
        assertEquals(2, processor.getInstructionCounter());
    }

    @Test
    void testCMPInstruction() {
        processor.setRegister(1, 10);
        processor.setRegister(2, 10);

        byte[] bytecode = {0x1D, 0x01, 0x02};
        processor.loadProgram(bytecode);
        processor.run();

        assertEquals(true, processor.getZeroFlag());
        assertEquals(1, processor.getInstructionCounter());
    }

    @Test
    void testHALTInstruction() {
        byte[] bytecode = {0x00};
        processor.loadProgram(bytecode);
        processor.run();

        assertTrue(processor.getIsHalted());
    }
}