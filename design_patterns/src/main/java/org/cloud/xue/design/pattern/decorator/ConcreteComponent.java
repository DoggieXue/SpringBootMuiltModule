package org.cloud.xue.design.pattern.decorator;

/**
 * @ClassName ConcreteComponent
 * @Description 装饰模式-具体构件角色
 * @Author xuexiao
 * @Date 2021/11/30 下午3:13
 * @Version 1.0
 **/
public class ConcreteComponent implements Component{

    @Override
    public void doSomething() {
        System.out.println("Function A:具体构件角色实现的功能");
    }
}
