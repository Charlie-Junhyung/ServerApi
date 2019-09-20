package com.beaconyx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class Main {

    private static String encodedString(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formattedString(String key, String value) {
        return String.format("&%s=%s", key, value);
    }

    private static List<Item> parsingXml(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc;

        try {
            InputSource inputSource = new InputSource(new StringReader(xml));
            builder = factory.newDocumentBuilder();
            doc = builder.parse(inputSource);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expr = xPath.compile("//items/item");
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            System.out.println(new Timestamp(System.currentTimeMillis()) + " | Total Data size = " + nodeList.getLength());

            List<Item> items = new ArrayList<>(nodeList.getLength());
            for (int i= 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                Item item = new Item();
                item.setContentid(node.getElementsByTagName("contentid").item(0).getTextContent());
                item.setEventstartdate(node.getElementsByTagName("eventstartdate").item(0).getTextContent());
                item.setEventenddate(node.getElementsByTagName("eventenddate").item(0).getTextContent());
                if (node.getElementsByTagName("firstimage").item(0) == null) {
                    item.setFirstimage("http://www.beaconyx.co.kr/festivalapp/res/image/no_image");
                } else {
                    item.setFirstimage(node.getElementsByTagName("firstimage").item(0).getTextContent());
                }


                if (node.getElementsByTagName("mapx").item(0) == null) {
                    item.setMapx("0.0");
                    item.setMapy("0.0");
                } else {
                    item.setMapx(node.getElementsByTagName("mapx").item(0).getTextContent());
                    item.setMapy(node.getElementsByTagName("mapy").item(0).getTextContent());
                }

                item.setModifiedtime(node.getElementsByTagName("modifiedtime").item(0).getTextContent());
                item.setTitle(node.getElementsByTagName("title").item(0).getTextContent());

                if (node.getElementsByTagName("readcount").item(0) == null) {
                    item.setReadcount("0");
                } else {
                    item.setReadcount(node.getElementsByTagName("readcount").item(0).getTextContent());
                }

                items.add(item);
                //System.out.println(item.toString());
            }

            return items;
        } catch (ParserConfigurationException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } catch (XPathExpressionException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } catch (IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        return null;
    }

    private static List<Item> refiningDataSet(List<Item> list) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        System.out.println("yesterday = " + yesterday);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        ParsePosition position = new ParsePosition(0);

        Predicate<Item> itemPredicate = item -> {
            Date modifiedTime = formatter.parse(item.getModifiedtime(), position);
            return modifiedTime.after(yesterday);
        };

        Iterator<Item> iterator = list.iterator();
        while (iterator.hasNext()) {
            Item temp = iterator.next();
            if (temp.getModifiedtime() == null) {
                iterator.remove();
            } else {
                try {
                    Date modifiedTime = formatter.parse(temp.getModifiedtime());
                    //System.out.println("modifiedTime = " + modifiedTime);
                    if (modifiedTime.before(yesterday))
                        iterator.remove();
                } catch(ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    private static String buildRestApiUrl() {
        // URL
        String urlBuilder = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival";

        //공공데이터포털에서 발급받은 인증키
        String a1 = String.format("?%s=%s", encodedString("ServiceKey"), "QpY%2FRYx6iXEAjwQwB67dMVp%2B3QOZCTS%2B20PpVJraGcO3qgzQAzBAJRRKP62f%2F2Eq4nF5b4LXUY0ImR4iM7t0Rg%3D%3D");

        // 한 페이지 결과 수
        String a2 = formattedString(encodedString("numOfRows"), encodedString("2000"));

        // 현재 페이지 번호
        String a3 = formattedString(encodedString("pageNo"), encodedString("1"));

        // IOS (아이폰), AND (안드로이드), WIN (원도우폰),ETC
        String a4 = formattedString(encodedString("MobileOS"), encodedString("ETC"));

        // 서비스명=어플명
        String a5 = formattedString(encodedString("MobileApp"), encodedString("어디로"));

        // (A=제목순, B=조회순, C=수정순, D=생성일순)
        String a6 = formattedString(encodedString("arrange"), encodedString("C"));

        // 목록 구분(Y=목록, N=개수)
        String a7 = formattedString(encodedString("listYN"), encodedString("Y"));

        // 행사 시작일(형식:YYYYMMDD)
        String a8 = formattedString(encodedString("eventStartDate"), encodedString("20000101"));

        return urlBuilder.concat(a1).concat(a2)
                .concat(a3).concat(a4).concat(a5)
                .concat(a6).concat(a7).concat(a8);
    }

    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp + " | Start to connecting API...");

        String responseXml = ApiManager.getApiData(buildRestApiUrl());
        timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp + " | Successfully retrieved API data");

        List<Item> itemList = parsingXml(responseXml);
        timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp + " | Successfully parsed response");

        /*if (itemList != null) {
            List<Item> refinedList = refiningDataSet(itemList);
            refinedList.forEach(System.out::println);
            System.out.println(refinedList.size() + " items updated at yesterday");
        }*/

        ParseManager parseManager = new ParseManager();
        if (itemList == null || itemList.size() == 0) {
            throw new RuntimeException("API returns OK code but there is no data!");
        } else {
            parseManager.parseTask(itemList);
        }

    }
}
