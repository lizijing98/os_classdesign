package algorithm;

import bean.Memory;
import bean.PCB;
import bean.PageTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    //进程
    public PCB runningPCB;
    public List<PCB> waitingPcbs = new ArrayList<>(PROCESS_NUM);
    public List<PCB> blockPcbs = new ArrayList<>(PROCESS_NUM);
    public List<PCB> finishPcbs = new ArrayList<>(PROCESS_NUM);
    private int time = 0;
    //内存
    private Memory memory;
    //页表
    private PageTable pageTable;

    public static void main(String[] args) {
        ProcessSchedule ps = new ProcessSchedule();
        ps.initData();
        ps.run();
    }

    public static void Enter() {//停顿
        System.out.println("按回车继续");
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        memory = new Memory(512);
        //初始化页表
        pageTable = new PageTable(5);
        //初始化进程数据
        for (int n = 0; n < PROCESS_NUM; n++) {
            PCB pcb = new PCB();
            pcb.setPID("P" + (n + 1));
            // 随机服务时间 10~50
            pcb.setNeedTime((r.nextInt(5) + 1) * 10);
            // 设置需要内存均为 50MB
            pcb.setNeedMemory(50);
            pcb.setStatus("等待");
            List<Integer> pageNum = new ArrayList<>(5);
            for (int i = 0; i < 5; i++) {
                pageNum.add(r.nextInt(7) + 1);
            }
            pcb.setPageNumbers(pageNum);

            if (n == 0) {
                pcb.setArriveTime(0);
                pcb.setBeginAdd(0);
                memory.requestMemory(pcb.getBeginAdd(), pcb.getNeedMemory());
            } else {
//                设置随机到达时间 0—50
                pcb.setArriveTime((r.nextInt(5)) * 10);
                pcb.setBeginAdd(waitingPcbs.get(n - 1).getBeginAdd() + waitingPcbs.get(n - 1).getNeedMemory() + 1);
                memory.requestMemory(pcb.getBeginAdd(), pcb.getNeedMemory());
            }
            waitingPcbs.add(pcb);
        }
        printSys();
    }

    /**
     * <p> 高响应比调度算法进行 CPU 调度 </p>
     *
     * @author LiZijing
     * @date 2021/12/20
     */
    public void run() {
        //第一个运行的进程为第一个创建的进程
        runningPCB = getRatioMaxOne();
        Random r = new Random();
        int blockPCB = r.nextInt(4) + 1;
        //对等待进程进行调度
        do {
            //如果当前运行进程阻塞或完成，重新获取新进程
            if (runningPCB.getStatus() == "阻塞" || runningPCB.getStatus() == "完成") {
                runningPCB = getRatioMaxOne();
                runningPCB.setStatus("运行");
            }
            //随机阻塞一个进程
            if (runningPCB.getPID().equals("P" + blockPCB)) {
                runningPCB.setStatus("阻塞");
                time += runningPCB.getNeedTime();
                blockPcbs.add(runningPCB);
            } else {
                //执行进程命令
                runningPCB = pcbRunning(runningPCB);
                memory.releaseMemory(runningPCB.getBeginAdd());
                runningPCB.setStatus("完成");
                finishPcbs.add(runningPCB);
            }
            //重设所有等待进程的响应比
            resetResponseRatio();
            //打印状态
            printSys();
        } while (!waitingPcbs.isEmpty());
        //对阻塞进程进行调度
        runningPCB = blockPcbs.get(0);
        blockPcbs.remove(0);
        runningPCB.setStatus("运行");
        runningPCB = pcbRunning(runningPCB);
        memory.releaseMemory(runningPCB.getBeginAdd());
        runningPCB.setStatus("完成");
        finishPcbs.add(runningPCB);
        do {
            memory.mergeMemory();
        }while (memory.getMemoryBlocks().size()!=1);
        printSys();
    }

    /**
     * <p> 打印当前系统状态 </p>
     *
     * @author LiZijing
     * @date 2021/12/22
     */
    private void printSys() {
        System.out.println("-----------------------------------");
        System.out.println("当前系统时间:" + time);
        System.out.println("页表状态:" + pageTable);
        System.out.println("内存状态:");
        System.out.println(memory);
        System.out.println("运行进程:");
        System.out.println(runningPCB);
        System.out.println("等待进程:");
        for (PCB waitingPcb : waitingPcbs) {
            System.out.println(waitingPcb);
        }
        System.out.println("阻塞进程:");
        for (PCB blockPcb : blockPcbs) {
            System.out.println(blockPcb);
        }
        System.out.println("完成进程:");
        for (PCB finishPcb : finishPcbs) {
            System.out.println(finishPcb);
        }
        System.out.println("-----------------------------------");
        Enter();
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
                pcb.setResponseRatio(1.0 + (double) (time - pcb.getArriveTime()) / pcb.getNeedTime());
            }
        }
    }

    /**
     * <p> 模拟进程运行 </p>
     *
     * @param runningPCB 运行的 PCB
     * @author LiZijing
     * @date 2021/12/30
     */
    private PCB pcbRunning(PCB runningPCB) {
        List<String> status = new ArrayList<>(runningPCB.getPageNumbers().size());
        for (Integer pageNum : runningPCB.getPageNumbers()) {
            //页表命中
            if (pageTable.getNums().contains(pageNum)) {
                status.add("命中");
                int pageSet = pageTable.getNums().indexOf(pageNum);
                //命中项的 Miss 重置为 0
                pageTable.getMiss().set(pageSet, 0);
                //对页表中其他页表项的未命中数+1
                pageTable.addMiss(pageSet);
            }
            //页表缺页
            else {
                status.add("缺页");
                //采用 LRU 算法进行缺页处理
                //若页表未满，则向页表中进行添加，并对其他项的未命中数+1
                if (pageTable.getNums().size() != 10 && pageTable.getNums().size() != 0) {
                    pageTable.getNums().add(pageNum);
                    pageTable.getMiss().add(0);
                    pageTable.addMiss(pageTable.getNums().size() - 1);
                }
                //若页表已满，则选最近最久未访问项进行替换，并对其他项的未命中数+1
                else if (pageTable.getNums().size() == 10) {
                    int maxMissIndex = pageTable.getMissMax();
                    pageTable.getNums().set(maxMissIndex, pageNum);
                    pageTable.getMiss().set(maxMissIndex, 0);
                    pageTable.addMiss(maxMissIndex);
                }
                //页表为空时
                else {
                    pageTable.getNums().add(pageNum);
                    pageTable.getMiss().add(0);
                }
            }
        }
        runningPCB.setPageStatus(status);
        time += runningPCB.getNeedTime();
        return runningPCB;
    }
}
