package io.github.zhaomy6.lab9;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class MainActivity extends Activity implements View.OnClickListener {
//    private TextView tv = null;
    private EditText inputCity = null;
    private ListView forecastView = null;
    private RecyclerView recyclerView = null;

    private TextView searchCityNameView = null;
    private TextView updateTimeView = null;
    private TextView humidityView = null;
    private TextView currentTemperatureView = null;
    private TextView airQualityView = null;
    private TextView temperatureRangeView = null;
    private TextView windView = null;

    private View line1 = null;
    private View line2 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

//        this.tv = (TextView) findViewById(R.id.debug_tv);
        this.inputCity = (EditText) findViewById(R.id.input_city);

        Button searchButton = (Button) findViewById(R.id.search_btn);
        searchButton.setOnClickListener(this);

        this.forecastView = (ListView) findViewById(R.id.lv);

        this.recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);

        searchCityNameView = (TextView) findViewById(R.id.r_city_name);
        updateTimeView = (TextView) findViewById(R.id.r_update_time);
        humidityView = (TextView) findViewById(R.id.r_humidity);
        currentTemperatureView = (TextView) findViewById(R.id.r_cur_tem);
        airQualityView = (TextView) findViewById(R.id.r_air_quality);
        temperatureRangeView = (TextView) findViewById(R.id.r_min_max_tem);
        windView = (TextView) findViewById(R.id.r_wind);

        this.line1 = findViewById(R.id.line1);
        this.line2 = findViewById(R.id.line2);
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.INVISIBLE);
    }

    //  search weather
    @Override
    public void onClick(View v) {
        String cityName = this.inputCity.getText().toString();
        if (cityName.isEmpty()) {
            Toast.makeText(this, "请输入需要查询的城市名", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://wthrcdn.etouch.cn/WeatherApi?city=" + cityName;
//        String url = "http://apis.baidu.com/heweather/weather/free?city=" + cityName;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            new WebAccessAsyncTask().execute(url);
        } else {
            Toast.makeText(this, "当前无可用网络", Toast.LENGTH_SHORT).show();
        }
    }

    private XmlObject xmlParse(String xmlStr) {
        if (xmlStr == null || xmlStr.length() == 0) return null;
        switch (xmlStr) {
            case "503":
                return new XmlObject("Error 503: Service Unavailable");
            case "-1":
                return new XmlObject("Error: IO Exception Happened");
        }
        String errMsg = "";
        XmlObject info = new XmlObject();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlStr));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        errMsg += XmlParseTag(parser, info);
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
            if (errMsg.isEmpty())
                return info;
            else
                return new XmlObject(errMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return new XmlObject("XML parse exception");
        }
    }

    private String XmlParseTag(XmlPullParser parser, XmlObject info)
            throws IOException, XmlPullParserException {
        switch (parser.getName()) {
            case "error":
                return parser.nextText();
            case "city":
                info.searchCityName = parser.nextText();
                break;
            case "updatetime":
                info.updateTime = parser.nextText();
                break;
            case "wendu":
                info.currentTemperature = parser.nextText();
                break;
            case "shidu":
                info.humidity = parser.nextText();
                break;
            case "quality":
                info.airQuality = parser.nextText();
                break;
            case "fengxiang":
                info.wind[0] = parser.nextText();
                break;
            case "fengli":
                info.wind[1] = parser.nextText();
                break;
            case "zhishu":
                String[] aSuggest = new String[3];
                for (int i = 0; i < 3; ++i) {
                    parser.nextTag();
                    aSuggest[i] = parser.nextText();
                }
                info.suggestions.add(aSuggest);
                break;
            case "weather":
                String[] aDayWeather = new String[4];
                for (int i = 0; i < 3; ++i) {
                    parser.nextTag();
                    aDayWeather[i] = parser.nextText();
                }
                parser.nextTag();
                parser.nextTag();

                aDayWeather[3] = parser.nextText();
                info.forecast.add(aDayWeather);
                break;
        }
        return "";
    }

    //  -----------
    //  内部异步任务类
    class WebAccessAsyncTask extends AsyncTask<String, String, String> {
        //  download web page
        @Override
        protected String doInBackground(String ... urls) {
            try {
//                return request(urls[0]);
                String xmlContent = downloadURL(urls[0]);
                xmlParse(xmlContent);
                return xmlContent;
            } catch (Exception e) {
                e.printStackTrace();
                return "无法访问目标地址";
            }
        }

        //  receive web page
        //  在这里进行更新UI的操作
        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);

            XmlObject info = xmlParse(content);
            if (info == null) return;
            if (info.isError()) {
//                searchCityNameView.setText(info.getErrorMsg());
                Toast.makeText(MainActivity.this, info.getErrorMsg(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (forecastView != null) {
                MyAdapter mAdapter = new MyAdapter(MainActivity.this, info.suggestions);
                forecastView.setAdapter(mAdapter);

                RecyclerAdapter rAdapter = new RecyclerAdapter(MainActivity.this, info.forecast);
                recyclerView.setAdapter(rAdapter);
            }
            String tmp = "";
            searchCityNameView.setText(info.searchCityName);
            tmp = "更新时间：" + info.updateTime;
            updateTimeView.setText(tmp);
            tmp = "湿度：" + info.humidity;
            humidityView.setText(tmp);
            tmp = info.currentTemperature + getString(R.string.degree);
            currentTemperatureView.setText(tmp);
            tmp = "空气质量：" + info.airQuality;
            airQualityView.setText(tmp);
            String min = info.forecast.get(0)[1].split(" ")[1];
            String max = info.forecast.get(2)[1].split(" ")[1];
            temperatureRangeView.setText(min + "/" + max);
            windView.setText(info.wind[0] + " " + info.wind[1]);

            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String[] values) {
            super.onProgressUpdate(values);
        }

        private String downloadURL(String strUrl) {
            InputStream is = null;
            try {
                URL url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int errCode = connection.getResponseCode();
                Log.d("downloadURL", "" + errCode);
                switch (errCode) {
                    case 503:
                        return "503";
                }
                is = connection.getInputStream();
                String content = readIS(is);
                is.close();
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                return "-1";
            }
        }

        private String readIS(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String lineInfo;
            while ((lineInfo = reader.readLine()) != null) {
                builder.append(lineInfo);
            }
            return builder.toString();
        }
    }
}
