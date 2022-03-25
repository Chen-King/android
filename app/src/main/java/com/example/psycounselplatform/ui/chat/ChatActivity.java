package com.example.psycounselplatform.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.psycounselplatform.R;
import com.example.psycounselplatform.data.model.Message;
import com.example.psycounselplatform.databinding.ActivityChatBinding;
import com.example.psycounselplatform.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private ChatViewMdoel chatViewMdoel;
    private ActivityChatBinding binding;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatViewMdoel = new ViewModelProvider(this).get(ChatViewMdoel.class);
        binding.setViewModel(chatViewMdoel);

        MessageAdapter messageAdapter = new MessageAdapter(context, new ArrayList<>());
        messageAdapter.setClickListener(this::onClick);
        binding.listMessage.setAdapter(messageAdapter);
        chatViewMdoel.updateMessgae();

        chatViewMdoel.getMessageList().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setmList(messages);
                messageAdapter.notifyDataSetChanged();
            }
        });

        chatViewMdoel.getResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        });

        ImageView ivPic = binding.ivPic;
        ImageView ivSend = binding.ivSend;

        ivSend.setOnClickListener(v -> {
            chatViewMdoel.sendMessage(binding.editInput.getText().toString());
            binding.editInput.setText("");
        });
        AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);
        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(500);
        TranslateAnimation disappearTranslate = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        disappearTranslate.setDuration(500);
        disappearTranslate.setFillAfter(true);
        TranslateAnimation appearTranslate = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        appearTranslate.setDuration(500);
        disappearTranslate.setFillAfter(true);
        binding.editInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    binding.layoutInfo.startAnimation(disappearTranslate);
                    disappearTranslate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            binding.layoutInfo.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }else{
                    LogUtil.e("ChatActivity", "focus lost");
                    binding.layoutInfo.startAnimation(appearTranslate);
                    binding.layoutInfo.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) context
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((ChatActivity)context).getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });
        binding.editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length() > 0){
                    if(ivPic.getVisibility() != View.GONE)
                        ivPic.setVisibility(View.GONE);
                    if(ivSend.getVisibility() == View.GONE){
                        ivSend.startAnimation(appearAnimation);
                        ivSend.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(ivSend.getVisibility() != View.GONE)
                        ivSend.setVisibility(View.GONE);
                    if(ivPic.getVisibility() == View.GONE){
                        ivPic.startAnimation(appearAnimation);
                        ivPic.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId() != R.id.edit_input && binding.editInput.hasFocus()){
            LogUtil.e("ChatActivity", "clear focus");
            binding.editInput.clearFocus();
        }
    }
}
