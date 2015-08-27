package com.example.administrator.downloadfilm;

import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/3/23.
 */
public  class HttpClient {

    public static byte[] getImageByURL(String path){
        HttpURLConnection conn = null;
        ByteArrayOutputStream bos =null;
        InputStream inputStream =null;
        try
        {

            URL url = new URL(path);

            HttpGet httpGet = new HttpGet(path);

            //setTitle(path);
            conn = (HttpURLConnection) url.openConnection();


           // conn.getResponseCode()
            conn.setRequestMethod("GET");                                 //获取方式为GET
            conn.setReadTimeout(10* 1000);                                 //设置超时时间为10s
            //conn.connect();
            //int length = conn.getContentLength();
            inputStream = conn.getInputStream();

            bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];

            int len = 0;

            while ((len = inputStream.read(buffer)) != -1) {

                bos.write(buffer, 0, len);

            }

            byte data[] = bos.toByteArray();
            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(bos!=null){
                    bos.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
                if(conn!=null){
                    conn.disconnect();
                }
            }catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }
}
