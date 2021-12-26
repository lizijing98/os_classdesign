package bean;

import lombok.Data;

/**
 * <p> 内存块 </p>
 *
 * @author LiZijing
 * @date 2021/12/12
 */
@Data
public class MemoryBlock {
    private int address = 0;
    private int length = 0;

    private String status = "free";

    public MemoryBlock(int address, int length) {
        this.address = address;
        this.length = length;
    }

    public MemoryBlock(int address, int length, String status) {
        this.address = address;
        this.length = length;
        this.status = status;
    }
}
