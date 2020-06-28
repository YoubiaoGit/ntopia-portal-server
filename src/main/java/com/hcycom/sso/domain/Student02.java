package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student02 {


    private String name;

    private Integer age;

    public void getname() {
        System.out.println("这是在类中定义的一个方法");
    }


}
