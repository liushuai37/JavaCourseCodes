package io.github.kimmking.gateway.outbound.httpclient4;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyHttpClient {

//    public static void main(String[] args) throws IOException {
//        String url = "http://localhost:8801";
//        String body = "hello";
//        String charSet = "utf8";
//        MyHttpClient myHttpClient = new MyHttpClient();
//        Map<String,Object> headers = new HashMap<>();
//        headers.put("Content-Type","text/html;charset=utf-8");
//        String result = myHttpClient.doPost(url,body,charSet,headers);
//        System.out.println(result);
//    }

    public String doPost(String url, String requestBody, String charSet, Map<String, Object> header) throws IOException {
        MyAHttpClient myAHttpClient = new MyAHttpClient();
        return myAHttpClient.doPost(url,requestBody,charSet,header);
    }
}


class MyAHttpClient{

    private CloseableHttpClient closeableHttpClient;
    public MyAHttpClient(){
        super();
    }

    public CloseableHttpClient getHttpClient(){
        return HttpClients.custom().build();
    }

    public String doPost(String url, String requestBody, String charSet, Map<String, Object> header) throws IOException {
        closeableHttpClient=this.getHttpClient();
        HttpPost httpPost = this.getHttpPostRequest(url,requestBody,charSet,header);
        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), Charset.forName(charSet));
        closeableHttpClient.close();
        return responseString;
    }

    private HttpPost getHttpPostRequest(String url,String requestBody,String charSet,Map<String, Object> header){
        HttpPost httpPost = new HttpPost(url);
        if(header!=null && !header.isEmpty()){
            Set<Map.Entry<String, Object>> entrySet = header.entrySet();
            for (Map.Entry<String, Object> entry:entrySet) {
                httpPost.addHeader(entry.getKey(),String.valueOf(entry.getValue()));
            }
        }
        StringEntity stringEntity = new StringEntity(requestBody,Charset.forName(charSet));
        httpPost.setEntity(stringEntity);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        httpPost.setConfig(requestConfig);
        return httpPost;
    }
}
