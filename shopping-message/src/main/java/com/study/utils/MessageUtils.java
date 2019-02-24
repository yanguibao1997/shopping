package com.study.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MessageUtils {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY ="973ede21e78b727c8d6c46793b689d4e";

    public static Logger logger= LoggerFactory.getLogger(MessageUtils.class);

    //1.屏蔽词检查测
    public static void getRequest1(){
        String result =null;
        String url ="http://v.juhe.cn/sms/black";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("word","");//需要检测的短信内容，需要UTF8 URLENCODE
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)

        try {
            result =net(url, params, "GET");
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.发送短信
    public static void senMessage(String phone,String code){
        String result =null;
        //请求接口地址
        String url ="http://v.juhe.cn/sms/send";
        //请求参数
        Map params = new HashMap();
        //接收短信的手机号码
        params.put("mobile",phone);
        //短信模板ID，请参考个人中心短信模板设置   136962
        params.put("tpl_id","136962");
        //变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，

//        生成6位数验证码
//        String code = CodeUtils.generateCode(6);

        params.put("tpl_value","#code#="+code);

        //应用APPKEY(应用详细页查询)
        params.put("key",APPKEY);
        //返回数据的格式,xml或json，默认json
        params.put("dtype","json");
        try {
            result =net(url, params, "GET");
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印请求结果
     * @param result
     */
    public static void getResult(String result){
        try {
            JSONObject object =JSONObject.parseObject(result);
//            JSONObject object = JSONObject.fromObject(result);
            int error_code = Integer.parseInt(object.get("error_code").toString());
            if(error_code==0){
//                System.out.println(object.get("result"));
                logger.info("成功{}",object.get("result"));
            }else{
//                System.out.println(object.get("error_code")+":"+object.get("reason"));
                logger.error("错误：{}===>错误代码{}",object.get("reason"),object.get("error_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        getRequest2("17315236520");
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }
}
