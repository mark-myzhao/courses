package io.github.zhaomy6.lab9;

import java.util.LinkedList;

public class XmlObject {
    public String searchCityName;
    public String updateTime;
    public String humidity;
    public String currentTemperature;
    public String airQuality;
    public String[] wind = new String[2];  //  xx风，xx级
    public LinkedList<String[]> suggestions = new LinkedList<>();
    //  date, weather, min_tmp, max_tmp
    public LinkedList<String[]> forecast = new LinkedList<>();

    private String errMsg = null;

    public XmlObject() {}

    public XmlObject(String err) {
        this.errMsg = err;
    }

    public String getErrorMsg() {
        return this.errMsg;
    }

    public boolean isError() {
        return this.errMsg != null;
    }
}
