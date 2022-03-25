package com.example.psycounselplatform.ui.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.psycounselplatform.R;
import com.example.psycounselplatform.databinding.ActivityPersonalBinding;
import com.example.psycounselplatform.ui.baseView.PhotoView;
import com.example.psycounselplatform.ui.main.MainActivity;
import com.example.psycounselplatform.ui.personal.model.Info;
import com.example.psycounselplatform.util.LogUtil;

public class PersonalActivity extends AppCompatActivity {
    private PersonalViewModel personalViewModel;
    private Context context;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityPersonalBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);

        personalViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);
        personalViewModel.setContext(this);
        personalViewModel.setInfo(new Info());
        binding.setViewModel(personalViewModel);

        binding.profile.setOnClickListener(v -> {
            photoView.showDialog();
        });
        binding.gainPhone.setOnClickListener(view -> {
            personalViewModel.gainPhone();
        });

        personalViewModel.getPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LogUtil.e("PersonalActivity", "phone:" + s);
                binding.phone.setText(s);
            }
        });

//        personalViewModel.getName().observe(this, s -> {
//            LogUtil.e("PersonalActivity", "name = " + s);
//        });

        personalViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equals("修改成功")){
                    startActivity(new Intent(context, MainActivity.class));
                }
            }
        });

        binding.confirm.setOnClickListener(view -> {
            personalViewModel.confirm();
        });

        photoView = new PhotoView(context);
        photoView.setActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                LogUtil.e("PersonalActivity" , "request finish");
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    personalViewModel.gainPhone();
                }else{
                    LogUtil.e("PersonalActivity" , "fail");
                }
        }

    }
}