package com.experiment.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.litepal.LitePal;


public class Edit extends AppCompatActivity {
    private  int pri0;
    private int yearSelected,monthSelected,daySelected,theday;
    static int judge;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm:ss");

    private List<Integer> day;
    private EditText title;
    private EditText content;
    private static Data data;
    private static Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null)
            actionbar.hide();
        Intent intent=getIntent();
        judge = intent.getIntExtra("judge", -1);
        TextView time = (TextView) findViewById(R.id.time);

        long timeNow = System.currentTimeMillis();
        date = new Date(timeNow);
        time.setText(format.format(date));
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        settleSpinner();
        if(judge !=-1){
            data=MainActivity.dataList.get(judge);
            title.setText(data.getTitle());
            content.setText(data.getTitle());
        }
    }
    public void onBackPressed () {
        if(judge!=-1){
            data.setYear(yearSelected);
            data.setMonth(monthSelected);
            data.setDay(daySelected);
            data.setTitle(title.getText().toString());
            data.setContent(content.getText().toString());
            data.setPriority(pri0);
            data.setDate(format.format(date));
            MainActivity.dataList.set(judge,data);
        }else{
            data = new Data(title.getText().toString(), content.getText().toString(), yearSelected,monthSelected,daySelected, pri0,format.format(date));
            MainActivity.dataList.add(data);
        }
        becomeSorted();
        data.save();
        MainActivity.adapter.notifyDataSetChanged();
        finish();
    }

    private void settleSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] mItems = {"很高", "高", "中", "摸鱼"};
        final DateData dateData = new DateData();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item/*到时候改改*/, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        //监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String pri = adapter.getItem(pos);
                int priority = 1;
                switch (pri){
                    case "很高":priority=4;break;
                    case "高":priority=3;break;
                    case "中":priority=2;break;
                    case "摸鱼":priority=1;break;
                    default:break;
                }
                pri0 = priority;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //先挖坑在这里
            }
        });
        Spinner yearspinner=(Spinner)findViewById(R.id.add_year_spinner);
        Spinner monthspinner=(Spinner)findViewById(R.id.add_month_spinner);
        List<Integer> year = new ArrayList<Integer>();//给列表分配空间
        for (int i = 2020; i <= 2050; i++) {//把年份的选项加入year列表中；
            year.add(i);
        }

        final ArrayAdapter<Integer> yearadapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, year);//把年份的列表添加到适配器中 还有他的下拉选项
        yearadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);//
        yearspinner.setAdapter(yearadapter);//把布局加入适配器 这边我们用默认布局
        //给year的Spinner对象注册监听器
        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override//
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected=yearadapter.getItem(position);
                dateData.setYear(yearSelected);//给年份处理的对象分配空间//用DateData对象来处理选中的年份
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //这是未选中的时候 这里我们不写

            }
        });
        // 声明 用来储存年份的list
        List<Integer> month = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
            month.add(i);
        }
        final ArrayAdapter<Integer> monthadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, month);
        monthadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        monthspinner.setAdapter(monthadapter);
        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected=monthadapter.getItem(position);
                theday = dateData.getMonth(position);
                day = new ArrayList<Integer>();
                for (int i = 1; i <= theday; i++) {
                    day.add(i);
                }
                final ArrayAdapter<Integer> daydapter = new ArrayAdapter<Integer>(Edit.this, android.R.layout.simple_spinner_item, day);
                daydapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                Spinner dayspinner=(Spinner)findViewById(R.id.add_day_spinner);
                dayspinner.setAdapter(daydapter);
                dayspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        daySelected=daydapter.getItem(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    public static void becomeSorted(){
        //item按照deadline排序
        Collections.sort(MainActivity.dataList, new Comparator<Data>() {
            public int compare(Data o1, Data o2) {
                //按照deadline进行升序排列
                if(o1.getYear()>o2.getYear())
                    return 1;
                else if(o1.getYear()<o2.getYear())
                    return -1;
                else if(o1.getMonth()>o2.getMonth())
                    return 1;
                else if(o1.getMonth()<o2.getMonth())
                    return -1;
                else if(o1.getDay()>o2.getDay())
                    return 1;
                else if(o1.getDay()<o2.getDay())
                    return -1;
                else return 0;
            }
        });
    }
}





