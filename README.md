# Simple Processor Simulator
This Java project implements a simple processor simulator with a memory management system. The simulator supports basic arithmetic and logic operations, memory manipulation, and control flow instructions.

The simulator consists of two main classes: `Memory` and `Processor`. The `Memory` class provides a simple interface for reading from and writing to memory addresses, while the `Processor` class simulates the behavior of a CPU with a set of registers and a program counter.

## Features
* **Memory Management**: Read, write, and expand memory.
* **Arithmetic Operations**: Addition, subtraction, multiplication, and division.
* **Logic Operations**: AND, OR, NOT, XOR.
* **Data Movement**: Move data between registers and memory.
* **Control Flow**: Conditional and unconditional jumps.
* **Input/Output**: Basic input and output operations.
  
## Usage
To use the processor simulator, you can write a Java program that creates an instance of the Processor class, loads a program (bytecode) into memory, and runs the program.

### Example:

```
public class Main {
    public static void main(String[] args) {
        Processor processor = new Processor();

        byte[] program = {
            0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, // MOV R0, 10
            0x09, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x14, // MOV R1, 20
            0x01, 0x00, 0x01, // ADD R0, R1
            0x00 // HALT
        };

        processor.loadProgram(program);
        processor.run();
        processor.printRegisters();
    }
}
```

## Instructions
The processor supports a variety of instructions. Here are some examples:

* `0x01 R1 R2` - ADD R1, R2
* `0x02 R1 R2` - SUB R1, R2
* `0x03 R1 R2` - MUL R1, R2
* `0x04 R1 R2` - DIV R1, R2
* `0x09 R1 value` - MOV R1, value
* `0x0A value R1` - MOV value, R1
* `0x0E R1` - JMP R1
* `0x11 R1` - JE R1

## Memory Class
The `Memory` class provides methods for reading from and writing to memory addresses, as well as expanding the memory size.

### Methods
* `public void write(long address, byte value)` - Write a byte to a specified memory address.
* `public byte read(long address)` - Read a byte from a specified memory address.
* `public void expand(long address)` - Expand the memory to a specified address.
* `public void print()` - Print the contents of the memory.
* `public int getSize()` - Get the size of the memory.
  
## Processor Class
The `Processor` class simulates the behavior of a CPU with a set of registers and a program counter.

### Methods
* `public void loadProgram(byte[] bytecode)` - Load a program (bytecode) into memory.
* `public void run()` - Run the loaded program.
* `public void printRegisters()` - Print the contents of the registers.
* `public void printMemory()` - Print the contents of the memory.
  
### Registers
The processor has four registers (R0, R1, R2, R3) for storing data.

## Unit Tests
The project includes unit tests for the `Memory` and `Processor` classes. These tests ensure the correct functionality of the memory management and processor operations.

### Running Tests
To run the tests, use a testing framework like JUnit. 

Example:

```
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessorTest {
    @Test
    void testADDInstruction() {
        Processor processor = new Processor();
        processor.setRegister(1, 10);
        processor.setRegister(2, 5);
        
        byte[] bytecode = {0x01, 0x01, 0x02};
        
        processor.loadProgram(bytecode);
        processor.run();
        
        assertEquals(15, processor.getRegister(1));
    }
}
```

## Contributing
Contributions are welcome! If you have suggestions for improvements or find any bugs, please open an issue or submit a pull request.
