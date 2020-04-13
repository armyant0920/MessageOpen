package com.example.messageopen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {

        public static final String TYPE_INTENT = "type";
        public static final String URL_INTENT = "url";
        public static final String NAME_INTENT = "name";
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
//如果是從網址開啟的
            Intent intent = getIntent();
            if (intent.getData() != null)
            {
                Uri uri = intent.getData();
                uri.getScheme();//獲取scheme
                uri.getHost();//獲取host
                uri.getAuthority();//獲取authority
                String type = uri.getQueryParameter(TYPE_INTENT);
                String url = uri.getQueryParameter(URL_INTENT);
                String name = uri.getQueryParameter(NAME_INTENT);
//標題轉UTF-8碼
                if (!TextUtils.isEmpty(name))
                {
                    try
                    {
                        name = URLDecoder.decode(name, "UTF-8");
                    } catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
//獲取到的引數跳轉
                Intent intentStart = new Intent(this,Main2Activity.class);
                intentStart.putExtra(TYPE_INTENT,type);
                intentStart.putExtra(URL_INTENT,url);
                intentStart.putExtra(NAME_INTENT,name);
                startActivity(intentStart);
                finish();
            }
        }
    }