package com.camp.share.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by winstar on 2017/9/10.
 */
@Slf4j
public class HttpClient {

    public static void main(String[] args) throws IOException {
        Map map = post("https://meican.com/preorder/api/v2.1/orders/show", "uniqueId=4ffc2858c5b3&type=CORP_ORDER", "remember=2874ddd684eb29ba98300ef07e5404d07cffaca8-605578");
        JSONObject jsonObject = JSON.parseObject(map.get("lines").toString());
        System.out.println(jsonObject);
    }

    public static Map get(String url_get, String str_param_url, String cookie) throws IOException {
        // 设置编码格式为utf-8
        String charset = "utf-8";
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        //    String getURL = GET_URL + "?username="  + URLEncoder.encode("fat man", "utf-8");
        String getURL = url_get + "?" + str_param_url;
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();

        if (cookie != null) {
            //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
            log.info("set cookieVal: {}", cookie );
            connection.setRequestProperty("Cookie", cookie);
        }

        log.info("http get param body: {}", str_param_url);
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),charset));
        log.info("Contents of get request:");
        String lines = "";
        String line;
        while ((line = reader.readLine()) != null)  {
            lines += line;
        }
        log.info(" request get results: {}", lines);
        reader.close();
        // 断开连接
        connection.disconnect();

        Map response = Maps.newHashMap();
        response.put("lines", lines);
        log.info(" request post results: {}", JSON.toJSON(response));

        return response;
    }

    public static Map post(String url_post, String str_param_body, String cookies) throws IOException  {

        // 设置编码格式为utf-8
        String charset = "utf-8";
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url_post);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl
                .openConnection();
        // Output to the connection. Default is
        // false, set to true because post
        // method must write something to the
        // connection
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        if("" != cookies){
            connection.setRequestProperty("Cookie", cookies);
        }

        log.info("http post param body: {}", str_param_body);
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        // Post cannot use caches
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        // This method takes effects to
        // every instances of this class.
        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
        // connection.setFollowRedirects(true);

        // This methods only
        // takes effacts to this
        // instance.
        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
        connection.setInstanceFollowRedirects(false);
        // Set the content type to urlencoded,
        // because we will write
        // some URL-encoded content to the
        // connection. Settings above must be set before connect!
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
        //    String content = "userName=" + URLEncoder.encode("console", "utf-8");
        //    content = content + "&password=" + URLEncoder.encode("12345678", "utf-8");

        log.info("http param body: {}", str_param_body);
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
        out.writeBytes(str_param_body);

        out.flush();

        // 取得cookie，相当于记录了身份，供下次访问时使用
        if (cookies == null || cookies.length() == 0) {
            for (String cookie : connection.getHeaderFields().get("Set-Cookie")) {
                if (cookie.contains("remember")) {
                    // 此处有返回cookie
                }
            }
        }

        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),charset));
        String lines = "";
        String line;
        while ((line = reader.readLine()) != null)  {
            lines += line;
        }

        reader.close();
        connection.disconnect();

        Map response = Maps.newHashMap();
        response.put("lines", lines);
        response.put("cookies", connection.getHeaderFields());
        log.info(" request post results: {}", JSON.toJSON(response));

        return response;
    }

}
