import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {
    private Memory memory;

    @BeforeEach
    void setUp() {
        memory = new Memory();
    }

    @Test
    void testWrite() {
        long address = 0x1;
        byte value = 0x01;

        memory.write(address, value);

        byte readValue = memory.read(address);

        assertEquals(value, readValue);
    }
}