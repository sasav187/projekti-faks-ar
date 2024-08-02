import java.util.HashMap;
import java.util.Map;

public class CacheLevel {
    private int cacheSize;
    private int associativity;
    private int cacheLineSize;
    private Map<Long, CacheSet> cacheSets;
    private int cacheMissCount;
    private int accessCount;

    public CacheLevel(int cacheSize, int associativity, int cacheLineSize) {
        this.cacheSize = cacheSize;
        this.associativity = associativity;
        this.cacheLineSize = cacheLineSize;
        this.cacheSets = new HashMap<>();
        this.cacheMissCount = 0;
        this.accessCount = 0;
    }

    public byte readFromMemory(long address) {
        accessCount++;
        long blockIndex = address / cacheLineSize;
        long setIndex = blockIndex % (cacheSize / (associativity * cacheLineSize));

        if (cacheSets.containsKey(setIndex)) {
            CacheLine[] setLines = cacheSets.get(setIndex).getLines();
            for (int i = 0; i < associativity; i++) {
                if (setLines[i].isValid() && setLines[i].getTag() == blockIndex) {
                    // Hit u kešu, ažuriraj redoslijed korišćenja (LRU)
                    updateLRU(setLines, i);
                    return setLines[i].readData((int) (address % cacheLineSize));
                }
            }
        }

        // Keš promašaj, čitanje iz glavne memorije i zamjena linije koristeći LRU
        cacheMissCount++;
        byte[] blockData = readFromMainMemory(blockIndex * cacheLineSize);
        replaceLRU(setIndex, blockIndex, blockData);

        // Čitanje željenog bajta iz zamijenjene keš linije
        return cacheSets.get(setIndex).getLines()[0].readData((int) (address % cacheLineSize));
    }

    private byte[] readFromMainMemory(long startAddress) {
        // Simulacija čitanja bloka iz glavne memorije
        byte[] blockData = new byte[cacheLineSize];
        for (int i = 0; i < cacheLineSize; i++) {
            blockData[i] = (byte) (startAddress + i);
        }
        return blockData;
    }

    private void updateLRU(CacheLine[] setLines, int accessedIndex) {
        // Pomiči pristupenu liniju na kraj niza radi ažuriranja redoslijeda korišćenja (LRU)
        CacheLine accessedLine = setLines[accessedIndex];
        System.arraycopy(setLines, accessedIndex + 1, setLines, accessedIndex, associativity - 1 - accessedIndex);
        setLines[associativity - 1] = accessedLine;
    }

    private void replaceLRU(long setIndex, long tag, byte[] data) {
        // Zamjena najmanje korišćene (najstarije) linije u kešu (LRU)
        CacheLine[] setLines = new CacheLine[associativity];
        for (int i = 0; i < associativity; i++) {
            setLines[i] = new CacheLine(cacheLineSize);
        }
        setLines[associativity - 1].setValid(true);
        setLines[associativity - 1].setTag(tag);
        setLines[associativity - 1].setData(data);
        cacheSets.put(setIndex, new CacheSet(associativity, cacheLineSize));
    }

    public double getMissRate() {
        // Računanje procenata keš promašaja
        if (accessCount == 0) {
            return 0.0;
        }
        return (double) cacheMissCount / accessCount;
    }

    public void printCache() {
        // Prikaz stanja keš memorije
        for (Long setIndex : cacheSets.keySet()) {
            System.out.println("Set " + setIndex + ":");
            CacheLine[] setLines = cacheSets.get(setIndex).getLines();
            for (int i = 0; i < associativity; i++) {
                System.out.println("  Line " + i + ": Valid=" + setLines[i].isValid() + ", Tag=" + setLines[i].getTag());
            }
        }
    }
}

class CacheSet {
    private CacheLine[] lines;

    public CacheSet(int associativity, int lineSize) {
        this.lines = new CacheLine[associativity];
        for (int i = 0; i < associativity; i++) {
            lines[i] = new CacheLine(lineSize);
        }
    }

    public CacheLine[] getLines() {
        return lines;
    }
}

class CacheLine {
    private boolean valid;
    private long tag;
    private byte[] data;

    public CacheLine(int lineSize) {
        this.valid = false;
        this.tag = 0;
        this.data = new byte[lineSize];
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getTag() {
        return tag;
    }

    public void setTag(long tag) {
        this.tag = tag;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte readData(int offset) {
        return data[offset];
    }
}

