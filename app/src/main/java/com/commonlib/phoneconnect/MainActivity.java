package com.commonlib.phoneconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.commonlib.phoneconnectlib.PhoneConnectActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PhoneConnectActivity.startActivity(this, "test");
    }
}
