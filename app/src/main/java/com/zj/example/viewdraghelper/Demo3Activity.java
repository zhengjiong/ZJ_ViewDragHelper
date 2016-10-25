package com.zj.example.viewdraghelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zj on 2016/9/20.
 */
public class Demo3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vdh_layout);

        TextView tv2 = (TextView) findViewById(R.id.tv_2);

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Demo3Activity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
