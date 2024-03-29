package org.cloud.xue.common.bean;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/11 下午1:51
 * @Version 1.0
 **/

public class User implements Serializable {
    public String uid;
    public String nickName;
    public volatile int age;

    public User() {

    }

    public User(String uid, String nickName) {
        this.uid = uid;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
