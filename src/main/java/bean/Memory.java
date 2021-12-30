package bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 内存管理 </p>
 *
 * @author LiZijing
 * @date 2021/12/25
 */
@Data
public class Memory {
    private int size;
    private List<MemoryBlock> memoryBlocks;

    public Memory(int size) {
        this.size = size;
        this.memoryBlocks = new ArrayList<>();
        this.memoryBlocks.add(new MemoryBlock(0, size));
    }

    /**
     * <p> 申请内存 </p>
     *
     * @param beginAdd   申请内存起始地址
     * @param usedMemory 申请内存长度
     * @author LiZijing
     * @date 2021/12/25
     */
    public void requestMemory(int beginAdd, int usedMemory) {
        //申请的内存
        MemoryBlock requestMemory = new MemoryBlock(beginAdd, usedMemory, "busy");
        //修改现有内存
        if (memoryBlocks.size() == 1) {
            MemoryBlock firstMemoryBlock = this.memoryBlocks.get(0);
            firstMemoryBlock.setAddress(beginAdd + usedMemory + 1);
            firstMemoryBlock.setLength(firstMemoryBlock.getLength() - usedMemory);
            //修改 List
            this.memoryBlocks.remove(0);
            this.memoryBlocks.add(firstMemoryBlock);
            this.memoryBlocks.add(0, requestMemory);
        } else {
            for (MemoryBlock block : memoryBlocks) {
                if (block.getStatus() == "free" && block.getLength() > usedMemory) {
                    MemoryBlock newFree = new MemoryBlock(beginAdd + usedMemory + 1, block.getLength() - usedMemory);
                    int blockSet = memoryBlocks.indexOf(block);
                    memoryBlocks.remove(block);
                    memoryBlocks.add(blockSet, requestMemory);
                    memoryBlocks.add(blockSet + 1, newFree);
                    break;
                } else if (block.getStatus() == "free" && block.getLength() == usedMemory) {
                    block.setStatus("busy");
                    break;
                }
            }
        }
    }


    /**
     * <p> 释放内存 </p>
     *
     * @param beginAdd 释放内存起始地址
     * @author LiZijing
     * @date 2021/12/25
     */
    public void releaseMemory(int beginAdd) {
        // 设置内存块为 free
        for (MemoryBlock block : this.memoryBlocks) {
            if (block.getAddress() == beginAdd) {
                block.setStatus("free");
                break;
            }
        }
        // 合并 free 内存块
        mergeMemory();
    }

    public void mergeMemory() {
        for (MemoryBlock block : memoryBlocks) {
            int index = memoryBlocks.indexOf(block);
            //如果是最后一个则退出
            if (index + 1 > memoryBlocks.size() - 1) {
                return;
            }
            MemoryBlock nextBlock = memoryBlocks.get(index + 1);
            if (block.getStatus() == "free" && nextBlock.getStatus() == "free") {
                MemoryBlock newBlock = new MemoryBlock(block.getAddress(), block.getLength() + nextBlock.getLength());
                memoryBlocks.remove(block);
                memoryBlocks.remove(nextBlock);
                memoryBlocks.add(index, newBlock);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Memory:\nsize=").append(size).append("\nmemoryBlocks:\n");
        for (MemoryBlock block : memoryBlocks) {
            sb.append(block.toString()).append("\n");
        }
        return sb.toString();
    }
}
