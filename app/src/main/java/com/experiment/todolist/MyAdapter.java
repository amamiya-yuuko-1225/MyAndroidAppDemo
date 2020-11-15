package com.experiment.todolist;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import org.litepal.LitePal;
import java.util.Collections;


import static com.experiment.todolist.MainActivity.adapter;
import static com.experiment.todolist.MainActivity.dataList;
import static com.experiment.todolist.MainActivity.sp;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.ViewHolder> implements ItemTouchCallBack.OnItemTouchListener {

    public static OnRecyclerViewItemClickListener myClickItemListener;// 声明自定义的接口
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,content,deadline,time,priority;
        Switch switch_1;
        View dataView;
        private OnRecyclerViewItemClickListener mListener;// 声明自定义的接口

        public ViewHolder (View v,OnRecyclerViewItemClickListener mListener){
            super(v);
            dataView=v;
            title=(TextView) v.findViewById(R.id.title);
            content=(TextView) v.findViewById(R.id.content);
            deadline=(TextView) v.findViewById(R.id.deadline);
            time=(TextView) v.findViewById(R.id.time_main);
            priority=(TextView) v.findViewById(R.id.priority);
            switch_1=v.findViewById(R.id.switch_1);
            dataView.setOnClickListener(this);
            this.mListener = mListener;
        }
        @Override
        public void onClick(View v) {
            //getAdapterPosition()为Viewholder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
            mListener.onItemClick(v, getAdapterPosition());
        }
    }
    public MyAdapter(){ }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        ViewHolder viewHolder=new ViewHolder(v,myClickItemListener);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        final Data data=MainActivity.dataList.get(position);
        String pri="摸鱼";
        switch(data.getPriority()){
            case 4:pri="很高";break;
            case 3:pri="高";break;
            case 2:pri="中";break;
            case 1:pri="摸鱼";break;
            default:break;
        }
        viewHolder.title.setText(data.getTitle());
        viewHolder.content.setText(data.getContent());
        viewHolder.priority.setText(pri);
        viewHolder.deadline.setText(data.getYear()+"年"+data.getMonth()+"月"+data.getDay()+"日");
        viewHolder.time.setText(data.getDate());
        viewHolder.switch_1.setChecked(sp.getBoolean("isChecked"+ data.getId(), false));
        viewHolder.switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonview,boolean isChecked){
                if(isChecked){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isChecked"+data.getId(), true);
                    editor.commit();
                }
            }
        });

    }
    public int getItemCount(){
        return MainActivity.dataList.size();
    }
    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                //LitePal.delete(Data.class, dataList.get(i).getId());
                //LitePal.delete(Data.class, dataList.get(i+1).getId());
                Collections.swap(MainActivity.dataList, i, i + 1);
                //boolean a=dataList.get(i).save();
                //boolean b=dataList.get(i+1).save();
                //Log.e("mainactivity", "onMove: "+a+b );
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(MainActivity.dataList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    @Override
    public void onSwiped(int position) {
        LitePal.delete(Data.class, MainActivity.dataList.get(position).getId());
        MainActivity.dataList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        MyAdapter.myClickItemListener = listener;
    }
    /**
     * 自定义接口
     */
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int postion);
    }

}
