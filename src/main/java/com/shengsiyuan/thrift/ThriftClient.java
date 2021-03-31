package com.shengsiyuan.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.generated.Person;
import thrift.generated.PersonService;

public class ThriftClient {
    public static void main(String[] args) {
        TTransport transport = new TFramedTransport(new TSocket("localhost",8899),600);//传输层对象，和Server端保持一致
        TProtocol protocol = new TCompactProtocol(transport);//传输协议，和server必须使用的一样
        PersonService.Client client = new PersonService.Client(protocol);

        try{
            transport.open();
            Person person = client.getPersonByUsername("张三");
            System.out.println(person.getUsername() + ","+person.getAge()+","+person.isMarried());

            System.out.println("--------------------");

            Person person2 = new Person();
            person2.setUsername("李四");
            person2.setAge(30);
            person2.setMarried(true);

            client.savePerson(person2);

        }catch (Exception ex){
            System.out.println(ex);
        }finally {
            transport.close();
        }
    }
}
