package com.nicerdata.bmobexample.bean;

import cn.bmob.v3.BmobObject;

/**
 * Author: HF
 * Email:  isayeshu@outlook.com | hf188680@qq.com
 * Date:   2015/11/20 0020
 * Description: Bmob后台数据表 _User
 */
public class Person extends BmobObject {

    private String name;
    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


}
