package com.example.psycounselplatform.ui.personal.model;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Info implements Serializable {
    private  String name;
    private String phone;
    private String contact;
    private String contactPhone;

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String check() {
        List<Pair<String, String>> toCheck = new ArrayList<>();
        toCheck.add(new Pair<>("真实姓名", name));
        toCheck.add(new Pair<>("联系电话", phone));
        toCheck.add(new Pair<>("紧急联系人", contact));
        toCheck.add(new Pair<>("紧急联系人电话", contactPhone));

        for (Pair<String, String> entry : toCheck
        ) {
//            LogUtil.e("Info", entry.first + " " + entry.second);
            if (entry.second == null || entry.second.length() <= 0) {
                return entry.first + "不能为空";
            }
        }
        return null;
    }
}
