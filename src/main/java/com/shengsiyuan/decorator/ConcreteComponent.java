package com.shengsiyuan.decorator;

public class ConcreteComponent implements Component{//类似FileInputStream
    @Override
    public void doSomething() {
        System.out.println("功能A");
    }
}
