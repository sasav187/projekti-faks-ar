import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CacheLevelTest {

    private CacheLevel cacheLevel;

    @BeforeEach
    public void setUp() {
        // Postavljanje keša prije svakog testa
        cacheLevel = new CacheLevel(1024, 4, 64);
    }

    @Test
    public void testReadFromMemoryHit() {
        // Simuliramo čitanje iz keša (hit)
        byte data = cacheLevel.readFromMemory(64);

        // Očekujemo da se dobiju ispravni podaci
        assertEquals(64, data);
    }

    @Test
    public void testReadFromMemoryMiss() {
        // Simuliramo čitanje iz keša (promašaj)
        byte data = cacheLevel.readFromMemory(128);

        // Očekujemo da se dobiju ispravni podaci
        assertEquals(128, data);
    }

    @Test
    public void testGetMissRate() {
        // Simuliramo nekoliko čitanja adresa
        for (int i = 0; i < 10; i++) {
            cacheLevel.readFromMemory(i * 64);
        }

        // Očekujemo određeni procenat keš promašaja
        assertEquals(0.9, cacheLevel.getMissRate(), 0.01);
    }
}
