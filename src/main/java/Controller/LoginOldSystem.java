package Controller;

import Entity.UserInfo;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author lxxxxxxy
 * @time 2019/07/02 01:27
 */
@Controller
public class LoginOldSystem {

//    public static CloseableHttpClient httpClient = null;
//    public StringBuilder sb = new StringBuilder();
//    public CookieStore cookieStore;
//    public static DefaultHttpClient httpClient = new DefaultHttpClient();

    @RequestMapping(value = "/getverifyCode")
    @ResponseBody
    public Map<String, Object> getverifyCode(HttpServletRequest request) throws IOException {
        CloseableHttpClient httpClient = null;
        StringBuilder sb = null;
        CookieStore cookieStore = null;
        String username = "";
        try {
            httpClient = (CloseableHttpClient) request.getSession().getAttribute("httpClient");
            sb = (StringBuilder) request.getSession().getAttribute("sb");
            cookieStore = (CookieStore) request.getSession().getAttribute("cookieStore");
            username = (String) request.getSession().getAttribute("username");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(send(HttpClients.createDefault(),"https://https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx"));
//        System.out.println(send("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx"));

        getCookies(cookieStore, sb);
        getVerifyCodeToFile(username, httpClient, cookieStore, sb);
        request.getSession().setAttribute("httpClient", httpClient);
        request.getSession().setAttribute("sb", sb);
        request.getSession().setAttribute("cookieStore", cookieStore);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", true);
        return modelMap;
    }

    private String send(String url, CloseableHttpClient httpClient, CookieStore cookieStore, StringBuilder sb) throws IOException {
        HttpGet httpGet = new HttpGet(url);
//        CookieStore cookieStore = httpClient.getCookieStore();
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Referer", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/_data/login_home.aspx");
        httpGet.setHeader("Origin", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        httpGet.setHeader("Host", "266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cookie", sb.toString());
        httpGet.setHeader("Pragma", "no-cache");
        List<Cookie> cookies1 = cookieStore.getCookies();
        for (int i = 0; i < cookies1.size(); i++) {
            //遍历Cookies
            System.out.println(cookies1.get(i));
            System.out.println("cookiename==" + cookies1.get(i).getName());
            System.out.println("cookieValue==" + cookies1.get(i).getValue());
        }
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = "";
        if (execute != null) {
            HttpEntity entity = execute.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
        }
        return result;
    }


    private String send1(String url, CloseableHttpClient httpClient, CookieStore cookieStore, StringBuilder sb) throws IOException {
        HttpGet httpGet = new HttpGet(url);
//        CookieStore cookieStore = httpClient.getCookieStore();
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Referer", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/SYS/Main_banner.aspx");
        httpGet.setHeader("Host", "266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Cookie", sb.toString());
        httpGet.setHeader("Pragma", "no-cache");
        List<Cookie> cookies1 = cookieStore.getCookies();
        for (int i = 0; i < cookies1.size(); i++) {
            //遍历Cookies
            System.out.println(cookies1.get(i));
            System.out.println("cookiename==" + cookies1.get(i).getName());
            System.out.println("cookieValue==" + cookies1.get(i).getValue());
        }
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = "";
        if (execute != null) {
            HttpEntity entity = execute.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
        }
        return result;
    }


    private String send2(String url, CloseableHttpClient httpClient, CookieStore cookieStore, StringBuilder sb) throws IOException {
        HttpPost httpGet = new HttpPost(url);
//        CookieStore cookieStore = httpClient.getCookieStore();
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Referer", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore.aspx");
        httpGet.setHeader("Host", "266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Origin", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Cookie", sb.toString());
        httpGet.setHeader("Pragma", "no-cache");


        Map<String, String> map = new HashMap<String, String>();
        map.put("sel_xn", "2018");
        map.put("SJ", "0");
        map.put("btn_search", "");
        map.put("SelXNXQ", "1");
        map.put("zfx_flag", "0");
        map.put("zxf", "0");

        List<Cookie> cookies1 = cookieStore.getCookies();
        for (int i = 0; i < cookies1.size(); i++) {
            //遍历Cookies
            System.out.println(cookies1.get(i));
            System.out.println("cookiename==" + cookies1.get(i).getName());
            System.out.println("cookieValue==" + cookies1.get(i).getValue());
        }

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpGet.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = "";
        if (execute != null) {
            HttpEntity entity = execute.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
        }
        return result;
    }

    void getVerifyCodeToFile(String username, CloseableHttpClient httpClient, CookieStore cookieStore, StringBuilder sb) throws IOException {
        HttpGet getVerifyCode = new HttpGet("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/sys/ValidateCode.aspx?t=" + Math.round(Math.random() * 899 + 100));
        getVerifyCode.setHeader("Accept-Encoding", "gzip, deflate");
        getVerifyCode.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        getVerifyCode.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        getVerifyCode.setHeader("Referer", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/_data/login_home.aspx");
        getVerifyCode.setHeader("Host", "266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        getVerifyCode.setHeader("Connection", "keep-alive");
        getVerifyCode.setHeader("Pragma", "no-cache");
        getVerifyCode.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        getVerifyCode.setHeader("Cookie", sb.toString());
        FileOutputStream fileOutputStream = null;
        HttpResponse response;
        try {
            response = httpClient.execute(getVerifyCode);//获取验证码
            getCookies(cookieStore, sb);

            //windows平台
//            fileOutputStream = new FileOutputStream(new File("D:\\IdeaProjects\\searchScoreByOldSystem\\src\\main\\webapp\\resources\\verifyCode\\" + username + ".jpeg"));
            //Linux平台
            fileOutputStream = new FileOutputStream(new File("/usr/local/tomcat/webapps/searchScoreByOldSystem/resources/verifyCode/" + username + ".jpeg"));
            response.getEntity().writeTo(fileOutputStream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public synchronized Map login(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        CloseableHttpClient httpClient = null;
        StringBuilder sb = null;
        CookieStore cookieStore = null;
        try {
            httpClient = (CloseableHttpClient) request.getSession().getAttribute("httpClient");
            sb = (StringBuilder) request.getSession().getAttribute("sb");
            cookieStore = (CookieStore) request.getSession().getAttribute("cookieStore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String userInfoJSON = request.getParameter("userInfo");
        String verifyCodeJSON = request.getParameter("verifyCode");
        JSONObject jsonObject = JSONObject.fromObject(userInfoJSON);
        UserInfo userInfo = (UserInfo) JSONObject.toBean(jsonObject, UserInfo.class);
        System.out.println(userInfo);
//        String Tmm = getMD5(userInfo.getUsername() + getMD5(userInfo.getPassword()).substring(0, 30).toUpperCase() + "10642").substring(0, 30).toUpperCase();
        String mmMD5 = getMD5(userInfo.getUsername() + getMD5(userInfo.getPassword()).substring(0, 30).toUpperCase() + "10642").substring(0, 30).toUpperCase();
        System.out.println("mmMD5 = " + mmMD5);
//        System.out.println(mmMD5);
//        String verifyCodeMD5 = getMD5(getMD5("1111").substring(0, 30).toUpperCase() + "10642").substring(0, 30).toUpperCase();
//        System.out.println(verifyCodeMD5);
        Map<String, String> map = new HashMap<String, String>();
        map.put("txt_mm_expression", "");
        map.put("txt_mm_length", "");
        map.put("txt_mm_userzh", "");
        map.put("Sel_Type", "STU");
        map.put("typeName", "");
        map.put("txt_pewerwedsdfsdff", "");
        map.put("txt_asmcdefsddsd", userInfo.getUsername());
        map.put("txt_psasas", "");
        map.put("txt_sdertfgsadscxcadsads", "");
        map.put("dsdsdsdsdxcxdfgfg", mmMD5);
        map.put("pcInfo", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36undefined5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36 SN:NULL");

        verifyCodeJSON = verifyCodeJSON.replaceAll("\"", "");

//        verifyCodeJSON = new Scanner(System.in).next();
//        System.out.println(verifyCodeJSON);
        HttpPost HttpPost = new HttpPost("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/_data/login_home.aspx");
        HttpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"); // 设置请求头消息User-Agent

        String verifyCodeMD5 = getMD5(getMD5(verifyCodeJSON.toUpperCase()).substring(0, 30).toUpperCase() + "10642").substring(0, 30).toUpperCase();
        System.out.println("verifyCodeMD5 = " + verifyCodeMD5);
        map.put("fgfggfdgtyuuyyuuckjg", verifyCodeMD5);
//        map.put("__VIEWSTATE", "dDwzNDIxOTUwNTE7dDw7bDxpPDA+O2k8MT47aTwyPjs+O2w8dDxwPGw8VGV4dDs+O2w86YeN5bqG5paH55CG5a2m6ZmiOz4+Ozs+O3Q8cDxsPFRleHQ7PjtsPFw8c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCJcPgpmdW5jdGlvbiBDaGtWYWx1ZSgpewogdmFyIHZVPSQoJ1VJRCcpLmlubmVySFRNTFw7CiB2VT12VS5zdWJzdHJpbmcoMCwxKSt2VS5zdWJzdHJpbmcoMiwzKVw7CiB2YXIgdmNGbGFnID0gIllFUyJcOyBpZiAoJCgndHh0X2FzbWNkZWZzZGRzZCcpLnZhbHVlPT0nJyl7CiBhbGVydCgn6aG75b2V5YWlJyt2VSsn77yBJylcOyQoJ3R4dF9hc21jZGVmc2Rkc2QnKS5mb2N1cygpXDtyZXR1cm4gZmFsc2VcOwp9CiBlbHNlIGlmICgkKCd0eHRfcGV3ZXJ3ZWRzZGZzZGZmJykudmFsdWU9PScnKXsKIGFsZXJ0KCfpobvlvZXlhaXlr4bnoIHvvIEnKVw7JCgndHh0X3Bld2Vyd2Vkc2Rmc2RmZicpLmZvY3VzKClcO3JldHVybiBmYWxzZVw7Cn0KIGVsc2UgaWYgKCQoJ3R4dF9zZGVydGZnc2Fkc2N4Y2Fkc2FkcycpLnZhbHVlPT0nJyAmJiB2Y0ZsYWcgPT0gIllFUyIpewogYWxlcnQoJ+mhu+W9leWFpemqjOivgeegge+8gScpXDskKCd0eHRfc2RlcnRmZ3NhZHNjeGNhZHNhZHMnKS5mb2N1cygpXDtyZXR1cm4gZmFsc2VcOwp9CiBlbHNlIHsgJCgnZGl2TG9nTm90ZScpLmlubmVySFRNTD0nXDxmb250IGNvbG9yPSJyZWQiXD7mraPlnKjpgJrov4fouqvku73pqozor4EuLi7or7fnqI3lgJkhXDwvZm9udFw+J1w7CiAgIGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCJ0eHRfcGV3ZXJ3ZWRzZGZzZGZmIikudmFsdWUgPSAnJ1w7CiBkb2N1bWVudC5nZXRFbGVtZW50QnlJZCgidHh0X3NkZXJ0ZmdzYWRzY3hjYWRzYWRzIikudmFsdWUgPSAnJ1w7IAogcmV0dXJuIHRydWVcO30KIH0KZnVuY3Rpb24gU2VsVHlwZShvYmopewogdmFyIHM9b2JqLm9wdGlvbnNbb2JqLnNlbGVjdGVkSW5kZXhdLmdldEF0dHJpYnV0ZSgndXNySUQnKVw7CiAkKCdVSUQnKS5pbm5lckhUTUw9c1w7CiBzZWxUeWVOYW1lKClcOwp9CmZ1bmN0aW9uIG9wZW5XaW5Mb2codGhlVVJMLHcsaCl7CnZhciBUZm9ybSxyZXRTdHJcOwpldmFsKCJUZm9ybT0nd2lkdGg9Iit3KyIsaGVpZ2h0PSIraCsiLHNjcm9sbGJhcnM9bm8scmVzaXphYmxlPW5vJyIpXDsKIGlmKHRoZVVSTC5pbmRleE9mKCdSZVNldF9QYXNzV29yZC5hc3B4JylcPi0xICYmICcxMDY0MichPScxMDQ4MicpIHsKIHBhcmVudC5kb01vYmlsZVJlc2V0KClcOyB9IGVsc2Ugewpwb3A9d2luZG93Lm9wZW4odGhlVVJMLCd3aW5LUFQnLFRmb3JtKVw7IC8vcG9wLm1vdmVUbygwLDc1KVw7CmV2YWwoIlRmb3JtPSdkaWFsb2dXaWR0aDoiK3crInB4XDtkaWFsb2dIZWlnaHQ6IitoKyJweFw7c3RhdHVzOm5vXDtzY3JvbGxiYXJzPW5vXDtoZWxwOm5vJyIpXDsKcG9wLm1vdmVUbygoc2NyZWVuLndpZHRoLXcpLzIsKHNjcmVlbi5oZWlnaHQtaCkvMilcO2lmKHR5cGVvZihyZXRTdHIpIT0ndW5kZWZpbmVkJykgYWxlcnQocmV0U3RyKVw7Cn0KfQpmdW5jdGlvbiBzaG93TGF5KGRpdklkKXsKdmFyIG9iakRpdiA9IGV2YWwoZGl2SWQpXDsKaWYgKG9iakRpdi5zdHlsZS5kaXNwbGF5PT0ibm9uZSIpCntvYmpEaXYuc3R5bGUuZGlzcGxheT0iIlw7fQplbHNle29iakRpdi5zdHlsZS5kaXNwbGF5PSJub25lIlw7fQp9CmZ1bmN0aW9uIHNlbFR5ZU5hbWUoKXsKICAkKCd0eXBlTmFtZScpLnZhbHVlPSROKCdTZWxfVHlwZScpWzBdLm9wdGlvbnNbJE4oJ1NlbF9UeXBlJylbMF0uc2VsZWN0ZWRJbmRleF0udGV4dFw7Cn0Kd2luZG93Lm9ubG9hZD1mdW5jdGlvbigpewoJdmFyIHNQQz1NU0lFP3dpbmRvdy5uYXZpZ2F0b3IudXNlckFnZW50K3dpbmRvdy5uYXZpZ2F0b3IuY3B1Q2xhc3Mrd2luZG93Lm5hdmlnYXRvci5hcHBNaW5vclZlcnNpb24rJyBTTjpOVUxMJzp3aW5kb3cubmF2aWdhdG9yLnVzZXJBZ2VudCt3aW5kb3cubmF2aWdhdG9yLm9zY3B1K3dpbmRvdy5uYXZpZ2F0b3IuYXBwVmVyc2lvbisnIFNOOk5VTEwnXDsKdHJ5eyQoJ3BjSW5mbycpLnZhbHVlPXNQQ1w7fWNhdGNoKGVycil7fQp0cnl7JCgndHh0X2FzbWNkZWZzZGRzZCcpLmZvY3VzKClcO31jYXRjaChlcnIpe30KdHJ5eyQoJ3R5cGVOYW1lJykudmFsdWU9JE4oJ1NlbF9UeXBlJylbMF0ub3B0aW9uc1skTignU2VsX1R5cGUnKVswXS5zZWxlY3RlZEluZGV4XS50ZXh0XDt9Y2F0Y2goZXJyKXt9Cn0KZnVuY3Rpb24gb3BlbldpbkRpYWxvZyh1cmwsc2NyLHcsaCkKewp2YXIgVGZvcm1cOwpldmFsKCJUZm9ybT0nZGlhbG9nV2lkdGg6Iit3KyJweFw7ZGlhbG9nSGVpZ2h0OiIraCsicHhcO3N0YXR1czoiK3NjcisiXDtzY3JvbGxiYXJzPW5vXDtoZWxwOm5vJyIpXDsKd2luZG93LnNob3dNb2RhbERpYWxvZyh1cmwsMSxUZm9ybSlcOwp9CmZ1bmN0aW9uIG9wZW5XaW4odGhlVVJMKXsKdmFyIFRmb3JtLHcsaFw7CnRyeXsKCXc9d2luZG93LnNjcmVlbi53aWR0aC0xMFw7Cn1jYXRjaChlKXt9CnRyeXsKaD13aW5kb3cuc2NyZWVuLmhlaWdodC0zMFw7Cn1jYXRjaChlKXt9CnRyeXtldmFsKCJUZm9ybT0nd2lkdGg9Iit3KyIsaGVpZ2h0PSIraCsiLHNjcm9sbGJhcnM9bm8sc3RhdHVzPW5vLHJlc2l6YWJsZT15ZXMnIilcOwpwb3A9cGFyZW50LndpbmRvdy5vcGVuKHRoZVVSTCwnJyxUZm9ybSlcOwpwb3AubW92ZVRvKDAsMClcOwpwYXJlbnQub3BlbmVyPW51bGxcOwpwYXJlbnQuY2xvc2UoKVw7fWNhdGNoKGUpe30KfQpmdW5jdGlvbiBjaGFuZ2VWYWxpZGF0ZUNvZGUoT2JqKXsKdmFyIGR0ID0gbmV3IERhdGUoKVw7Ck9iai5zcmM9Ii4uL3N5cy9WYWxpZGF0ZUNvZGUuYXNweD90PSIrZHQuZ2V0TWlsbGlzZWNvbmRzKClcOwp9CmZ1bmN0aW9uIGNoa3B3ZChvYmopIHsgIGlmKG9iai52YWx1ZSE9JycpICB7ICAgIHZhciBzPW1kNShkb2N1bWVudC5hbGwudHh0X2FzbWNkZWZzZGRzZC52YWx1ZSttZDUob2JqLnZhbHVlKS5zdWJzdHJpbmcoMCwzMCkudG9VcHBlckNhc2UoKSsnMTA2NDInKS5zdWJzdHJpbmcoMCwzMCkudG9VcHBlckNhc2UoKVw7ICAgZG9jdW1lbnQuYWxsLmRzZHNkc2RzZHhjeGRmZ2ZnLnZhbHVlPXNcO30gZWxzZSB7IGRvY3VtZW50LmFsbC5kc2RzZHNkc2R4Y3hkZmdmZy52YWx1ZT1vYmoudmFsdWVcO31jaGtMeHN0cihvYmoudmFsdWUpXDsgfSAgZnVuY3Rpb24gY2hreXptKG9iaikgeyAgaWYob2JqLnZhbHVlIT0nJykgeyAgIHZhciBzPW1kNShtZDUob2JqLnZhbHVlLnRvVXBwZXJDYXNlKCkpLnN1YnN0cmluZygwLDMwKS50b1VwcGVyQ2FzZSgpKycxMDY0MicpLnN1YnN0cmluZygwLDMwKS50b1VwcGVyQ2FzZSgpXDsgICBkb2N1bWVudC5hbGwuZmdmZ2dmZGd0eXV1eXl1dWNramcudmFsdWU9c1w7fSBlbHNlIHsgICAgZG9jdW1lbnQuYWxsLmZnZmdnZmRndHl1dXl5dXVja2pnLnZhbHVlPW9iai52YWx1ZS50b1VwcGVyQ2FzZSgpXDt9fSBmdW5jdGlvbiBjaGtMeHN0cihzdHIpIAogewogaWYgKHN0ciE9JycpIHsgdmFyIGFyciA9IHN0ci5zcGxpdCgnJylcOwpmb3IgKHZhciBpID0gMVw7IGkgXDwgYXJyLmxlbmd0aC0xXDsgaSsrKSB7CiAgIHZhciBmaXJzdEluZGV4ID0gYXJyW2ktMV0uY2hhckNvZGVBdCgpXDsKICAgdmFyIHNlY29uZEluZGV4ID0gYXJyW2ldLmNoYXJDb2RlQXQoKVw7CiAgIHZhciB0aGlyZEluZGV4ID0gYXJyW2krMV0uY2hhckNvZGVBdCgpXDsKICAgdGhpcmRJbmRleCAtIHNlY29uZEluZGV4ID09IDFcOwogICAgc2Vjb25kSW5kZXggLSBmaXJzdEluZGV4PT0xXDsKICAgaWYoKCh0aGlyZEluZGV4IC0gc2Vjb25kSW5kZXggPT0gMSkmJihzZWNvbmRJbmRleCAtIGZpcnN0SW5kZXg9PTEpICkgfHwgKHRoaXJkSW5kZXg9PXNlY29uZEluZGV4ICYmIHNlY29uZEluZGV4PT1maXJzdEluZGV4KSl7CiAgICAgIGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCd0eHRfbW1fbHhwZCcpLnZhbHVlPScxJ1w7IAogICB9CiB9CiB9Cn0KClw8L3NjcmlwdFw+Oz4+Ozs+O3Q8O2w8aTwxPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8bDxUZXh0Oz47bDxcPG9wdGlvbiB2YWx1ZT0nU1RVJyB1c3JJRD0n5a2m44CA5Y+3J1w+5a2m55SfXDwvb3B0aW9uXD4KXDxvcHRpb24gdmFsdWU9J1RFQScgdXNySUQ9J+W3peOAgOWPtydcPuaVmeW4iOaVmei+heS6uuWRmFw8L29wdGlvblw+Clw8b3B0aW9uIHZhbHVlPSdTWVMnIHVzcklEPSfluJDjgIDlj7cnXD7nrqHnkIbkurrlkZhcPC9vcHRpb25cPgpcPG9wdGlvbiB2YWx1ZT0nQURNJyB1c3JJRD0n5biQ44CA5Y+3J1w+6Zeo5oi357u05oqk5ZGYXDwvb3B0aW9uXD4KOz4+Ozs+Oz4+Oz4+Oz4+Oz7dc9t5CevxG3PI2R2IQQ9Xzh08nA==");


        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
//        System.out.println("请求参数：" + nvps.toString());


        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost.setHeader("Accept-Encoding", "deflate, sdch");
        HttpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        HttpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpPost.setHeader("Accept", "zh-CN,zh;q=0.9");
        HttpPost.setHeader("Cookie", sb.toString());
        HttpPost.setHeader("Referer", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/_data/login_home.aspx");
        HttpPost.setHeader("Origin", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        HttpPost.setHeader("Host", "266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        HttpPost.setHeader("Connection", "keep-alive");
        HttpPost.setHeader("Pragma", "no-cache");

        //执行请求操作,拿到结果，查询状态码
        CloseableHttpResponse res = httpClient.execute(HttpPost);
        String result = "";
        if (res != null) {
            HttpEntity entity = res.getEntity();  //获取网页内容
            result = EntityUtils.toString(entity, "UTF-8");
        }
        System.out.println("result:" + result);

//        System.out.println(res.getStatusLine().getStatusCode());
//        System.out.println(send("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/SYS/Main_banner.aspx"));
        String send = send1("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/frame/menu.aspx", httpClient, cookieStore, sb);
        if(send.contains("<span style='font-color:red;font-size:12px'")){
            modelMap.put("success", false);
            return modelMap;
        }
        System.out.println(send);
        String regex = "js_UserID='(.*?)'";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(send);
        String xh = "";
        while (m.find()) {
            xh = m.group(1);
        }
        //获取学号姓名
        String regex2 = "if \\(\\'\\[(.*?)\\]'!=obj.code";
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(send);
        String xh2 = "";
        while (m2.find()) {
            xh2 = m2.group(1);
        }

        String width = send2("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_rpt.aspx", httpClient, cookieStore, sb);
        System.out.println(width);
        String regex1 = "width=(.*?) BORDER";
        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(width);
        String w = "";
        while (m1.find()) {
            w = m1.group(1);
            break;
        }

//        HttpPost httpPost = new HttpPost("https://https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/SYS/Main_banner.aspx");

        //windows平台
        String realPath = request.getSession().getServletContext().getRealPath("");
        String searchScoreByOldSystem = realPath.substring(0, realPath.indexOf("searchScoreByOldSystem"));
//        String dir = searchScoreByOldSystem + "searchScoreByOldSystem/src/main/webapp/resources/score/";
        //Linux平台
        String dir = "/usr/local/tomcat/webapps/searchScoreByOldSystem/resources/score/";
        String filep = dir + userInfo.getUsername() + ".jpg";
        String filep1 = dir + xh2 + ".jpg";



        //2018年第一学期
//        HttpGet getVerifyCode = new HttpGet("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_Drawimg.aspx?x=1&h=2&w=" + w + "&xnxq=20180&xn=2018&xq=&rpt=0&rad=1&zfx=0&xh=" + xh);
        //2018年第二学期
        HttpGet getVerifyCode = new HttpGet("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_Drawimg.aspx?x=1&h=2&w=" + w + "&xnxq=20181&xn=2018&xq=&rpt=0&rad=1&zfx=0&xh=" + xh);
//        HttpGet getVerifyCode = new HttpGet("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_Drawimg.aspx?x=1&h=2&w=818&xnxq=20180&xn=2018&xq=0&rpt=0&rad=2&zfx=0&xh=" + send);


        getVerifyCode.setHeader("Accept-Encoding", "gzip, deflate");
        getVerifyCode.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        getVerifyCode.setHeader("Content-Type", "application/x-www-form-urlencoded");
        getVerifyCode.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        getVerifyCode.setHeader("Referer", "https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_rpt.aspx");
        getVerifyCode.setHeader("Host", "266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn");
        getVerifyCode.setHeader("Connection", "keep-alive");
        getVerifyCode.setHeader("Pragma", "no-cache");
        getVerifyCode.setHeader("Cookie", sb.toString());
        FileOutputStream fileOutputStream = null;
        HttpResponse response;
        try {
            response = client.execute(getVerifyCode);
            File file = new File(filep);
            if (file.exists()) {
                file.delete();
            }
            fileOutputStream = new FileOutputStream(file);
            response.getEntity().writeTo(fileOutputStream);
            modelMap.put("success", true);
            modelMap.put("userInfo", xh2);
        } catch (ClientProtocolException e) {
            modelMap.put("success", false);
            e.printStackTrace();
        } catch (IOException e) {
            modelMap.put("success", false);
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
        }
        modelMap.put("success", true);
        modelMap.put("userInfo", xh2);
        {
            FileOutputStream fileOutputStream1 = null;
            HttpResponse response1;
            try {
                response1 = client.execute(getVerifyCode);
                File file = new File(filep1);
                if (file.exists()) {
                    file.delete();
                }
                fileOutputStream1 = new FileOutputStream(file);
                response1.getEntity().writeTo(fileOutputStream1);
            } catch (ClientProtocolException e) {
                modelMap.put("success", false);
                e.printStackTrace();
            } catch (IOException e) {
                modelMap.put("success", false);
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//        }
            }
        }
        return modelMap;
    }


    public static String getMD5(String text) throws Exception {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text);
        System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
        return encodeStr;
    }

    public void getCookies(CookieStore cookieStore, StringBuilder sb) throws IOException {
//        CookieStore cookieStore = httpClient.getCookieStore();
        List<Cookie> cookies1 = cookieStore.getCookies();
        for (int i = 0; i < cookies1.size(); i++) {
            //遍历Cookies
//            System.out.println(cookies1.get(i));
//            System.out.println("cookiename==" + cookies1.get(i).getName());
//            System.out.println("cookieValue==" + cookies1.get(i).getValue());
        }
        List<Cookie> cookies = cookieStore.getCookies();
        for (int j = 0; j < cookies.size(); j++) {
            sb.append(cookies.get(j).getName() + "=" + cookies.get(j).getValue() + ";");
        }

//        System.out.print("成功后的Cookie---->" + sb.toString());
//        System.out.println();

    }


}