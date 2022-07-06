package org.cloudxue.design.pattern.masterworker;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName MasterWorkerTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/5/10 下午4:42
 * @Version 1.0
 **/
public class MasterWorkerTest {

    static class SimpleTask extends Task<Integer> {
        @Override
        protected Integer doExecute() {
            Print.tcfo("task " + getId() + " is done ");
            return getId();
        }
    }

    public static void main(String[] args) {
        Master<SimpleTask, Integer> master = new Master<>(4);

        ThreadUtil.scheduleAtFixedRate(() -> master.submit(
                new SimpleTask()),
                2, TimeUnit.SECONDS);

        ThreadUtil.scheduleAtFixedRate(() -> master.printResult(), 5, TimeUnit.SECONDS);
    }
}
