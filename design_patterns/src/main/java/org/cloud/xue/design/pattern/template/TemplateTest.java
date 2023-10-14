package org.cloud.xue.design.pattern.template;

/**
 * @ClassName TemplateTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/28 上午10:49
 * @Version 1.0
 **/
public class TemplateTest {
    public static void main(String[] args) {
        AbstractAction action = null;
        action = new ActionA();
        action.tempMethod();

        action = new ActionB();
        action.tempMethod();

    }
}
