package org.cloud.xue.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年10月01日 12:51:37
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    public String name;
    public String sex;
    public int age;
    public String email;
    public String address;
    public String phone;
}
