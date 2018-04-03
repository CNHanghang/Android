package com.example.hcszh.usb1_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_download:
                Toast.makeText(this, "下载", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, download_code.class);
                startActivity(intent);
                break;
            case R.id._item_transmit:
                Toast.makeText(this, "传输", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "hello");


       /* Button button1 = (Button) findViewById(R.id.button_open);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "正在打开文件",
                        Toast.LENGTH_SHORT).show();*/
  /*          }
        });*/
    }
}
