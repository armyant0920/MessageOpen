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
import android.content.SharedPreferences;
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

//特別注意,渠道一但建立之後,後面無法用程式碼修改配置,以使用者手動的配置為準,
//所以在這個案例中,使用了另闢新渠道的爛招,直接棄用被使用者更改過設定的舊渠道來實現
//這樣做會開一堆的渠道,而且還要存起來以避免下次開啟時應用程式去抓舊的渠道ID,且被刪除的渠道數量在app資訊欄中也會有紀錄
//所以純粹是拿來實驗硬開一個無視使用者意願,永遠最高級別的短信通知,常規還是跳到請使用者設置的畫面比較好


public class NotificationActivity extends AppCompatActivity {
    private static boolean msgOff;
    private Button btn_msg,btn_ads;
    private AlertDialog.Builder builder=null;
    private AlertDialog dialog=null;
    private String[]choice={"一律不開啟"};
    private int select;//儲存選項位置
    private static String TAG="NotififcationActivity";
    NotificationChannel channel_msg;
    private static String msgID;
    static SharedPreferences SP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



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

        String s="https://www.youtube.com/";
        Uri uri=Uri.parse("https://www.youtube.com/");
        //設置前往uri的意圖
        PendingIntent msgIntent=PendingIntent.getActivity(this,0,new Intent(Intent.ACTION_VIEW,uri),0);
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        channel_msg = manager.getNotificationChannel(msgID);

        //如果這個渠道通知功能被關閉
        if(channel_msg.getImportance() == NotificationManager.IMPORTANCE_NONE) {
            if (!msgOff) {//如果沒有一律關閉通知

                select=-1;//初始化勾選位置
                if (builder == null) {//初始化alertDialog

                    builder = new AlertDialog.Builder(this);

                    builder.setTitle("開啟通知權限")
                            //.setMessage("通知權限未開啟,直接按下確認後重新註冊channel,或勾選,以後不再提示")
                            .setSingleChoiceItems(choice, select, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    select = which;
                                }
                            });
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (select!=0) {//如果使用者沒有勾不再提醒,就重新註冊一個msg_channel

                                resetMsgImportance();

                            }else{
                                msgOff=true;
                            }
                        }
                    });
//                    builder.setNegativeButton("一律關閉", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            msgOff=true;
//                            Toast.makeText(getApplicationContext(), "不再提示,如需開啟通知請手動設定", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    builder.show();

                }else{
                    builder.show(); }
                Log.d(TAG,"msgID:"+channel_msg.getId()+"/"+"importance:"+channel_msg.getImportance());
            }
        }else{//渠道沒被關閉時
            msgOff=false;//重設以保證boolean值正確
            Log.d(TAG,"msgID:"+channel_msg.getId()+"/"+"importance:"+channel_msg.getImportance());
            //如果目前
            if(channel_msg.getImportance()!=NotificationManager.IMPORTANCE_HIGH){
            resetMsgImportance();
//                manager.deleteNotificationChannel(channel_msg.getId());
//                manager.createNotificationChannel(channel_msg);
//                channel_msg.setImportance(NotificationManager.IMPORTANCE_HIGH);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, msgID)
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





    public void resetMsgImportance() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //channel_msg = manager.getNotificationChannel(msgID);
        if (channel_msg.getImportance() != NotificationManager.IMPORTANCE_HIGH) {
            Log.d(TAG, String.valueOf(channel_msg.getImportance()));
            //取得原本channel的名稱及ID,設置importances
            msgID = channel_msg.getId() + "1";
            SP.edit().putString("msgID",msgID).commit();
            Log.d(TAG+"SP",SP.getString("msgID","msgID:1"));
            String channelName = channel_msg.getName().toString();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //刪除重要性被重製的舊channel
            manager.deleteNotificationChannel(channel_msg.getId());
            channel_msg=null;

            createNotificationChannel(msgID, channelName, importance);
            channel_msg=manager.getNotificationChannel(msgID);
            Log.d(TAG, channel_msg.getId() + "/" + channel_msg.getName() + "/" +channel_msg.getImportance());

            //用這個方法可以跳到android預設的設定畫面,手動設定,這也是正規的方法,
            /*NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = manager.getNotificationChannel("chat");
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                    startActivity(intent);
                    Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
                }
            }*/


        }
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
        //設定sharepreference
        SP=getSharedPreferences("Msg",MODE_PRIVATE);
        msgID=SP.getString("msgID","msg:1");

        //判斷android版本>8.0(=sdk26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //渠道1
            // String channelID = "msg";

            String channelID =msgID;
            String channelName = "短訊通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelID,channelName,importance);
            //渠道2
            channelID="ads";
            channelName="廣告消息";
            importance =NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelID,channelName,importance);


        }


    }

    //暫不使用
    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
