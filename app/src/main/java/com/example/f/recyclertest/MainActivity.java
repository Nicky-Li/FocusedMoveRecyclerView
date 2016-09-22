package com.example.f.recyclertest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int S = 1;
    public static final int Q = 1 << 1;
    public static final int A = 1 << 2;
    public static final int B = 1 << 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "&&&&&&" + "5 & S:" + (2 & S));
        Log.d("MainActivity", "&&&&&&" + "5 & S:" + (3 & S));
        Log.d("MainActivity", "&&&&&&" + "5 & S:" + (4 & S));
        Log.d("MainActivity", "&&&&&&" + "5 & S:" + (5 & S));
        Log.d("MainActivity", "&&&&&&" + "5 & S:" + (6 & S));
        Log.d("MainActivity", "&&&&&&" + "5 & Q:" + (1 & Q));
        Log.d("MainActivity", "&&&&&&" + "5 & Q:" + (2 & Q));
        Log.d("MainActivity", "&&&&&&" + "5 & Q:" + (3 & Q));
        Log.d("MainActivity", "&&&&&&" + "5 & Q:" + (4 & Q));
        Log.d("MainActivity", "&&&&&&" + "5 & Q:" + (5 & Q));
        Log.d("MainActivity", "&&&&&&" + "5 & Q:" + (6 & Q));

        final MyRecyclerView recyclerView = (MyRecyclerView) findViewById(R.id.rc_list);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setFocusDrawableId(R.mipmap.liebiao_focus);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            datas.add("data" + i);
        }

        MyAdapter adapter = new MyAdapter(this, datas);
        recyclerView.setAdapter(adapter);

    }

    class MyAdapter extends BaseRecyclerAdapter<String, MyHoder> {


        public MyAdapter(Context context, List<String> datas) {
            super(context, datas);
        }


        @Override
        protected MyHoder onCreateHolder(ViewGroup parent, int viewType) {
            return new MyHoder(LayoutInflater.from(MainActivity.this).inflate(R.layout.rc_item, parent, false));

        }

        @Override
        protected void onBindHolder(MyHoder holder, final int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, datas.get(position) + "", Toast.LENGTH_SHORT).show();
                }
            });

            holder.tvData.setText(datas.get(position) + "");
        }

    }


    class MyHoder extends RecyclerView.ViewHolder {

        TextView tvData;

        public MyHoder(View itemView) {
            super(itemView);
            tvData = (TextView) itemView.findViewById(R.id.tv_data);

            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = (int) (Math.random() * 200 + 100);
            itemView.setLayoutParams(layoutParams);
        }
    }
}
