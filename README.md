# Version1
在7月1号晚上，我去教务系统查成绩的时候，突然想把以前停滞的老教务网查成绩的项目重新弄起来，以前停滞的原因是查成绩的那个图片我无法解析出来，现在我发现它只要保存下来，修改成jpg格式就能够显示，于是在2号下午考完之后，我于3号凌晨0点左右重新开始了这个项目，我梳理了一下大概流程：
![QQ截图20190703122910](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190703122910-3c9717d28d184963a1c501170b76e930.png)
进入网上办事大厅这个功能在2月实现新教务网查成绩的时候做过，详情可以看（[重庆文理学院教务系统成绩查询项目实现经过](http://www.lixingyu.cn:8090/archives/1560758933564)）
进入[老教务网](https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx)，只要能进入网上办事大厅就能进去
获取验证码和提交表单做完后感觉也还是挺简单的，但是还是花了几个小时的时间在这上面，首先是对密码验证码的加密
```java
String mmMD5 = getMD5("201758274039" + getMD5("LXY981203").substring(0, 30).toUpperCase() + "10642").substring(0, 30).toUpperCase();
String verifyCodeMD5 = getMD5(getMD5(code.toUpperCase()).substring(0, 30).toUpperCase() + "10642").substring(0, 30).toUpperCase();
```
第一行代码主要是对密码进行MD5的算法加密，第二行代码主要是对验证码进行MD5的算法加密，最后封装好的表单参数如下：
![QQ图片20190703125426](http://www.lixingyu.cn:8090/upload/2019/7/QQ图片20190703125426-83a375bf19bf4eb998173865145b8115.png)
验证码这块，我当时主要是想把验证码显示到浏览器上，然后用户输入后传回后台，后面发现这样做可能不行，因为第一次访问的时候是有一个验证码回显到浏览器，用户提交过来的时候，[老教务网](https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx)是要重新获取一遍数据的，验证码可能两次不一致，于是在v1版本我先把验证码下载到本地，用了两种方法来处理验证码：
1、我研究了一会Tess4j，打算使用它来自动识别验证码，大概花了40多分钟，发现它不能够识别教务网的那种有点扭曲的验证码，我就放弃了。
2、在本地输入，发送到后台。后面我登录的时候访问[成绩链接](http://218.194.177.14/jwmis/xscj/Stu_MyScore_Drawimg.aspx?type=fydrdjr&w=750&xnxq=20180&rpt=1&xh=201700000817)每次都会给我提示
```html
<span style='font-color:red;font-size:12px' >系统提示：您无权访问此页，可重新<a href='http://218.194.177.14/jwmis' target=_top>登录</a>再试！</font>
```
也就是说我登录失败了，我只好排查，后面发现在登录教务网的时候，如果登录失败，它会有一个提示，比如这种
![QQ截图20190703130453](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190703130453-920bb1048fb14d1c902cb1467a90be76.png)
我想这个应该是浏览器会回显，因为我在[登录api](http://218.194.177.14/jwmis/_data/login_home.aspx)上没有找到验证码错误五个字样，所以我使用
```java
System.out.println(send(httpClient,"http://218.194.177.14/jwmis/_data/login_home.aspx"));
```
这一行代码来输出教务网给我的提示，结果教务网给我返回了
```html
<div align="center"><span id="divLogNote"><font color="Red">验证码错误！<br>登录失败！</font></span></div>
```
我判断应该是获取验证码的时候没有加cookie上去，于是我在获取验证码时给它加了一个header，最后获取验证码的实现过程是这样的
```java
	HttpGet getVerifyCode = new HttpGet("http://218.194.177.14/jwmis/sys/ValidateCode.aspx?t=" + Math.round(Math.random() * 899 + 100));
        getVerifyCode.setHeader("Accept-Encoding", "gzip, deflate");
        getVerifyCode.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        getVerifyCode.setHeader("Content-Type", "application/x-www-form-urlencoded");
        getVerifyCode.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        getVerifyCode.setHeader("Referer", "http://218.194.177.14/jwmis/_data/login_home.aspx");
        getVerifyCode.setHeader("Host", "218.194.177.14");
        getVerifyCode.setHeader("Connection", "keep-alive");
        getVerifyCode.setHeader("Pragma", "no-cache");
        getVerifyCode.setHeader("Cookie", sb.toString());
        getVerifyCode.getParams().setParameter("http.protocol.allow-circular-redirects", true);
        FileOutputStream fileOutputStream = null;
        HttpResponse response;
        try {
            response = client.execute(getVerifyCode);//获取验证码
            fileOutputStream = new FileOutputStream(new File("./src/main/webapp/resources/verifyCode/" + username + ".jpeg"));
            response.getEntity().writeTo(fileOutputStream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
```
为了表示我是更换验证码，于是我加了一个t的可变参数上去。
验证码这一个是弄好了，但是我登录的时候还是出错
```html
<div align="center"><span id="divLogNote"><font color="Red">发生未知错误！<br>登录失败！</font></span></div>
```
这一行表示我验证码没问题，但是发送的数据还是有问题，于是我进行排查，发现我忘了输入学号这一个表单，我重新验证，最后
![psb](http://www.lixingyu.cn:8090/upload/2019/7/psb-09cbe36483414ed28b4e7999352c3e61.jpg)
成功把我成绩的数据获取到了。
最后一个难点就是把网页上的数据下载下来保存成jpg图片。
我首先尝试的就是在java基础中学习到的InputStream，OutputStream，数据源就是这个网页，输出的时候设置成图片。
在网上搜了很多种方法，比如
```java
	FileBody bin = new FileBody(new File(filep));

        StringBody userName = new StringBody("Scott", ContentType.create(
                "text/plain", Consts.UTF_8));
        StringBody password = new StringBody("123456", ContentType.create(
                "text/plain", Consts.UTF_8));
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                // 相当于<input type="file" name="file"/>
                .addPart("file", bin)

                // 相当于<input type="text" name="userName" value=userName>
                .addPart("userName", userName)
                .addPart("pass", password)
                .build();

        httpPost.setEntity(reqEntity);
        CloseableHttpResponse res1 = httpClient.execute(httpPost);
//        String result1 = EntityUtils.toString(res1.getEntity(),"windows-1252");
        HttpEntity entity = res1.getEntity();
        String result1 = EntityUtils.toString(entity);
        System.out.println(result1);
        if (entity != null) {
            // 打印响应长度
            System.out.println("Response content length: " + entity.getContentLength());
            // 打印响应内容
            System.out.println(EntityUtils.toString(entity, Charset.forName("UTF-8")));
        }

        // 销毁
        EntityUtils.consume(entity);
```
其中
```java
String result1 = EntityUtils.toString(res1.getEntity(),"windows-1252");
```
这一行代码，是在第一次输出时，默认的编码是UTF-8，我以为是编码的问题，所以在转换成字符串的时候，我想把编码改一下，结果保存了很多次，都不能保存成windows-1252编码，所以这条方法便失败了
其实后面也尝试了很多种方法，都是编码问题。
最后我突然想起了，验证码的Url的内容和成绩的Url的内容其实是一样的，验证码能保存成图片，于是我把保存验证码的代码拿过来，然后成功保存了成绩的图片。
![查看成绩](http://www.lixingyu.cn:8090/upload/2019/7/查看成绩-c03b6d77b9a84cc98c8d6b9896061c8f.JPG)
在这个时候，基本上整体的框架的各个部分都已经完成了。大概的时间线分布如下：
![QQ截图20190703132237](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190703132237-eea4474c39184694990f977837edc458.png)
# Version2
整体框架的各个部分完成了，就该是提升用户体验了。
首先是验证码的回显，把验证码显示到浏览器上。
在第一次请求的时候，它会把验证码回显到浏览器上。然后把表单提交过去的时候，它又会重新获取一次验证码，我觉得它两次验证码就不一样了就会登陆失败。因为我做完前面的步骤后，对POST请求、GET请求已经很熟悉了，于是我想到用Cookie的方式来请求验证码
![QQ截图20190706151755](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706151755-a79cf432a5f24f799ac37e5a0e05a92c.png)
最终的实现方式：
我使用了
```java
        request.getSession().setAttribute("httpClient", httpClient);
        request.getSession().setAttribute("sb", sb);
        request.getSession().setAttribute("cookieStore", cookieStore);
```
来实现Cookie在两个页面传输的问题，在获取验证码的时候，把Cookie传过去就可以了
```java
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
//            fileOutputStream = new FileOutputStream(new File("D:\\桌面\\searchScoreByOldSystem\\src\\main\\webapp\\resources\\verifyCode\\" + username + ".jpeg"));
            //Linux平台
            fileOutputStream = new FileOutputStream(new File("/software/verifyCode/" + username + ".jpeg"));
            response.getEntity().writeTo(fileOutputStream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
```
在最开始还在找百度的图像识别API和一些打码平台来实现自动化验证码，后面发现百度的识别率虽然比Tess4j高一点，但是不能够完全识别，打码平台我不会用他们提供的工具，客服也找不到（以此纪念我损失的1块钱ToT）
![QQ截图20190706153044](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706153044-604a3ce1e4c34060adeb33fb087f8358.png)
![QQ截图20190706153117](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706153117-3c02f3d0e39e4d3a9bcf2254a5767f4a.png)
后面验证码能够成功获取到了，但是我每次请求的时候都会失败，于是我又开始排错。
刚开始从页面传过来的验证码和自己输入的是一模一样的，但是使用页面来请求总会失败，使用
```java
 verifyCodeJSON = new Scanner(System.in).next();
 System.out.println(verifyCodeJSON);
```
来请求就不会失败，最终我发现
```java
String verifyCodeJSON = request.getParameter("verifyCode");
```
从这里传过来的验证码，会默认加上双引号("6YBD"),MD5加密的时候，就会把引号也加进去，而使用Scanner输入的不会加，所以这就是问题所在。
于是我使用
```java
verifyCodeJSON = verifyCodeJSON.replaceAll("\"", "");
```
来把从页面传过来的验证码的双引号去掉。
![QQ截图20190706152211](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706152211-f625c4803f0f46cd823e7bec862e5d36.png)
最后获取验证码的方法是这样写的
```java
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
```
其实到这里，才算是各个部分都完成了。
![QQ截图20190706153605](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706153605-a3881785d225420eacfa7d19c2fd29b7.png)
# Version3
最后一部分是整体的连接。
在经过研究发现，只有访问[网上办事大厅](https://authservercqwu.cqwu.edu.cn/authserver/login?service=https%3a%2f%2fclient.cqwu.edu.cn%2fapi%2fenwas%2fcas%2fgoCallback?state=aHR0cHM6Ly9laGFsbGNxd3UuY3F3dS5lZHUuY24=)才能够通过[老教务网](https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx)来查成绩，因为只有登录了这个Url，才能获得一个Cookie
![QQ截图20190706154836](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706154836-1641eed50d004efbb2544507748e7b88.png)
只有通过这个Cookie，才能访问[老教务网](https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/home.aspx)
于是我成功获取到Cookie，把它存入Seesion域后，成功把网上办事大厅和教务网连接。
![QQ图片20190706155235](http://www.lixingyu.cn:8090/upload/2019/7/QQ图片20190706155235-d56f8834c6e04611ad7bcc0ef11779ca.jpg)
最后做出来的效果![Video_20190706_042329_510](http://www.lixingyu.cn:8090/upload/2019/7/Video_20190706_042329_510-45043b0a75ea442f9445a9396ba9d673.gif)
# Version4
添加了网站favicon.ico
# Version5
新增：查询者的名字
# Bug修复

Bug1、每个学生的js_UserID都不一样，获取成绩时会出现500错误。<br>
解决办法：找到学生的js_UserID，在<a href="https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/frame/menu.aspx">https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/frame/menu.aspx</a>中，有一串代码是js_UserID，使用字符串切割把它提取出来。
![QQ截图20190706163547](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706163547-fde607570e4f4ba1b4dfdfb978c3b186.png)
使用的方法是
```java
String send = send1("https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/frame/menu.aspx", httpClient, cookieStore, sb);
        System.out.println(send);
        String regex = "js_UserID='(.*?)'";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(send);
        String xh = "";
        while (m.find()) {
            xh = m.group(1);
        }
```
先访问这个页面，然后通过正则表达式来提取。

Bug2、每个用户的<a href="https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_Drawimg.aspx?x=1&h=2&w=818&xnxq=20180&xn=2018&xq=0&rpt=0&rad=2&zfx=0&xh=201700000817">https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_Drawimg.aspx?x=1&h=2&w=818&xnxq=20180&xn=2018&xq=0&rpt=0&rad=2&zfx=0&xh=201700000817</a>参数中的w的值也不一样。<br>
解决办法：找w的取值，在<a href="https://266d7afcaeb2ff1b22398f0ca30d270c.cqwu.edu.cn/jwmis/xscj/Stu_MyScore_rpt.aspx"></a>页面中，有这样一行代码width=xxx。找到后，也通过正则表达式来切割。其实后面也有一个完整的请求成绩的Url，反正效果一样，就不用它给出的Url了。
![QQ截图20190706163630](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706163630-95514a9a155944ec8b6edbf3a915ec70.png)
![QQ截图20190706163655](http://www.lixingyu.cn:8090/upload/2019/7/QQ截图20190706163655-20b7ecbf81e44a2cbed1af6edc77e462.png)

Bug3、密码如果是纯数字，密码的MD5加密会出错。<br>
解决办法：修改MD5加密的代码
```java
public static String getMD5(String text) throws Exception {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text);
        System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
        return encodeStr;
    }
```
Bug4、用户名或密码错误依然显示登录成功或查询成功。<br>
解决办法：加验证。
登录成功解决办法：对Cookie判断是否有web_vpn_user_token的键。
```java
if(!sb.toString().contains("web_vpn_user_token")){
            modelMap.put("success", false);
            return modelMap;
        }
```
查询成功解决办法：判断查询出来的图片里的字符是否是<span style='font-color:red;font-size:12px'，也就是登录失败后教务网返回的字符串。
```java
if(send.contains("<span style='font-color:red;font-size:12px'")){
            modelMap.put("success", false);
            return modelMap;
        }
```
# 测试地址
[测试地址](http://www.lixingyu.cn/searchScoreByOldSystem)