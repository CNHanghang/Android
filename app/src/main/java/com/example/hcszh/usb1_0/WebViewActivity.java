package com.example.hcszh.usb1_0;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends AppCompatActivity {

    private WebView webview;
    private EditText editTextHttp;
    private Button buttonGo;
    private  String webPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        editTextHttp = (EditText)findViewById(R.id.edittext_http);
        buttonGo = (Button)findViewById(R.id.button_go);
        buttonGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                webPath = "http://"+editTextHttp.getText().toString();
                webview.loadUrl(webPath);
            }
        });
        webview = (WebView)findViewById(R.id.web_view);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webview.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);");
            }
        });

        webview.getSettings().setLoadWithOverviewMode(true);

        webview.getSettings().setUseWideViewPort(true);//设置webview推荐使用的窗口
        webview.getSettings().setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webview.getSettings().setDisplayZoomControls(false);//隐藏webview缩放按钮
        webview.getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        webview.getSettings().setAllowFileAccess(true); // 允许访问文件
        webview.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮
        webview.getSettings().setSupportZoom(true); // 支持缩放
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 在这里处理html源码
        }
    }
}
