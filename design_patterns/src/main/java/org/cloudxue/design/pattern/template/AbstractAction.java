package org.cloudxue.design.pattern.template;

import org.cloudxue.common.util.Print;

/**
 * @ClassName AbstractAction
 * @Description 模板模式中的抽象方法
 * @Author xuexiao
 * @Date 2022/4/28 上午10:42
 * @Version 1.0
 **/
public abstract class AbstractAction {

    /**
     * 模板方法
     */
    public void tempMethod() {
        Print.cfo("模板算法的算法骨架被执行...");
        beforeAction();
        action();
        afterAction();
    }

    /**
     * 钩子方法：由子类具体实现
     */
    public abstract void action();

    /**
     * 钩子方法执行前的操作
     */
    protected void beforeAction() {
        Print.cfo("准备执行钩子方法...");
    }

    /**
     * 钩子方法执行后操作
     */
    protected void afterAction() {
        Print.cfo("钩子方法执行完毕!");
    }
}
