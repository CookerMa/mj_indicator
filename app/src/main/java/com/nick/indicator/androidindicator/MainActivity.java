package com.nick.indicator.androidindicator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nick.indicator.lib.PageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PageIndicator mIndicator ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIndicator = findViewById(R.id.indicator);

        List<String> mList = new ArrayList<>();
        for (int i = 0 ;i<10;i++){
            mList.add("T"+i);
        }

        mIndicator.setViewType(TextView.class);
        mIndicator.setTabWidth(50);
        mIndicator.setTitles(mList);
    }
}
