package org.cloudxue.simplespringboot.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName CollectionTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/22 下午3:00
 * @Version 1.0
 **/
public class CollectionTest {
    public static void main(String[] args) {
        List<Customer> list = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("1");
        list.add(customer);
        customer = new Customer();
        customer.setId(2);
        customer.setName("2");
        list.add(customer);
        customer = new Customer();
        customer.setId(3);
        customer.setName("3");
        list.add(customer);
        customer = new Customer();
        customer.setId(4);
        customer.setName("4");
        list.add(customer);
        customer = new Customer();
        customer.setId(5);
        customer.setName("5");
        list.add(customer);

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(4);
        integers.add(5);
        integers.add(6);
        integers.add(7);
        //取差集
        List<Integer> integerss = integers.stream().filter(integer ->
                !list.stream().map(Customer::getId).collect(Collectors.toList()).contains(integer))
                .collect(Collectors.toList());
        System.out.println(integerss);
        List<Customer> listw = list.stream().filter(customer1 -> !integers.contains(customer1.getId())).collect(Collectors.toList());
        System.out.println(listw);
        //取并集
        List<Integer> integersb = integers.stream().filter(integer ->
                list.stream().map(Customer::getId).collect(Collectors.toList()).contains(integer))
                .collect(Collectors.toList());
        System.out.println(integersb);
        List<Customer> listb = list.stream().filter(customer1 -> integers.contains(customer1.getId())).collect(Collectors.toList());
        System.out.println(listb);

    }

}
