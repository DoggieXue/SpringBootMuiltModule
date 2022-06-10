package org.cloudxue.visiable;

/**
 * @ClassName InstructionReorder
 * @Description 指令重排序
 * @Author xuexiao
 * @Date 2022/6/10 10:29 上午
 * @Version 1.0
 **/
public class InstructionReorder {
    private static int x,y = 0;
    private static int a,b = 0;

    public static void main(String[] args) throws InterruptedException{
        int i = 0;
        for (;;){
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;

            Thread one = new Thread(() -> {
                a = 1;
                x = b;
            });

            Thread two = new Thread(() -> {
                b = 1;
                y = a;
            });

            one.start();
            two.start();
            one.join();
            two.join();

            String result = "第" + i + "次（" + x + ", " + y + "）";
            if (x == 0 && y ==0) {
                System.out.println(result);
            }
        }
    }
}
