package com.shengsiyuan.decorator;

public class Decorator implements Component{
    private Component component;//持有一个抽象对象

    public Decorator(Component component){
        this.component = component;
    }
    @Override
    public void doSomething() {
        component.doSomething();
    }
}
