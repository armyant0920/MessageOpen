package com.example.messageopen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class NotificationActivity extends AppCompatActivity {
    private static boolean msgOff;
    private Button btn_msg,btn_ads;
    private AlertDialog.Builder builder=null;
    private AlertDialog dialog=null;
    private String[]choice={"開啟通知","一律不開啟"};
    private int select;//儲存選項位置
    private static String TAG="NotififcationActivity";
    NotificationChannel channel_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        //判斷android版本>8.0(=sdk26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //渠道1
            String channelID = "msg";
            String channelName = "短訊通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelID,channelName,importance);
            //渠道2
            channelID="ads";
            channelName="廣告消息";
            importance =NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelID,channelName,importance);


        }
        init();



    }
    @RequiresApi(Build.VERSION_CODES.O)//
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        //創建通知渠道
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);


    }

    public void sendMsg(View view){
        Random r=new Random();
        char c[]=new char[10+r.nextInt(11)];
        for( int i=0;i<c.length;i++){
            c[i]=(char)(65+r.nextInt(26));
        }
        String s="https://www.youtube.com/";
        Uri uri=Uri.parse("https://www.youtube.com/");
        PendingIntent msgIntent=PendingIntent.getActivity(this,0,new Intent(Intent.ACTION_VIEW,uri),0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationChannel channel = manager.getNotificationChannel("msg");
//        if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
//
//            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
//            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
//            startActivity(intent);
//            Toast.makeText(this, "請手動打開通知", Toast.LENGTH_SHORT).show();
//        }
        ////如果這個渠道通知功能被關閉
        if(channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
            if (msgOff==false) {//如果沒有一律關閉通知

                if (builder == null) {//初始化alertDialog

                    builder = new AlertDialog.Builder(this);

                    builder.setTitle("設定通知權限")
                            .setMessage("通知權限未開啟,直接按下確認前往開啟,或勾選選項以後不再提示")
                            .setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    select = which;
                                    //變更設定布林
                                    if(msgOff==false){
                                    msgOff = true;
                                    }else{
                                        msgOff=false;
                                    }
                                    Log.d(TAG, String.valueOf(select)+":"+msgOff);

                                }
                            });
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!msgOff) {
                                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "前往手動打開通知", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                    builder.setNegativeButton("一律關閉", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            msgOff=true;
                            Toast.makeText(getApplicationContext(), "不再提示,如需開啟通知請手動設定", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();


                }else{
                    builder.show();
                }
            }
        }else{
            msgOff=false;
            if(channel.getImportance() != NotificationManager.IMPORTANCE_HIGH){
                Log.d(TAG,String.valueOf(channel.getImportance()));
                channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
//            String channelID=channel.getId()+1;
//            String channelName=channel.getName().toString();
//
//
//            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.deleteNotificationChannel(channelID);
//
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel2 = new NotificationChannel(channelID, channelName, importance);
//            notificationManager.createNotificationChannel(channel2);
           }

//            private void createNotificationChannel(String channelId, String channelName, int importance) {
//                //創建通知渠道
//                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(
//                        NOTIFICATION_SERVICE);
//                notificationManager.createNotificationChannel(channel);


            Log.d(TAG,"msgLevel"+":"+channel.getImportance());
        }

        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);

        Notification notification = new NotificationCompat.Builder(this, "msg_1")
                .setContentTitle("一條新短信")
                .setContentText(s)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.msg)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.msg))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(msgIntent)
                .build();
        manager.notify(1, notification);


    }


    public void sendAds(View view){
        Random r=new Random();
        char c[]=new char[10+r.nextInt(11)];
        for( int i=0;i<c.length;i++){
            c[i]=(char)(65+r.nextInt(26));
        }
        Uri uri=Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dingdong1);
        PendingIntent adsIntent=PendingIntent.getActivity(this,0,new Intent(),0);
        String s=String.valueOf(c);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "ads")
                .setContentTitle("一條新廣告")

                .setContentText(s)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(s+s+s+s))
                .setWhen(System.currentTimeMillis())
                .setNumber(2)
                .setSmallIcon(R.drawable.ads)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ads))
                .setAutoCancel(true)
                .setContentIntent(adsIntent)
                .setColor(Color.RED)//這個color好像是給訊息文字用的

                .setDefaults(Notification.DEFAULT_LIGHTS)

                .setLights(Color.RED,1000,1000)

                .setSound(uri)
                .build();
        manager.notify(2, notification);


    }

    private void init(){
        btn_ads=findViewById(R.id.btn_ads);
        btn_msg=findViewById(R.id.btn_msg);


    }

    //暫不使用
    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
