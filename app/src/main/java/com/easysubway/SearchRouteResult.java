package com.easysubway;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import kr.go.seoul.trafficsubway.Common.BaseActivity;

public class SearchRouteResult extends BaseActivity {
    private String openAPIKey = "70575677706d696333327152507752";
    private String subwayLocationAPIKey = "70575677706d696333327152507752";
    private String startStation = "";
    private String finalStation = "";
    private TextView result;
    public String startX = "126.83948388112836";
    public String startY = "37.558210971753226";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_route_result);
        result = (TextView)findViewById(R.id.result);

        if(this.getIntent() != null && this.getIntent().getStringExtra("OpenAPIKey") != null) {
            this.openAPIKey = this.getIntent().getStringExtra("OpenAPIKey");
        }

        if(this.getIntent() != null && this.getIntent().getStringExtra("SubwayLocationAPIKey") != null) {
            this.subwayLocationAPIKey = this.getIntent().getStringExtra("SubwayLocationAPIKey");
        }

        if(this.getIntent() != null && this.getIntent().getStringExtra("startStation") != null) {
            this.startStation = this.getIntent().getStringExtra("startStation");
        }

        if(this.getIntent() != null && this.getIntent().getStringExtra("finalStation") != null) {
            this.finalStation = this.getIntent().getStringExtra("finalStation");
        }

        openAddressAPI addressAPI = new openAddressAPI(); //역에 따른 주소를 넘겨주는 api
        try {
            String s = addressAPI.execute(startStation, finalStation).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        openAPI subwayApi = new openAPI(); //받은 주소로 경로를 탐색하는 api
        subwayApi.execute();

    }

    class openAddressAPI extends AsyncTask<String, Void, String> {
        URL startUrl = null;
        URL finalUrl = null;
        protected String doInBackground(String... strings) {
            this.executeClient(strings);
            return "";
        }

        void executeClient(String[] str) {

            StringBuffer buffer = new StringBuffer();

            try {
                startUrl = new URL("http://swopenapi.seoul.go.kr/api/subway/" + SearchRouteResult.this.openAPIKey + "/xml/stationInfo/1/999/" + str[0]);
                InputStream is= startUrl.openStream(); //url위치로 입력스트림 연결
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType= xpp.getEventType();
                int find = 0;
                int start = 0;
                while( eventType != XmlPullParser.END_DOCUMENT ){
                    if(find == 1){break;};
                    switch( eventType ){
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;
                        case XmlPullParser.START_TAG:
                            tag= xpp.getName();//테그 이름 얻어오기
                            if(tag.equals("row")) ;// 첫번째 검색결과
                            else if(start == 0 && tag.equals("fname")){
                                buffer.append("출발역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                start = 1;
                                break;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag= xpp.getName(); //테그 이름 얻어오기
                            if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                    }
                    eventType= xpp.next();
                }
                finalUrl = new URL("http://swopenapi.seoul.go.kr/api/subway/" + SearchRouteResult.this.openAPIKey + "/xml/stationInfo/1/999/" + str[1]);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public class openAPI extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            URL url;
            try {
                //url = new URL("http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway?ServiceKey=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D&startX=126.83948388112836&startY=37.558210971753226&endX=127.01460762172958&endY=37.57250");
                ////
                StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway");
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("startX","UTF-8") + "=" + URLEncoder.encode(startX, "UTF-8")); /*출발지X좌표*/
                urlBuilder.append("&" + URLEncoder.encode("startY","UTF-8") + "=" + URLEncoder.encode(startY, "UTF-8")); /*출발지Y좌표*/
                urlBuilder.append("&" + URLEncoder.encode("endX","UTF-8") + "=" + URLEncoder.encode("127.01460762172958", "UTF-8")); /*목적지X좌표*/
                urlBuilder.append("&" + URLEncoder.encode("endY","UTF-8") + "=" + URLEncoder.encode("37.57250", "UTF-8")); /*목적지Y좌표*/
                url = new URL(urlBuilder.toString());
                ////
                InputStream is = url.openStream(); //url위치로 입력스트림 연결
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType = xpp.getEventType();
                int find = 0;
                int start = 0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (find == 1) {
                        break;
                    }
                    ;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//테그 이름 얻어오기
                            if (tag.equals("pathList")) ;// 첫번째 검색결과
                            else if (start == 0 && tag.equals("fname")) {
                                buffer.append("출발역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                start = 1;
                                break;
                            } else if (start == 1 && tag.equals("fname")) {
                                buffer.append("환승역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                break;
                            } else if (tag.equals("tname")) {
                                buffer.append("도착역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                break;
                            } else if (tag.equals("time")) {
                                buffer.append("소요시간 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                find = 1;
                                break;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag = xpp.getName(); //테그 이름 얻어오기
                            if (tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                    }
                    eventType = xpp.next();
                }
                return buffer.toString();
            } catch (Exception e) {
                System.out.println("failed");
            }
            return "";
        }
    }
}
