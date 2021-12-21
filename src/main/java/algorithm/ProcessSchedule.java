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
    //    时间片
    private static int timeSlice = 10;
    //进程数组
    public List<PCB> waitingPcbs = new ArrayList<>(PROCESS_NUM);
    public List<PCB> finishPcbs = new ArrayList<>(PROCESS_NUM);
    private int time = 0;
    //内存表
    private LinkedList<MemoryBlock> memoryBlocks = new LinkedList<>();
    //页表
    private List<Integer> PageTable = new ArrayList<>();

    public static void main(String[] args) {
        ProcessSchedule ps = new ProcessSchedule();
        ps.initData();
        ps.run();
    }

    /**
     * <p> 初始化数据 </p>
     *
     * @author LiZijing
     * @date 2021/12/20
     */
    private void initData() {
        Random r = new Random();
        //初始化内存块数据

        //初始化进程数据
        for (int n = 0; n < PROCESS_NUM; n++) {
            PCB pcb = new PCB();
            pcb.setName("P" + (n + 1));
            // 随机服务时间 10~50
            pcb.setNeedTime((r.nextInt(5) + 1) * 10);
            // 设置需要内存均为 50MB
            pcb.setNeedMemory(50);
            pcb.setStatus("等待");
            List<Integer> pageNum = new ArrayList<>(5);
            for (int i = 0; i < 5; i++) {
                pageNum.add(r.nextInt(10));
            }
            pcb.setPageNumbers(pageNum);

            if (n == 0) {
                pcb.setArriveTime(0);
                pcb.setBeginAdd(0);
            } else {
//                设置随机到达时间 0—50
                pcb.setArriveTime((r.nextInt(5)) * 10);
                pcb.setBeginAdd(waitingPcbs.get(n - 1).getBeginAdd() + waitingPcbs.get(n - 1).getNeedMemory());
            }
            waitingPcbs.add(pcb);
        }
    }

    /**
     * <p> 高响应比调度算法进行 CPU 调度 </p>
     *
     * @author LiZijing
     * @date 2021/12/20
     */
    public void run() {
        //第一个运行的进程为第一个创建的进程
        PCB runningPCB = getRatioMaxOne();
        do {
            //如果当前运行进程阻塞或完成，重新获取新进程
            if (runningPCB.getStatus() == "阻塞" || runningPCB.getStatus() == "完成") {
                runningPCB = getRatioMaxOne();
                runningPCB.setStatus("运行");
            }
            //重设所有等待进程的响应比
            resetResponseRatio();
            //事件递增
            time += timeSlice;
        } while (!waitingPcbs.isEmpty());
    }


    /**
     * <p> 获取进程列表中响应比最大的进程 </p>
     *
     * @author LiZijing
     * @date 2021/12/20
     */
    public PCB getRatioMaxOne() {
        PCB result = waitingPcbs.get(0);
        for (PCB pcb : waitingPcbs) {
            if (result.getResponseRatio() < pcb.getResponseRatio()) {
                result = pcb;
            }
        }
        waitingPcbs.remove(result);
        result.setStatus("运行");
        return result;
    }

    /**
     * <p> 重设响应比 </p>
     *
     * @author LiZijing
     * @date 2021/12/20
     */
    private void resetResponseRatio() {
        for (PCB pcb : waitingPcbs) {
            if (pcb.getArriveTime() <= time) {
                pcb.setResponseRatio(1 + (time - pcb.getArriveTime()) / pcb.getNeedTime());
            }
        }
    }
}
