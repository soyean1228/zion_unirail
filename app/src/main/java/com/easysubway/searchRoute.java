package com.easysubway;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import kr.go.seoul.trafficsubway.Common.BaseActivity;

public class searchRoute extends BaseActivity {

    boolean is_theme_white = false;

    private Button final_search;
    private Button start_search;
    private TextView start_result;
    private TextView final_result;
    static  final int GET_STRING = 1;

    private TextView textmain;
    public static Context mContext;

    public searchRoute()
    {
        openAPIKey = "70575677706d696333327152507752";
        subwayLocationAPIKey = "70575677706d696333327152507752";

    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_route);
        start_search = findViewById(R.id.start_search);
        final_search = findViewById(R.id.start_search);
        start_result = findViewById(R.id.start_result);
        final_result = findViewById(R.id.final_result);
        RouteStation r= new RouteStation();

        start_search.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent(searchRoute.this,SearchActivity.class);
                        intent.putExtra("is_theme_white", is_theme_white);
                        startActivity(intent);
                    }
            }
        );

        final_search.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent(searchRoute.this,SearchActivity.class);
                        intent.putExtra("is_theme_white", is_theme_white);
                        startActivity(intent);
                    }
                }
        );

        mContext = this;
        textmain = findViewById(R.id.textmain);
        Button start_button = (Button)findViewById(R.id.start_search);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchRoute.this, SearchActivity.class);
                intent.putExtra("is_theme_white", is_theme_white);
                startActivityForResult(intent, GET_STRING);
            }
        });

        Button final_button = (Button)findViewById(R.id.final_search);
        final_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchRoute.this, SearchActivity.class);
                intent.putExtra("is_theme_white", is_theme_white);
                startActivityForResult(intent, GET_STRING);
            }
        });

        if(getIntent() != null && getIntent().getStringExtra("OpenAPIKey") != null)
            openAPIKey = getIntent().getStringExtra("OpenAPIKey");
        if(getIntent() != null && getIntent().getStringExtra("SubwayLocationAPIKey") != null)
            subwayLocationAPIKey = getIntent().getStringExtra("SubwayLocationAPIKey");
        initView();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GET_STRING){
            if(resultCode == RESULT_OK){
                textmain.setText(data.getStringExtra("INPUT_TEXT"));
            }
        }
    }

    private void initView()
    {
        btnBackSubway = (ImageView)findViewById(R.id.btn_back_subway);
        btnBackSubway.setOnClickListener(new View.OnClickListener()
                                         {

                                             public void onClick(View view)
                                             {
                                                 finish();
                                             }

                                             final searchRoute this$0;

                                             {
                                                 this.this$0 = searchRoute.this;
                                                 // super();
                                             }
                                         }
        );
        /*lineMapWebview = (WebView)findViewById(R.id.line_map_webview);
        lineMapWebview.setWebViewClient(new WebViewClient());
        lineMapWebview.getSettings().setJavaScriptEnabled(true);
        lineMapWebview.getSettings().setBuiltInZoomControls(true);
        lineMapWebview.getSettings().setSupportZoom(true);
        lineMapWebview.getSettings().setDisplayZoomControls(false);
        lineMapWebview.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebViewInterface = new WebViewInterface(this, lineMapWebview, openAPIKey, subwayLocationAPIKey);
        lineMapWebview.addJavascriptInterface(mWebViewInterface, "Android");
        lineMapWebview.loadUrl("file:///android_asset/mSeoul_Subway.html");*/
    }


    private String openAPIKey;
    private String subwayLocationAPIKey;
    private ImageView btnBackSubway;
    //private WebView lineMapWebview;
    //private WebViewInterface mWebViewInterface;
}

