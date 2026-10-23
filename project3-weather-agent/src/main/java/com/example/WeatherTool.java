package com.example;

import dev.langchain4j.agent.tool.Tool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherTool {

    // 内部辅助方法：根据城市名获取经纬度
    private String[] getCoordinates(String cityName) {
        try {
            String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + cityName + "&count=1&language=zh";
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(new Request.Builder().url(url).build()).execute();
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                if (json.has("results")) {
                    JsonArray results = json.getAsJsonArray("results");
                    if (results.size() > 0) {
                        JsonObject location = results.get(0).getAsJsonObject();
                        return new String[]{location.get("latitude").getAsString(), location.get("longitude").getAsString()};
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Tool("查询指定城市的未来3天天气预报（最高温、最低温、降水概率）")
    public String getWeatherForecast(String cityName) {
        String[] coords = getCoordinates(cityName);
        if (coords == null) return "找不到该城市的位置信息。";

        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + coords[0] + "&longitude=" + coords[1] 
                       + "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum&timezone=Asia%2FShanghai&forecast_days=3";
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(new Request.Builder().url(url).build()).execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonObject daily = JsonParser.parseString(response.body().string()).getAsJsonObject().getAsJsonObject("daily");
                JsonArray dates = daily.getAsJsonArray("time");
                JsonArray maxTemps = daily.getAsJsonArray("temperature_2m_max");
                JsonArray minTemps = daily.getAsJsonArray("temperature_2m_min");
                JsonArray precips = daily.getAsJsonArray("precipitation_sum");

                StringBuilder sb = new StringBuilder(cityName + "未来3天天气预报：\n");
                for (int i = 0; i < 3; i++) {
                    sb.append(dates.get(i).getAsString())
                      .append("：最高温 ").append(maxTemps.get(i).getAsDouble()).append("°C，")
                      .append("最低温 ").append(minTemps.get(i).getAsDouble()).append("°C，")
                      .append("降水量 ").append(precips.get(i).getAsDouble()).append("mm。\n");
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "查询预报失败：" + e.getMessage();
        }
        return "无法获取预报数据。";
    }
}