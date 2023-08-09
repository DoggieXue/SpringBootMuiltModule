package org.cloudxue.simplespringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Student
 * @Description:
 * @Author: Doggie
 * @Date: 2023年08月09日 13:52:28
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    public String name;
    public String sex;
    public int age;
    public String email;
    public String address;
    public String phone;
}
