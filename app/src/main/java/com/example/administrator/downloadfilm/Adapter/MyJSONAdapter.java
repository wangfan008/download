package com.example.administrator.downloadfilm.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.downloadfilm.R;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/4/3.
 */
public class MyJSONAdapter extends BaseAdapter {

    private Context context;
    private  JSONArray jsonArray;
    private Handler handler;
    private Map<Integer,Integer> downloaded;
    private Map<Integer,Integer> total;
    private ViewHolder [] views;



    public MyJSONAdapter(Context context, JSONArray jsonArray) {
        views = new ViewHolder[jsonArray.length()];
        this.context = context;
        this.jsonArray = jsonArray;
        downloaded = new HashMap<Integer, Integer>();
        total = new HashMap<Integer, Integer>();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        total.put(msg.getData().getInt("id"),msg.getData().getInt("size"));
                        views[msg.getData().getInt("id")].fileSize.setText("0/"+msg.getData().getInt("size"));
                        System.out.println(msg.getData().getInt("id")+"---->"+msg.getData().getInt("size"));
                        break;
                    case 1:
                        downloaded.put(msg.getData().getInt("id"),msg.getData().getInt("size"));
                        views[msg.getData().getInt("id")].fileSize.setText(msg.getData().getInt("size")+"/"+total.get(msg.getData().getInt("id")));
                        System.out.println(msg.getData().getInt("size"));
                        break;
                    default:
                        System.out.println("defalt"); break;
                }
            }
        };
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int i) {

        try{
            return jsonArray.get(i);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view!=null)
             System.out.println("invate getView"+i);
        if(views[i]!=null){
            System.out.println("views[i]"+i+views[i].fileName.getText());
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.fileName = views[i].fileName;
            viewHolder.fileSize = views[i].fileSize;
            viewHolder.downLoad = views[i].downLoad;
            System.out.println("views[i]"+i+viewHolder.fileName.getText());
            return view;

        }
        ViewHolder viewHolder;

        view = LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        viewHolder = new ViewHolder((TextView) view.findViewById(R.id.fileName),
                (TextView) view.findViewById(R.id.fileSize), (Button) view.findViewById(R.id.downLoad));
        view.setTag(viewHolder);

        try {
            String fileName = ((JSONObject)jsonArray.get(i)).getString("fileName");
            final String url  = ((JSONObject)jsonArray.get(i)).getString("url");
            viewHolder.fileName.setText(fileName);
            viewHolder.downLoad.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new DownLoadThread(url,i)).start();
                }
            });



        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        views[i] = viewHolder;
        System.out.println("views[i]"+ views[i].hashCode()+ "------>"+viewHolder.hashCode());
        return view;
    }

    /**
     * 存储item中控件
     */
    private class  ViewHolder{
        TextView fileName;
        TextView fileSize;
        Button downLoad;

        private ViewHolder(TextView fileName, TextView fileSize, Button downLoad) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.downLoad = downLoad;
        }

        public  void copyOf(ViewHolder viewHolder){
            this.fileName = viewHolder.fileName;
            this.fileSize = viewHolder.fileSize;
            this.downLoad = viewHolder.downLoad;

        }
    }

    private void  updateView(){

    }


    /**
     * 下载文件线程
     */

    private class DownLoadThread implements  Runnable{
        private String path;
        private int id;

        public DownLoadThread(String url, int id) {
            this.path = url;
            this.id = id;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            ByteArrayOutputStream bos =null;
            InputStream inputStream =null;
            try
            {

                URL url = new URL(path);

                //setTitle(path);
                conn = (HttpURLConnection) url.openConnection();
                // conn.getResponseCode()
                conn.setReadTimeout(10* 1000);                                 //设置超时时间为10s
                //conn.connect();
                //int length = conn.getContentLength();

                int size = conn.getContentLength();
                if(size>0){
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",id);
                    bundle.putInt("size",size);
                    msg.setData(bundle);
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                inputStream = conn.getInputStream();

                bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];

                int len = 0;

                int downloaded=0;

                while ((len = inputStream.read(buffer)) != -1) {
                    downloaded+=len;
                    bos.write(buffer, 0, len);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",id);
                    bundle.putInt("size",downloaded);
                    msg.setData(bundle);
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

                byte data[] = bos.toByteArray();
              //  return data;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //return null;
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





}
