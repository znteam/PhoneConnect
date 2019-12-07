package com.commonlib.phoneconnectlib.bean;

/**
 * 手机联系人
 */
public class PhoneConnectUserBean {

    private String name;
    private String phone;

    public PhoneConnectUserBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
