import java.util.HashMap;

public class Memory {
    private HashMap<Long, Byte> memory;

    public Memory() {
        this.memory = new HashMap<>();
    }

    public void write(long address, byte value) {
        memory.put(address, value);
    }

    public byte read(long address) {
        if (memory.containsKey(address) == false)
            throw new IllegalArgumentException("Ne postoji ništa na adresi " + address);

        return memory.get(address);
    }

    public void expand(long address) {
        memory.put(address, (byte) 0);
    }

    public void print() {
        for (Long i : memory.keySet())
            System.out.println("Adresa: " + i + " vrijednost: " + memory.get(i));
    }

    public int getSize() {
        return memory.size();
    }
}
