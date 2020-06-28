package com.hcycom.sso.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: IPLocationUtil
 * @Description: ip归属地查询
 * @Author: zh
 * @Date: 2019/3/14 11:20
 */
public class IPLocationUtil {

    public String getAddresses(String content) {
        // 这里调用pconline的接口
        if (!StringUtils.isEmpty(content)) {
            if (isInnerIP(content)) {
                String addr = "局域网";
                return addr;
            } else {
                // 从http://whois.pconline.com.cn取得IP所在的省市区信息
                String urlStr = "https://whois.pconline.com.cn/ipJson.jsp?ip=" + content;
                String returnStr = this.getResult(urlStr, content);
                if (returnStr != null) {
                    JSONObject jsonObject = JSON.parseObject(returnStr);
                    String addr = (String) jsonObject.get("addr");
                    return addr;
                }
            }
        }
        return "";
    }


    /**
     * @param urlStr  请求的地址
     * @param content 请求的参数 格式为：name=xxx&pwd=xxx
     * @return
     */
    private String getResult(String urlStr, String content) {
        //1.使用默认的配置的httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        //2.使用get方法
        HttpGet httpGet = new HttpGet(urlStr + content);
        InputStream inputStream = null;
        CloseableHttpResponse response = null;

        try {
            //3.执行请求，获取响应
            response = client.execute(httpGet);
            //看请求是否成功，这儿打印的是http状态码
            System.out.println(response.getStatusLine().getStatusCode());

            //4.获取响应的实体内容，就是我们所要抓取得网页内容
            HttpEntity entity = response.getEntity();

            //使用inputStream
            if (entity != null) {
                inputStream = entity.getContent();
                //获取返回数据，服务器返回的是gbk格式
                String line = EntityUtils.toString(entity, "GBK").trim();
                //gbk转换utf8
                byte[] bytes = getUTF8BytesFromGBKString(line);
                String str = new String(bytes);
                //截取字符串
                Pattern pattern = Pattern.compile("(?<=IPCallBack\\()(.+?)(?=\\))");
                Matcher matcher = pattern.matcher(str);
                String result = "";
                while (matcher.find()) {
                    result = matcher.group();
                }
                return result;
            }

        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @param gbkStr 转换的gbk格式字符串
     * @return
     */
    public byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    /**
     * 判断ip是公网还是内网  start
     */
    private boolean isInnerIP(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    private long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }

    private boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
    /**判断ip是公网还是内网  end*/

}
