package com.example.psycounselplatform.ui.personal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psycounselplatform.ui.personal.model.Info;
import com.example.psycounselplatform.util.MyApplication;

public class PersonalViewModel extends ViewModel {
    private Context context;
    private MutableLiveData<Info> info;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> message;

    public PersonalViewModel() {
        info = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MutableLiveData<Info> getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info.setValue(info);
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.setValue(phone);
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
//            LogUtil.e("PersonalViewModel", tm.getLine1Number().substring(1));
            phone.setValue(tm.getLine1Number().substring(1));
        }
    }

    public void confirm() {
        String checkResult = info.getValue().check();
        if(checkResult == null){
            message.setValue("修改成功");
//            context.startActivity(new Intent(context, MainActivity.class));
        }else{
            message.setValue(checkResult);
        }
    }
}
