package com.zj.example.viewdraghelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.example.viewdraghelper.view.GoTopLayout;

/**
 * Created by zj on 2016/9/20.
 */
public class Demo5ActivityGoTop extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo5_layout);

        ImageView imageView = (ImageView) findViewById(R.id.goTop);
        final GoTopLayout goTopLayout = (GoTopLayout) findViewById(R.id.go_top_layout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTopLayout.goTop();
            }
        });
    }

}
