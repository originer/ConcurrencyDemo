package com.mmall.concurrency.example.proxy;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

class DemoClass{
    public String firstName;  
    private String endName;  
    private Date birthDay;  
    private String email;  
    private String phone;  
    private DemoClass parent;  

    public String getFirstName() {  
        return firstName;  
    }  
    public void setFirstName(String firstName) {  
        this.firstName = firstName;  
    }  
    public String getEndName() {  
        return endName;  
    }  
    public void setEndName(String endName) {  
        this.endName = endName;  
    }  
    public Date getBirthDay() {
        //try {
        //    //TimeUnit.MICROSECONDS.sleep(1);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        return birthDay;  
    }  
    public void setBirthDay(Date birthDay) {  
        this.birthDay = birthDay;  
    }  
    public String getEmail() {  
        return email;  
    }  
    public void setEmail(String email) {  
        this.email = email;  
    }  
    public String getPhone() {  
        return phone;  
    }  
    public void setPhone(String phone) {  
        this.phone = phone;  
    }  
    public DemoClass getParent() {  
        return parent;  
    }  
    public void setParent(DemoClass parent) {  
        this.parent = parent;  
    }  
}  

public class Test {

    public static void main(String[] args) throws Exception{  
        testInstance1();
        testInstance2();
        testVariable1();
        testVariable2();
        testCallmethod1();
        testCallmethod2();
    }  

    public static void testInstance1() {      //普通实例化对象
        int i = 0;  
        long start = System.currentTimeMillis();  
        while(i<1000000){  
            i++;  
            new DemoClass();  
        }  
        long end = System.currentTimeMillis();  
        System.err.println("普通实例化对象:"+(end - start) + " MillSeconds");
    }

    public static void testInstance2() throws Exception {  //反射实例化对象
        int i = 0;  
        long start = System.currentTimeMillis();  
        while(i<1000000){  
            i++;  
            DemoClass.class.newInstance();  
        }  
        long end = System.currentTimeMillis();  
        System.err.println("反射实例化对象:"+(end - start) + " MillSeconds");
    }

    public static void testVariable1() {     //普通获取变量
        int i = 0;  
        DemoClass dc = new DemoClass();  
        String s;  
        long start = System.currentTimeMillis();  
        while(i<1000000){  
            i++;  
            s = dc.firstName;  
        }  
        long end = System.currentTimeMillis();  
        System.err.println("普通获取变量:"+(end - start) + " MillSeconds");
    }

    public static void testVariable2() throws Exception {  //反射获取变量
        int i = 0;  
        DemoClass dc = new DemoClass();  
        String s;  
        long start = System.currentTimeMillis();  
        while(i<1000000){  
            i++;  
            s = (String) DemoClass.class.getField("firstName").get(dc);  
        }  
        long end = System.currentTimeMillis();  
        System.err.println("反射获取变量:"+(end - start) + " MillSeconds");
    }

    public static void testCallmethod1() {     //普通调用方法
        int i = 0;  
        DemoClass dc = new DemoClass();  
        long start = System.currentTimeMillis();  
        while(i<1000000){
            i++;  
            dc.getBirthDay();  
        }  
        long end = System.currentTimeMillis();  
        System.err.println("普通调用方法:"+(end - start) + " MillSeconds");
    }

    public static void testCallmethod2() throws Exception {    //反射调用方法
        int i = 0;  
        DemoClass dc = new DemoClass();  
        long start = System.currentTimeMillis();
        HashMap<String,Method> map  = new HashMap<>();
        while(i<1000000){
            i++;  
            DemoClass.class.getMethod("getBirthDay").invoke(dc);
            //if (map.get("getBirthDay")==null) {
            //    Method m = DemoClass.class.getMethod("getBirthDay");
            //    m.setAccessible(true);
            //    map.put("getBirthDay",m);
            //}

            map.get("getBirthDay").invoke(dc);
        }  
        long end = System.currentTimeMillis();  
        System.err.println("反射调用方法:"+(end - start) + " MillSeconds");
    }
}