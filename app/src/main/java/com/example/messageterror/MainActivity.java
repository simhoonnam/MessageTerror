package com.example.messageterror;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button terrorstart;
    EditText pnumber, cnt, contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefault();
    }

    private void setDefault() {
        terrorstart = (Button) findViewById(R.id.terrorstartbutton);
        pnumber = (EditText) findViewById(R.id.phonenumber);
        cnt = (EditText) findViewById(R.id.counttext);
        contents = (EditText) findViewById(R.id.contenttext);
        terrorstart.setOnClickListener(this);
    }

    public void sendSMS() {
        String smsNum = pnumber.getText().toString();
        String smsText = contents.getText().toString();
        int smsfor = Integer.parseInt(cnt.getText().toString());

        if (smsNum.length() > 0 && smsText.length() > 0 && smsfor > 0) {
                for(int i=0; i<smsfor ; i++) {
                    sendSMS(smsNum, smsText);
                }
                result();
        } else {
            Toast.makeText(MainActivity.this, "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(String smsNumber, String smsText) {

        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        if (mSmsManager != null)
            mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
        else Toast.makeText(MainActivity.this, "asdf", Toast.LENGTH_SHORT).show();
    }

    public void result(){
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.terrorstartbutton:
                sendSMS();
                break;
        }
    }
}
