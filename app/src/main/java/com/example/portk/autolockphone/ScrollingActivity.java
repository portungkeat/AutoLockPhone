package com.example.portk.autolockphone;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        ComponentName componentName = new ComponentName(this, AutoLockService.class);
        if (!dpm.isAdminActive(componentName)){
            Intent intent = new Intent();
                        // 指定动作名称
                        intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        // 指定给哪个组件授权
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                        startActivity(intent);
        }


        FloatingActionButton start= (FloatingActionButton) this.findViewById(R.id.start);
        FloatingActionButton stop= (FloatingActionButton) this.findViewById(R.id.stop);
        start.setOnClickListener(new clickListent());
        stop.setOnClickListener(new clickListent());
    }

    class clickListent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start:
                   Intent startIntent=new Intent(v.getContext(),AutoLockService.class);
                    startService(startIntent);
                    break;
                case R.id.stop:
                    Intent stopIntent=new Intent(v.getContext(),AutoLockService.class);
                    stopService(stopIntent);
                    break;
            }
        }
    }
}
