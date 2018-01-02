package com.everestadvanced.datausedtoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {


    public Button btnHideNofi,btnClearData,btnshowNotifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHideNofi=(Button)findViewById(R.id.hideNoti);
        btnClearData=(Button)findViewById(R.id.clearDat);
        btnshowNotifi=(Button) findViewById(R.id.showNoti);

        btnHideNofi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notiResult=true;
                Intent svc3=new Intent(getApplicationContext(), BackgroundService.class);
                stopService(svc3);
            }
        });

        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService backgroundService=new BackgroundService();
                backgroundService.ClearData();
            }
        });

        btnshowNotifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent svc3=new Intent(getApplicationContext(), BackgroundService.class);
                startService(svc3);
            }
        });


    }
}
