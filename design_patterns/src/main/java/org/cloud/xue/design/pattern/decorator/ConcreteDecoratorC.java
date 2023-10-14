package org.cloud.xue.design.pattern.decorator;

/**
 * @ClassName ConcreteDecorator
 * @Description 装饰模式-具体装饰角色
 * @Author xuexiao
 * @Date 2021/11/30 下午3:22
 * @Version 1.0
 **/
public class ConcreteDecoratorC extends Decorator{

    public ConcreteDecoratorC(Component component) {
        super(component);
    }

    @Override
    public void doSomething() {
        super.doSomething();
        this.doAnotherThing();
    }

    public void doAnotherThing() {
        System.out.println("Function C:具体装饰角色包装的功能");
    }
}
