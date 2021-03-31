package com.shengsiyuan.decorator;

public class Client {
    public static void main(String[] args) {
        Component component = new ConcreteDecrator2(new ConcreteDecorator1(new ConcreteComponent()));
        component.doSomething();
    }
}
