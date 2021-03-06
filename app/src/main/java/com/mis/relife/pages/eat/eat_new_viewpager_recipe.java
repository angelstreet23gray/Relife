package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class eat_new_viewpager_recipe extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Context context;
    private TextView tv_eat;
    public static String eat;
    Button bt_new_recipe;
    Button bt_cancel;
    ListView lv_recipe;
    EditText ed_search;
    // ArrayList<String> array_recipe = new ArrayList<String>();
    SQLiteDatabase db;
    private List<eat_listview_recipe> mData; // 定義數據

    public eat_new_viewpager_recipe(Context context, String eat){
        this.context = context;
        this.eat = eat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eat_new_viewpager_recipe,container,false);
        db = context.openOrCreateDatabase("relife", 0, null);
        tv_eat = view.findViewById(R.id.tv_eat);
        tv_eat.setText(eat);
        bt_new_recipe = view.findViewById(R.id.bt_new_recipe);
        bt_new_recipe.setOnClickListener(this);
        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(this);
        lv_recipe = view.findViewById(R.id.listview_recipe);
        lv_recipe.setOnItemClickListener(this);
        ed_search = view.findViewById(R.id.ed_search);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<eat_listview_recipe> search_data = new ArrayList<eat_listview_recipe>();
                String word = ed_search.getText().toString();
                Cursor search = db.rawQuery("SELECT * FROM recipe WHERE name LIKE '%" + word + "%'", null);
                if (search.moveToFirst()) {
                    do {
                        eat_listview_recipe record = new eat_listview_recipe(search.getInt(1), search.getString(0), search.getDouble(2));
                        search_data.add(record);
                    } while (search.moveToNext());
                }
                LayoutInflater layoutinflater = getLayoutInflater();
                recipe_adapter adapter = new recipe_adapter(layoutinflater, search_data, context,(eat_new_activity) getActivity());
                lv_recipe.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_search = view.findViewById(R.id.ed_search);
        // 初始化數據
        initData();
        lv_recipe.setOnItemClickListener(this);
        return view;
    }


    // 初始化数据
    public void initData() {
        mData = new ArrayList<eat_listview_recipe>();
        Cursor c = db.rawQuery("SELECT * FROM recipe", null);
        if(c.moveToFirst()){
            do {
                eat_listview_recipe record  = new eat_listview_recipe(c.getInt(1),c.getString(0), c.getDouble(2));
                mData.add(record);
            } while (c.moveToNext());
        }

        //  將布局添加到ListView中
        LayoutInflater layoutinflater =getLayoutInflater();
        // 創建自定義Adapter的對象
        recipe_adapter adapter = new recipe_adapter(layoutinflater, mData, context,(eat_new_activity) getActivity());
        lv_recipe.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_new_recipe){
            Intent it = new Intent(context, eat_new_recipe.class);
            startActivityForResult(it, 0);
        }
        else if(v.getId() == R.id.bt_cancel){
            ed_search.setText("");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, "嗨 ! ", Toast.LENGTH_SHORT).show();
        //new AlertDialog.Builder(context).setTitle("牛角麵包378大卡(1個)，份數 : ").setIcon(R.drawable.pen).setView(new EditText(context)).setPositiveButton("確認", null).setNegativeButton("取消", null).show();
    }
}
