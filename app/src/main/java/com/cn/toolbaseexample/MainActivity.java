package com.cn.toolbaseexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.com.toolbase.bean.BottomMoldBean;
import cn.com.toolbase.view.BottomMoldView;

public class MainActivity extends AppCompatActivity {
    private Button mBtnListview1;
    private Button mBtnListview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();
    }

    private void initViews() {
        mBtnListview1 = (Button) findViewById(R.id.btn_listview1);
        mBtnListview2 = (Button) findViewById(R.id.btn_listview2);

    }

    private void initListener() {
        mBtnListview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomShow(0);
            }
        });
        mBtnListview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomShow(1);
            }
        });
    }

    //底部弹出框

    private void bottomShow(int flg) {
        List<BottomMoldBean> mList = new ArrayList<>();
        mList.add(new BottomMoldBean("测试一", ""));
        mList.add(new BottomMoldBean("测试二", ""));
        mList.add(new BottomMoldBean("测试三", ""));
        mList.add(new BottomMoldBean("测试四", ""));
        mList.add(new BottomMoldBean("测试五", ""));
        BottomMoldView moldView = new BottomMoldView(this);
        moldView.setTitle("选择");
        moldView.setList(mList);
        if (flg == 1) {
//            moldView.openItemBackground(true);
            moldView.openItemBackground(true, "#FFFFFF");
            //一下设置不设置时为默认
            moldView.isShowCancel(true);//显示底部关闭按钮
        }
        moldView.setListener(new BottomMoldView.Listner() {
            @Override
            public void onItemSelected(String name, Object vluse) {
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            }
        });
        moldView.show();
    }
}
