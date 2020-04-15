package com.example.messageopen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {
        private static String TAG="MainActivity";
        public static final String TYPE_INTENT = "type";
        public static final String URL_INTENT = "url";
        public static final String NAME_INTENT = "name";
        public static final String ACTIVITY_INTENT = "activity";


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
                String activity=uri.getQueryParameter(ACTIVITY_INTENT);
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
                Intent IntentStart=new Intent();

                if(!TextUtils.isEmpty(activity)){
                    if(activity.equals("Test1")){
                        IntentStart.setClass(this,Test1Activity.class);
                    }else if(activity.equals("Test2")){
                        IntentStart.setClass(this,Test2Activity.class);
                    }else if(activity.equals("Test3")){
                        IntentStart.setClass(this,Test3Activity.class);

                    }else{
                        IntentStart.setClass(this,Main2Activity.class);

                    }
                    
                }else{
                    IntentStart.setClass(this,Main2Activity.class);
                }

                Log.d(TAG,IntentStart.getClass().toString());
               // Intent intentStart = new Intent(this,Main2Activity.class);

                IntentStart.putExtra(TYPE_INTENT,type);
                IntentStart.putExtra(URL_INTENT,url);
                IntentStart.putExtra(NAME_INTENT,name);
                startActivity(IntentStart);
                finish();
            }
        }
    }