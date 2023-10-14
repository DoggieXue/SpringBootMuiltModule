package org.cloud.xue.visiable;

/**
 * @ClassName VolatileVar
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/6/6 10:11 上午
 * @Version 1.0
 **/
public class VolatileVar {
    volatile int var = 0;

    public void setVar (int var){
        System.out.println("setVar = " + var);
        this.var = var;
    }

    public static void main(String[] args) {
        VolatileVar var = new VolatileVar();
        var.setVar(20);
    }
}
