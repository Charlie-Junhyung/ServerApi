package com.beaconyx.api;

import com.beaconyx.log.MyLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiManager {
    private static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/$lang/searchFestival";
    private static final String KOREAN = "KorService";
    private static final String CHINESE_S = "ChsService";
    private static final String CHINESE_T = "ChtService";
    private static final String ENGLISH = "EngService";
    private static final String FRENCH = "FreService";
    private static final String GERMAN = "GerService";
    private static final String JAPANESE = "JpnService";
    private static final String RUSSIAN = "RusService";
    private static final String SPANISH = "SpnService";

    private static final String API_KEY = "QpY%2FRYx6iXEAjwQwB67dMVp%2B3QOZCTS%2B20PpVJraGcO3qgzQAzBAJRRKP62f%2F2Eq4nF5b4LXUY0ImR4iM7t0Rg%3D%3D";

    private String encodingUTF(String toEncode) {
        try {
            return URLEncoder.encode(toEncode, "UTF-8");
        } catch (IOException e) {
            MyLogger.z(e.getMessage());
            return "";
        }
    }

    private String firstQuery(String key) {
        return String.format("?%s=%s", key, API_KEY);
    }

    private String andQuery(String key, String value) {
        return String.format("&%s=%s", key, value);
    }

    private String buildRestApiUrl() {
        // URL
        StringBuilder builder = new StringBuilder(BASE_URL.replace("$lang", RUSSIAN));

        builder.append(
                firstQuery(encodingUTF("ServiceKey"))); //공공데이터포털에서 발급받은 인증키
        builder.append(
                andQuery(encodingUTF("numOfRows"), encodingUTF("2000"))); // 한 페이지 결과 수
        builder.append(
                andQuery(encodingUTF("pageNo"), encodingUTF("1"))); // 현재 페이지 번호
        builder.append(
                andQuery(encodingUTF("MobileOS"), encodingUTF("ETC"))); // IOS, AND, WIN, ETC
        builder.append(
                andQuery(encodingUTF("MobileApp"), encodingUTF("어디로"))); // 서비스명
        builder.append(
                andQuery(encodingUTF("arrange"), encodingUTF("C"))); // (A=제목순, B=조회순, C=수정순, D=생성일순)
        builder.append(
                andQuery(encodingUTF("listYN"), encodingUTF("Y"))); // 목록 구분(Y=목록, N=개수)
        builder.append(
                andQuery(encodingUTF("eventStartDate"), encodingUTF("20000101"))); // 행사 시작일(형식:YYYYMMDD)
        return builder.toString();
    }

    public String getData() {
        try {
            URL url = new URL(buildRestApiUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            MyLogger.z("HTTP Response " + conn.getResponseCode());

            BufferedReader reader;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            else
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            conn.disconnect();

            return sb.toString();
        } catch (IOException e) {
            MyLogger.z(e.getMessage());
        }

        return null;
    }
}
