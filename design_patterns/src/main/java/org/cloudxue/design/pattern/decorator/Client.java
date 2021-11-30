package org.cloudxue.design.pattern.decorator;

/**
 * @ClassName Client
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/30 下午3:24
 * @Version 1.0
 **/
public class Client {
    public static void main(String[] args) {
        Component component = new ConcreteDecoratorC(new ConcreteDecoratorB(new ConcreteComponent()));
        component.doSomething();
    }
}
