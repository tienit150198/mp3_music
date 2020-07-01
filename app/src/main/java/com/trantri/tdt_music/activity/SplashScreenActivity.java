package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trantri.tdt_music.databinding.ActivitySplashScreenBinding;
import com.trantri.tdt_music.utils.NetworkUtils;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Handler().postDelayed(() -> {
            binding.processSplash.setIndeterminate(false);

            if(NetworkUtils.isOnline(this)){
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }else{
                Toast.makeText(this, "Please check your wifi", Toast.LENGTH_SHORT).show();
            }
                finish();


        },2000);
    }
}
