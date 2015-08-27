package com.example.administrator.downloadfilm;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.administrator.downloadfilm.Adapter.MyJSONAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.listView);

    }
    private  void initData(){
        jsonArray = new JSONArray();

        for(int i=0; i<50; i++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("fileName", "智取威虎山"+i);
                jsonObject.put("url", "http://192.168.110.100:8081/wsview.rar");
            }catch (JSONException e){
                e.printStackTrace();
                continue;
            }
            jsonArray.put(jsonObject);
        }

    }
    private void init(){
        MyJSONAdapter myJSONAdapter= new MyJSONAdapter(this,jsonArray);
        listView.setAdapter(myJSONAdapter);
    }
}
