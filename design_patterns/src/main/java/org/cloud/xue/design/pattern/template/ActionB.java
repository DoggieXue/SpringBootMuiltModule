package org.cloud.xue.design.pattern.template;

import org.cloud.xue.common.util.Print;

/**
 * @ClassName ActionB
 * @Description 模板模式中的子类
 * @Author xuexiao
 * @Date 2022/4/28 上午10:48
 * @Version 1.0
 **/
public class ActionB extends AbstractAction{

    @Override
    public void action() {
        Print.cfo("钩子方法的实现：ActionB.action()被执行...");
    }
}
