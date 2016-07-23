package com.bupt.monitorpositionsys.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bupt.monitorpositionsys.R;
import com.bupt.monitorpositionsys.db.Path;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import swipemenulistview.SwipeMenu;
import swipemenulistview.SwipeMenuCreator;
import swipemenulistview.SwipeMenuItem;
import swipemenulistview.SwipeMenuListView;

/**
 * Created by Kerry on 15/12/4.
 */
public class RecordActivity extends BaseActivity{

    private List<Path> paths = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        initUI();
        paths = DataSupport.findAll(Path.class, false);
    }

    private void initUI() {
        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.lv_record);
        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView av, View v,int position, long id)
                    {
                        Intent intent = new Intent(RecordActivity.this,MapActivity.class);
                        intent.putExtra("pathID", paths.get(position).getId());
                        startActivity(intent);
                    }
                }
        );

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        context);
                // set item background
                deleteItem.setBackground(R.color.mati_red);
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.roadbook_img_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        new AsyncTask<Void, Void, Boolean>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();

                            }

                            @Override
                            protected Boolean doInBackground(Void... params) {
                                DataSupport.delete(Path.class,paths.get(position).getId());
                                return true;
                            }

                            @Override
                            protected void onPostExecute(Boolean isSuccess) {
                                if (isSuccess) {
                                    // delete
                                    paths.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }.execute();
                        break;
                }
            }
        });
    }

    /** dp转px*/
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_record, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.tv_name.setText(getItem(position));
            return convertView;
        }

        @Override
        public String getItem(int position) {
            return paths.get(position).getDate();
        }

        class ViewHolder {
            TextView tv_name;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
    }
}
