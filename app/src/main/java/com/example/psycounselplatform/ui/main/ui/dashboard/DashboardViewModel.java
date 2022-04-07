package com.example.psycounselplatform.ui.main.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psycounselplatform.util.LogUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> result;
    private String contactId;

    public DashboardViewModel() {
        result = new MutableLiveData<>();
//        contactId = V2TIMManager.getInstance().getLoginUser().equals("123456@qq.com") ? "1980055813@qq.com" : "123456@qq.com";
        contactId = "01";
    }

    public MutableLiveData<String> getResult() {
        return result;
    }


    public void setAsReaded(){
        V2TIMManager.getMessageManager().markC2CMessageAsRead(contactId, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                LogUtil.e("ChatViewModel","成功将与" + contactId + "的对话置为已读");
            }

            @Override
            public void onError(int code, String desc) {
                result.postValue(desc);
            }
        });
    }
}