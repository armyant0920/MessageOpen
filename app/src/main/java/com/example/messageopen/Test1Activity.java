package com.example.messageopen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Test1Activity extends AppCompatActivity {

    private static String TAG="Test1Activity";
    public static final String TYPE_INTENT = "type";
    public static final String URL_INTENT = "url";
    public static final String NAME_INTENT = "name";
    public static final String ACTIVITY_INTENT = "activity";

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        Log.d(TAG,"onCreate");


        Intent intent = getIntent();
        if (intent.getData() != null)
        {
            Uri uri = intent.getData();
            uri.getScheme();//獲取scheme
            uri.getHost();//獲取host
            uri.getAuthority();//獲取authority

//
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


        }
    }
}
