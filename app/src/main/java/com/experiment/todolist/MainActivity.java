package com.experiment.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Weather;


import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static List<Data> dataList=new ArrayList<>();
    public static MyAdapter adapter;
    public static SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null)
            actionbar.hide();
        SQLiteDatabase db = Connector.getDatabase();

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dataList=LitePal.findAll(Data.class);

        adapter=new MyAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View v,int pos){
                Intent intent=new Intent(MainActivity.this,Edit.class);
                intent.putExtra("judge", pos);
                startActivity(intent);
            }
        });
        ItemTouchCallBack touchCallBack = new ItemTouchCallBack();
        touchCallBack.setOnItemTouchListener(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        requestWeather();

        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.floatbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Edit.class);
                intent.putExtra("change", -1);
                startActivity(intent);
            }
        });
        Button button1=findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit.becomeSorted();
                adapter.notifyDataSetChanged();
            }
        });

        sp=getSharedPreferences("SP_TEST", Context.MODE_PRIVATE);

    }
    public void requestWeather(){
        String url="https://free-api.heweather.com/s6/weather/now?key=86a3c4999f6346248511a308d60856cd&location=CN101200101";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Looper.prepare();
                            Toast.makeText(MainActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            }
        });
    }
    private void showWeatherInfo(Weather weather){
        String degree="今天天气："+weather.now.tmp+"℃"+"  "+weather.now.con_text;
        TextView degreeText=findViewById(R.id.degree);
        degreeText.setText(degree);
    }

}