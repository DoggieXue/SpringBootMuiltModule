package org.cloud.xue.design.pattern.decorator;

/**
 * @ClassName ConcreteDecoratorB
 * @Description 装饰模式-具体装饰角色
 * @Author xuexiao
 * @Date 2021/11/30 下午3:18
 * @Version 1.0
 **/
public class ConcreteDecoratorB extends Decorator{

    public ConcreteDecoratorB(Component component) {
        super(component);
    }

    public void doSomething() {
        super.doSomething();
        this.doAnotherThing();
    }

    public void doAnotherThing() {
        System.out.println("Function B:具体装饰角色包装的功能");
    }
}
