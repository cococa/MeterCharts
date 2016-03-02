package com.c.metercharts;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;

import com.c.mcharts.MeterChart;

public class MainActivity extends Activity {


    private MeterChart mMeterChart;
    private Button setValue;
    private SeekBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setValue = (Button) findViewById(R.id.setValue);
        mMeterChart = (MeterChart) findViewById(R.id.meterChart);
        progressBar = (SeekBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double t = progress / 100.00;
                mMeterChart.setProgress(t);
                mMeterChart.setTopTextInt(123123);
                mMeterChart.playAnimation();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        setValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeterChart.setProgress(0.5);
                mMeterChart.setTopTextInt(123123);
                mMeterChart.playAnimation();
            }
        });


    }
}
