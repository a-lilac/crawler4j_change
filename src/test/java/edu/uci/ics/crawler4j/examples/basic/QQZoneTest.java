package edu.uci.ics.crawler4j.examples.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Lists;

/**
 * 参考网页：http://blog.csdn.net/zknxx/article/details/51598852
 * @author long
 *
 */
public class QQZoneTest {

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream is = null;
        String url = "http://user.qzone.qq.com/494819587";
       
        try {
            //创建Get请求
            HttpGet httpGet = new HttpGet(url);
            
            Collection<BasicHeader> defaultHeaders = new HashSet<BasicHeader>();
            BasicHeader cookie =
                    new BasicHeader(
                            "Cookie",
                            "hasShowWeiyun1098756763=1; pt_login_sig=B6Q0-*Eb5xXpk1aVSwrh2xT*OAKPDakAizbkWlXaTpGWP3sA7ibjb9geD0-HtD0R; zzpaneluin=; zzpanelkey=; pt_clientip=8ce67f0000010e3d; pt_serverip=ff9d0aab3d2cf8dd; qm_username=1098756763; qm_sid=b6785f01e459cf59acd68fc695a68555,qWWc4NTBQaFlueFBlZjlDbU1CVU1GS0c0LUJpTzhORUsxUGFzZmRnU2Mzb18.; o_cookie=985155884; ptui_loginuin=985155884; FTN5K=40a4843b; __Q_w_s__QZN_TodoMsgCnt=1; pgv_pvid=6446374325; pgv_info=ssid=s7324026912&pgvReferrer=; RK=P016CnaSQh; randomSeed=881312; qzmusicplayer=qzone_player_531770660_1465917463921; qqmusic_uin=; qqmusic_key=; qqmusic_fromtag=; rv2=806140DD2AFC4C1961B3E2038B82CAADF752E381EC3523CFEE; property20=7555E5D6B7335A79A74FCCC99147970BDE2AD6B0A276C3E14F4A8BE924C1173ED27AD7D7B48DC11A; pt2gguin=o1098756763; uin=o1098756763; skey=@dXigttGIt; ptisp=cnc; qzone_check=1098756763_1466000359; ptcz=4ce44659811c75e09508a7fad0b1d83a83a3378a32af0fc4feef485ba4499fa8; blabla=dynamic; Loading=Yes; qzspeedup=sdch; p_skey=ikgyzxw5SKP75n4xnaIZvjSvusZ9vOhxYtR56HX13r0_; p_uin=o1098756763; pt4_token=PKEe3agS68HP2VKYNL54Pu1uAuRLwlYNEZyHGIdNEEc_; qz_screen=1366x768; QZ_FE_WEBP_SUPPORT=1; cpu_performance_v8=17");
            BasicHeader accept = new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
            BasicHeader language = new BasicHeader("Accept-Language", "zh-cn");
            BasicHeader agent =
                    new BasicHeader("User-Agent",
                            "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
            BasicHeader charset = new BasicHeader("Accept-Charset", "UTF-8");
            BasicHeader alive = new BasicHeader("Keep-Alive", "300");
            BasicHeader connection = new BasicHeader("Connection", "Keep-Alive");
            BasicHeader cachecontrol = new BasicHeader("Cache-Control", "no-cache");
            defaultHeaders.add(cookie);
            defaultHeaders.add(accept);
            defaultHeaders.add(language);
            defaultHeaders.add(agent);
            defaultHeaders.add(charset);
            defaultHeaders.add(alive);
            defaultHeaders.add(connection);
            defaultHeaders.add(cachecontrol);
            
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
                String body = null;
                while((body=br.readLine()) != null){
                    System.out.println(body);
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
        
    }
}

