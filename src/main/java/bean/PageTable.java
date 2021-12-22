package bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 页表 </p>
 *
 * @author LiZijing
 * @date 2021/12/21
 */
@Data
public class PageTable {
    private List<Integer> nums;
    private List<Integer> miss;

    public PageTable() {
        this.nums = new ArrayList<>();
        this.miss = new ArrayList<>();
    }

    public PageTable(int size) {
        this.nums = new ArrayList<>(size);
        this.miss = new ArrayList<>(size);
    }

    /**
     * <p> 增加除Index位置外的未访问数 </p>
     *
     * @author LiZijing
     * @date 2021/12/21
     */
    public void addMiss(int index) {
        for (int i = 0; i < miss.size(); i++) {
            if (i != index && miss.get(index) != null){
                int oldMiss = miss.get(i);
                miss.set(i, ++oldMiss);
            }
        }
    }

    public int getMissMax() {
        int result = 0;
        for (Integer mis : miss) {
            if (mis >= result) {
                result = mis;
            }
        }
        return result;
    }
}
