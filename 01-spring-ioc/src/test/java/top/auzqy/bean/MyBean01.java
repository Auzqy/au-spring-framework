package top.auzqy.bean;

import lombok.Data;

/**
 * description:  
 * createTime: 2019-08-16 12:43
 * @author au
 */
@Data
public class MyBean01 {

    private String name01;

    private String age01;

    public void sayHello(){
        System.out.println("MyBean01 ----> sayHello()");
    }
}
