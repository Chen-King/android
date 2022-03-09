package com.example.psycounselplatform.ui.personal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psycounselplatform.ui.main.MainActivity;
import com.example.psycounselplatform.util.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalViewModel extends ViewModel {
    private Context context;
    private MutableLiveData<String> name;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> contact;
    private MutableLiveData<String> contactPhone;
    private MutableLiveData<String> message;

    public PersonalViewModel() {
        name = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        contact = new MutableLiveData<>();
        contactPhone = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public void setContact(String contact){
        this.contact.setValue(contact);
    }

    public MutableLiveData<String> getContact() {
        return contact;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone.setValue(contactPhone);
    }

    public MutableLiveData<String> getContactPhone() {
        return contactPhone;
    }

    public void setMessage(String message) {
        this.message.setValue(message);
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }



    public void gainPhone() {
        TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((PersonalActivity)context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
            setPhone(tm.getLine1Number().substring(1));
        }
    }

    public void confirm() {
        Map<String, String> toCheck = new HashMap<>();
        toCheck.put("真实姓名", name.getValue());
        toCheck.put("联系电话", phone.getValue());
        toCheck.put("紧急联系人", contact.getValue());
        toCheck.put("紧急联系人电话", contactPhone.getValue());

        for (Map.Entry<String, String> entry : toCheck.entrySet()
             ) {
            if(entry.getValue() == null || entry.getValue().length() <= 0){
                message.setValue(entry.getKey() + "不能为空");
                return;
            }
        }

        message.setValue("修改成功");
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
