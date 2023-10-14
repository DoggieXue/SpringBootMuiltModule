package org.cloud.xue.design.pattern.decorator;

/**
 * @ClassName Decotator
 * @Description 装饰模式-装饰角色
 * @Author xuexiao
 * @Date 2021/11/30 下午3:15
 * @Version 1.0
 **/
public class Decorator implements Component{
    //持有一个构件角色的引用
    private Component component;

    public Decorator (Component component) {
        this.component = component;
    }
    //通过实现构件对象接口，来定义一个与抽象构件接口一致的接口
    @Override
    public void doSomething() {
        component.doSomething();
    }
}
