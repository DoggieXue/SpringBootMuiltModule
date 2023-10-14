package org.cloud.xue.design.pattern.template;

import org.cloud.xue.common.util.Print;

/**
 * @ClassName ActionA
 * @Description 模板模式中的子类
 * @Author xuexiao
 * @Date 2022/4/28 上午10:47
 * @Version 1.0
 **/
public class ActionA extends AbstractAction{

    @Override
    public void action() {
        Print.cfo("钩子方法的实现：ActionA.action()被执行...");
    }
}
