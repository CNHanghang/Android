package com.example.hcszh.usb1_0;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import com.usbxyz.usbtransmit.USBTransmit;

public class MainActivity extends AppCompatActivity {

    public String path = "";
    public String realPath = "";
    USBTransmit mUSBTransmit;

    //TextView textView;
    public class ConnectStateChanged implements USBTransmit.DeviceConnectStateChanged {
        @Override
        public void stateChanged(boolean connected) {
            if (connected) {
                Toast.makeText(MainActivity.this, "设备已连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "设备断开连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_download:
                //Toast.makeText(this, "下载", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Uri uri = data.getData();
                path = uri.getPath().toString();
                realPath = "/storage/emulated/0/" + path.substring(16);

                TextView textFileName = (TextView) findViewById(R.id.textView_filename);
                textFileName.setText(realPath);
                Log.d("MyPath", path);
                /*try
                {
                    url = FileUtils.get
                }*/

            }

        }

        /*super.onActivityResult(requestCode, resultCode, data);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TransmitCode();
                } else {
                    Toast.makeText(MainActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "hello");

        mUSBTransmit = new USBTransmit(this, new ConnectStateChanged());
        Button buttonBrowse = (Button) findViewById(R.id.button_browse);
        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "正在浏览文件",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择代码文件"), 0);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
                }
            }


        });

        Button buttonStart = (Button) findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                FileInputStream input = null;
*/
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    TransmitCode();
                }

            }
        });
    }

    private void TransmitCode() {
        byte[] buffer = new byte[512];

        try {
            File file = new File(realPath);
            if (!file.exists()) {
                throw new RuntimeException("the file is not exist");
            }
            FileInputStream input = new FileInputStream(file);

            int len = input.read(buffer);
            Log.d("MainActivity", Integer.toString(len));


            input.close();
            Toast.makeText(MainActivity.this, "file read successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Faild to read file.", Toast.LENGTH_SHORT).show();
        }

        int devIndex = 0;
        int DataNum = 5000;//单位为包
        int PacketSize = 512;//每次传输的数据字节数，该参数必须和单片机中的参数完全匹配，该参数不能大于或等于64K
        int DataNumIndex = DataNum;
        int ret;
        boolean State;
        byte[] WriteDataBuffer = new byte[PacketSize - 1];
        String str = new String(buffer, 0, 512);
        Log.d("MainAcativity", str);

        //扫描设备连接数
        int devNum = mUSBTransmit.usbDevice.USBScanDevice();
        if (devNum <= 0) {
            Toast.makeText(MainActivity.this, "无设备连接", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(MainActivity.this, "连接设备数为" + Integer.toString(devNum), Toast.LENGTH_SHORT).show();
        }
        //打开设备
        if (!mUSBTransmit.usbDevice.USBOpenDevice(devIndex)) {
            Toast.makeText(MainActivity.this, "打开设备失败", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(MainActivity.this, "打开设备成功", Toast.LENGTH_SHORT).show();
        }

        //System.arraycopy(buffer, 0, WriteDataBuffer, 0, 512);
        //Log.d("MainActivity", WriteDataBuffer.toString());
        State = mUSBTransmit.usbDevice.USBBulkWriteData(devIndex, mUSBTransmit.EP1_OUT, buffer, 512, 100);
        if (State) {
            Toast.makeText(MainActivity.this, "写数据成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "写数据失败", Toast.LENGTH_SHORT).show();
            return;
        }


        //receive
        do {
            ret = mUSBTransmit.usbDevice.USBBulkReadData(devIndex, mUSBTransmit.EP1_IN, buffer, PacketSize, 1000);
        } while (ret <= 0);
        if (buffer[0] == 'O' && buffer[1] == 'K') {
            buffer[0] = 0x0e;
            buffer[1] = 0x00;
            buffer[2] = 0x0f;
            mUSBTransmit.usbDevice.USBBulkWriteData(devIndex, mUSBTransmit.EP1_OUT, buffer, 3, 100);
            Toast.makeText(MainActivity.this, "传输结束", Toast.LENGTH_SHORT).show();
        }
    }
}

