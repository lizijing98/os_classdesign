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
    int address = 0;
    int length = 0;

    String status = "free";

    public MemoryBlock(int address, int length) {
        this.address = address;
        this.length = length;
    }

}
