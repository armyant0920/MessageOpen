package com.example.messageopen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        Bundle bundle=getIntent().getExtras();
        String type=bundle.getString(MainActivity.TYPE_INTENT);
        String url=bundle.getString(MainActivity.URL_INTENT);
        String name=bundle.getString(MainActivity.NAME_INTENT);


    }
}
