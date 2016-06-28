package edu.uci.ics.crawler4j.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ZoneUtil {
    
    public static String extractQQ(String url) {
        if (null == url) {
            return null;
        }
        url = url.trim();
        if(url.length() == 0) {
            return null;
        }
        
        char[] chars = url.toCharArray();
        String result = "";
        int length = chars.length;
        for (int i = 0; i < length; i++) {
            char curChar = chars[i];
            if (curChar >= 48 && curChar <= 57) {
                result += curChar;
            } else {
                if (result.length() >= 5 && result.length() <= 11) {
                    break;
                } else {
                    result = "";
                }
            }
        }
        result = result.trim();
        if(result.length() == 0) {
            return null;
        }
        if(result.length() < 5 || result.length() > 11) {
            return null;
        }
        if(result.startsWith("0")) {
            return null;
        }
        return result;
    }

    
    public static Set<String> buildExternalContents(String urlPrefix, String urlSuffix, Collection<BasicHeader> defaultHeaders) {
        Set<String> qqs = new HashSet<String>();
        int i = 1;
        int lastSize = 0;
        int curSize = 0;
        while(true) {
            String url = urlPrefix+i+urlSuffix;
            Set<String> friends = parseViewers(url, defaultHeaders);
            if(friends.size() == 0) {
                break;
            } else {
                qqs.addAll(friends);
            }
            i++;
            curSize = qqs.size();
            if(curSize == lastSize) {
                break;
            } else {
                lastSize = qqs.size();
            }
        }
        
        Set<String> result = new HashSet<String>();
        for(String str : qqs) {
            String pathOne = "http://ic2.s21.qzone.qq.com/cgi-bin/feeds/feeds_html_module?i_uin="+str+"&i_login_uin=1098756763";
            String pathTwo = "http://user.qzone.qq.com/"+str;
            result.add(pathOne);
            result.add(pathTwo);
        }
        return result;
    }
    
    public static Set<String> parseViewers(String url, Collection<BasicHeader> defaultHeaders) {
        Set<String> friends = new HashSet<String>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream is = null;
       
        try {
            //创建Get请求
            HttpGet httpGet = new HttpGet(url);
            // 设置header
            for (BasicHeader header : defaultHeaders) {
                httpGet.setHeader(header);
            }
            
            //执行Get请求，
            response = httpClient.execute(httpGet);
            //得到响应体
            HttpEntity entity = response.getEntity();
            if(entity != null){
                is = entity.getContent();
                //转换为字节输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
                StringBuffer buffer = new StringBuffer();
                String body = null;
                while((body=br.readLine()) != null){
                    buffer.append(body);
                }
                
                String friendsJson = buffer.toString();
                friendsJson = friendsJson.substring(10);
                friendsJson = friendsJson.substring(0, friendsJson.length() - 2);
                JSONObject obj = JSONObject.parseObject(friendsJson);
                // 判断返回码是否正确
                int code = obj.getIntValue("code");
                int subCode = obj.getIntValue("subcode");
                if(code != 0 || subCode != 0) {
                    return friends;
                }
                
                JSONObject jsonobjFriends = obj.getJSONObject("data");
                JSONArray friendsArray = jsonobjFriends.getJSONArray("items");
                int arraySize = friendsArray.size();
                for(int i = 0; i < arraySize; i++) {
                    JSONObject friend = friendsArray.getJSONObject(i);
                    int uin = friend.getIntValue("uin");
                    friends.add(String.valueOf(uin));
                }
                
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            //关闭输入流，释放资源
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //消耗实体内容
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭相应 丢弃http连接
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return friends;
    }
}
