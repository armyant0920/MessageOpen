package com.example.messageopen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    Bundle bundle;
    private String type,url,name;
    private Uri uri;
    private static String TAG ="Main2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

    }

    private void init(){
        TextView TV=findViewById(R.id.TV);
        TV.setOnClickListener(listener);
        bundle=getIntent().getExtras();
        type=bundle.getString(MainActivity.TYPE_INTENT);
        url=bundle.getString(MainActivity.URL_INTENT);
        name=bundle.getString(MainActivity.NAME_INTENT);
        TV.setText(type+"\n"+url+"\n"+name);

        if(type.equals("red")){
            TV.setBackgroundColor(Color.RED);
        }else if(type.equals("yellow")){
            TV.setBackgroundColor(Color.YELLOW);
        }else if(type.equals("green")){
            TV.setBackgroundColor(Color.GREEN);
        }
        if(!TextUtils.isEmpty(url)){
            uri= Uri.parse(url);
            Log.d(TAG,uri.toString()+"/////"+url);
        }else{
            uri = Uri.parse("http://google.com");
        }


    }

    View.OnClickListener listener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);





        }
    };
}
