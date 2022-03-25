package com.example.psycounselplatform.ui.chat;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psycounselplatform.data.model.Message;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatViewMdoel extends ViewModel {
    private MutableLiveData<List<Message>> messageList;
    private MutableLiveData<String> result;

    public ChatViewMdoel(){
        messageList = new MutableLiveData<>();
        result = new MutableLiveData<>();
    }

    public MutableLiveData<List<Message>> getMessageList() {
        return messageList;
    }

    public void updateMessgae(){
        messageList.setValue(new ArrayList<>());

    }

    public MutableLiveData<String> getResult() {
        return result;
    }

    public void sendMessage(String message){
        V2TIMManager.getInstance().sendC2CTextMessage(message, "123456", new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                List<Message> newMList = new ArrayList<>(messageList.getValue());
                Message newMessage = new Message();
                newMessage.setMe(true);
                newMessage.setContent(message);
                newMessage.setProfile("");
                newMList.add(newMessage);
                messageList.postValue(newMList);
            }

            @Override
            public void onError(int code, String desc) {
                result.postValue(desc);
            }
        });
    }
}
