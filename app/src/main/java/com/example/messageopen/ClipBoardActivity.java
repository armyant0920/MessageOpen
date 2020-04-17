package com.example.messageopen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClipBoardActivity extends AppCompatActivity {
    private Button btn_cut,btn_paste;
    private ClipboardManager myClipboard;
    private  ClipData myClip;
    private EditText editText;
    private TextView pasteV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_board);
        init();


    }

    public void init(){
        myClipboard=  (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        btn_cut=findViewById(R.id.btn_cut);
        btn_cut.setOnClickListener(listener);
        btn_paste=findViewById(R.id.btn_paste);
        btn_paste.setOnClickListener(listener);
        editText=findViewById(R.id.editText);
        pasteV=findViewById(R.id.pasteView);


    }

    public void paste(){


        ClipData.Item item = myClip.getItemAt(0);
        String s = item.getText().toString();
        pasteV.setText(s);



    }

    public void cut(){


        myClip = ClipData.newPlainText("text", editText.getText().toString());
        myClipboard.setPrimaryClip(myClip);




    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_paste:
                    paste();
                    break;
                case R.id.btn_cut:
                    cut();
                    break;

            }

        }
    };
}
