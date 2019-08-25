package Controller;

import Entity.UserInfo;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lxxxxxxy
 * @time 2019/07/02 00:15
 */
@Controller
public class LoginNewSystem {

    @RequestMapping(value = "/loginNew")
    @ResponseBody
    public Map<String, Object> loginNew(HttpServletRequest request) throws IOException {

        Map<String, Object> modelMap = new HashMap<String, Object>();
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient= HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        StringBuilder sb=new StringBuilder();
        //获取账号密码
        String userInfoJSON = request.getParameter("userInfo");
        JSONObject jsonObject = JSONObject.fromObject(userInfoJSON);
        UserInfo userInfo = (UserInfo) JSONObject.toBean(jsonObject, UserInfo.class);

        Map<String, String> map = new HashMap<String, String>();
        HttpPost HttpPost = new HttpPost("https://authservercqwu.cqwu.edu.cn/authserver/login?service=https%3a%2f%2fclient.cqwu.edu.cn%2fapi%2fenwas%2fcas%2fgoCallback?state=aHR0cHM6Ly9laGFsbGNxd3UuY3F3dS5lZHUuY24="); // 创建httpget实例
        HttpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0"); // 设置请求头消息User-Agent

        CloseableHttpResponse response1 = httpClient.execute(HttpPost);
        String result = "";
        if (response1 != null) {
            HttpEntity entity = response1.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
//            System.out.println("网页内容:" + result);
        }
        String ltRegex = "LT-(.*)-cas";
        Pattern ltP = Pattern.compile(ltRegex);
        Matcher ltM = ltP.matcher(result);
        String ltRes = "";
        if (ltM.find()) {
            ltRes = ltM.group(0);
        } else {
            System.out.println("NO MATCH");
        }
        map.put("dllt", "userNamePasswordLogin");
        map.put("execution", "e1s1");
        map.put("lt", ltRes);
        map.put("username", userInfo.getUsername());
        map.put("password", userInfo.getPassword());
        map.put("_eventId", "submit");
        map.put("rmShown", "1");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        System.out.println("请求参数：" + nvps.toString());

        HttpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        HttpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");


        //执行请求操作,拿到结果，查询状态码
        CloseableHttpResponse res = httpClient.execute(HttpPost);
//        --------------------登录成功--------------------------------



        send1(httpClient, "https://authservercqwu.cqwu.edu.cn/authserver/login?service=https%3a%2f%2fclient.cqwu.edu.cn%2fapi%2fenwas%2fcas%2fgoCallback?state=aHR0cHM6Ly9laGFsbGNxd3UuY3F3dS5lZHUuY24=",cookieStore,sb);
        String value = send1(httpClient, "https://ehallcqwu.cqwu.edu.cn/login?service=https://ehallcqwu.cqwu.edu.cn/new/index.html",cookieStore,sb);
//        send(httpClient, value);
        send(httpClient, "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx", cookieStore, sb);
        if(!sb.toString().contains("web_vpn_user_token")){
            modelMap.put("success", false);
            return modelMap;
        }
        request.getSession().setAttribute("httpClient", httpClient);
        request.getSession().setAttribute("sb", sb);
        request.getSession().setAttribute("cookieStore", cookieStore);
        String username = request.getParameter("id");
        request.getSession().setAttribute("username", username);
        modelMap.put("success", true);

        return modelMap;

    }


    private String send1(CloseableHttpClient httpClient, String url,CookieStore cookieStore,StringBuilder sb) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        getCookies(httpClient,cookieStore,sb);
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Referer", "https://authservercqwu.cqwu.edu.cn/authserver/login?service=https%3a%2f%2fclient.cqwu.edu.cn%2fapi%2fenwas%2fcas%2fgoCallback?state=aHR0cHM6Ly9laGFsbGNxd3UuY3F3dS5lZHUuY24=");
        httpGet.setHeader("Host", "authservercqwu.cqwu.edu.cn");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Origin", "https://authservercqwu.cqwu.edu.cn");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Cookie", sb.toString());
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Pragma", "no-cache");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String value = "";
        try {

            Header location = execute.getFirstHeader("Location");
            value = location.getValue();
        } catch (Exception e) {

        }
        return value;
    }

    private String send(CloseableHttpClient httpClient, String url,CookieStore cookieStore,StringBuilder sb) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        getCookies(httpClient,cookieStore,sb);
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Host", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Cookie", sb.toString());
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Pragma", "no-cache");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = "";
        if (execute != null) {
            HttpEntity entity = execute.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
        }
        return result;
    }


    public void getCookies(CloseableHttpClient httpClient,CookieStore cookieStore,StringBuilder sb) throws IOException {
        List<Cookie> cookies1 = cookieStore.getCookies();
        for (int i = 0; i < cookies1.size(); i++) {
//            遍历Cookies
            System.out.println(cookies1.get(i));
            System.out.println("cookiename==" + cookies1.get(i).getName());
            System.out.println("cookieValue==" + cookies1.get(i).getValue());
        }
        List<Cookie> cookies = cookieStore.getCookies();
        for (int j = 0; j < cookies.size(); j++) {
            sb.append(cookies.get(j).getName() + "=" + cookies.get(j).getValue() + ";");
        }

        System.out.print("成功后的Cookie---->" + sb.toString());

    }
}