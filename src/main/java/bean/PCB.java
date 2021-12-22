package bean;

import lombok.Data;

import java.util.List;

/**
 * <p> PCB </p>
 *
 * @author LiZijing
 * @date 2021/12/12
 */
@Data
public class PCB {
    //     进程名称
    String name;
    //     所需内存
    int needMemory;
    //    内存起址
    int beginAdd;
    //    到达时间
    int arriveTime;
    //    所需时间
    int needTime;
    //    已用时间
//    int usedTime;
    //    状态:等待、运行、阻塞、完成
    String status;
    //    页表数
    List<Integer> pageNumbers;
    //页表命中状态
    List<String> pageStatus;
    //     响应比：1+作业等待/作业处理
    double responseRatio;

    @Override
    public String toString() {
        return "PCB{" +
                "name='" + name + '\'' +
//                ", needMemory=" + needMemory +
//                ", beginAdd=" + beginAdd +
                ", arriveTime=" + arriveTime +
                ", needTime=" + needTime +
                ", status='" + status + '\'' +
                ", pageNumbers=" + pageNumbers +
                ", pageStatus=" + pageStatus +
                ", responseRatio=" + responseRatio +
                '}';
    }
}
