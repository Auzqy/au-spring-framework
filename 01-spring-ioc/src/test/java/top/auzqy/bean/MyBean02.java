package top.auzqy.bean;

import lombok.Data;

/**
 * description:  
 * createTime: 2019-08-16 11:48
 * @author au
 */
@Data
public class MyBean02 {

    private String name02;

    private String age02;

    public void sayHello(){
        System.out.println("MyBean02 ----> sayHello()");
    }
}
