package me.songning.circleview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import me.songning.library.CircleView;

public class MainActivity extends AppCompatActivity {

    private AppCompatSeekBar mSeekBar;
    private CircleView mCircleViewHi;
    private CircleView mCircleView;
    private AppCompatButton mStartBtn;
    private AppCompatButton mToggleBtn;
    private AppCompatButton mShowOrhideBtn;
    private AppCompatButton mStopBtn;
    private Toolbar mToolbar;

    int mSpeed = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCircleView = (CircleView) findViewById(R.id.circle_view);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        mStartBtn = (AppCompatButton) findViewById(R.id.btn_start);
        mToggleBtn = (AppCompatButton) findViewById(R.id.btn_toggle_rotate);
        mShowOrhideBtn = (AppCompatButton) findViewById(R.id.btn_toggle_show_hide);
        mStopBtn = (AppCompatButton) findViewById(R.id.btn_stop);

        mCircleViewHi = (CircleView) findViewById(R.id.circle_view_hi);
        mCircleViewHi.startRotateAnimation();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleView.setRotateSpeed(mSpeed - progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleView.startRotateAnimation();
            }
        });

        mToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleView.toggleRotateOrientation();
            }
        });

        mShowOrhideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleView.toggleShow();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleView.stopRotateAnimation();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, InRecyclerViewActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
