package algorithm;

import bean.MemoryBlock;
import bean.PCB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * <p> 进程调度 </p>
 *
 * @author LiZijing
 * @date 2021/12/12
 */
public class ProcessSchedule {

    //进程数
    private static int PROCESS_NUM = 5;

    //进程数组
    private List<PCB> pcbs = new ArrayList<>(PROCESS_NUM);
    //内存表
    private LinkedList<MemoryBlock> memoryBlocks = new LinkedList<>();

    public void initData() {
        Random r = new Random();
        for (int n = 0; n < PROCESS_NUM; n++) {
            PCB pcb = new PCB();
            pcb.setName("P" + (n + 1));
            // 随机服务时间 10~50
            pcb.setNeedTime((r.nextInt(5) + 1) * 10);
            // 随机产生需要的内存 30 - 100
            pcb.setNeedMemory((r.nextInt(7) + 1) * 10);
            pcb.setStatus("等待");

            if (n == 0) {
                pcb.setArriveTime(0);
                pcb.setBeginAdd(0);
            } else {
                pcb.setArriveTime((r.nextInt(5)) * 10);
                pcb.setBeginAdd(pcbs.get(n - 1).getBeginAdd() + pcbs.get(n - 1).getNeedMemory());
            }
            pcbs.add(pcb);
        }

    }

    public static void main(String[] args) {
        new ProcessSchedule().initData();
    }
}
