package com.example.psycounselplatform.ui.chat;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.psycounselplatform.data.model.Message;
import com.example.psycounselplatform.util.LogUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatViewMdoel extends ViewModel {
    private MutableLiveData<List<Message>> messageList;
    private MutableLiveData<String> result;
    private String contactId;

    public ChatViewMdoel(){
        messageList = new MutableLiveData<>();
        result = new MutableLiveData<>();
        contactId = "01";
        LogUtil.e("ChatViewModel", "contactId:" + contactId);
    }

    public MutableLiveData<List<Message>> getMessageList() {
        return messageList;
    }

    public void updateMessgae(){
//        messageList.setValue(new ArrayList<>());
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(contactId, 15, null, new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                List<Message> newMList = new ArrayList<>();
                for (int i = v2TIMMessages.size() - 1; i >= 0; i--
                ) {
                    V2TIMMessage v2Message = v2TIMMessages.get(i);
                    Message newMessage = new Message();
                    newMessage.setSelf(v2Message.isSelf());
                    newMessage.setContent(v2Message.getTextElem().getText());
                    newMessage.setProfile("");
                    newMList.add(newMessage);
                }
                messageList.postValue(newMList);
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
//        V2TIMManager.getConversationManager().getConversation(String.format("c2c_%s", contactId), new V2TIMValueCallback<V2TIMConversation>() {
//            @Override
//            public void onSuccess(V2TIMConversation v2TIMConversation) {
//
//            }
//
//            @Override
//            public void onError(int code, String desc) {
//
//            }
//        });

    }

    public MutableLiveData<String> getResult() {
        return result;
    }

    public void addListener(){
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            @Override
            public void onRecvC2CTextMessage(String msgID, V2TIMUserInfo sender, String text) {
                LogUtil.e("ChatViewModel", "content:" + text);

                List<Message> newMList = new ArrayList<>(messageList.getValue());
                Message newMessage = new Message();
                newMessage.setSelf(false);
                newMessage.setContent(text);
                newMessage.setProfile("");
                newMList.add(newMessage);
                messageList.postValue(newMList);
            }
        });
    }

    public void sendMessage(String message){
        V2TIMManager.getInstance().sendC2CTextMessage(message, contactId, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                LogUtil.e("ChatViewModel", "send success");
                List<Message> newMList = new ArrayList<>(messageList.getValue());
                Message newMessage = new Message();
                newMessage.setSelf(true);
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
