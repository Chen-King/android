package com.example.psycounselplatform.ui.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.psycounselplatform.databinding.ActivityPersonalBinding;
import com.example.psycounselplatform.util.LogUtil;

public class PersonalActivity extends AppCompatActivity {
    private PersonalViewModel personalViewModel;
    private ActivityPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        personalViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);
        personalViewModel.setContext(this);

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

        personalViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equals("success")){

                }
            }
        });

        binding.confirm.setOnClickListener(view -> {
            personalViewModel.confirm();
        });
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