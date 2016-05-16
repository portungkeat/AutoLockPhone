package com.example.portk.autolockphone;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

/**
 * Created by portk on 2016/5/15.
 */
public class AutoLockService extends Service {


    @Override
    public void onCreate() {
       DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        PowerManager.WakeLock mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "mytag");
//        ComponentName componentName = new ComponentName(this, AutoLockService.class);


        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitysensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);//距离传感器
        Sensor gravitysensor=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);//重力传感器
        sensorManager.registerListener(new sensorListener(dpm),proximitysensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(new sensorListener(dpm),gravitysensor,SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

class sensorListener implements SensorEventListener{

    private boolean ishorizontal=false;//是否平放
    private boolean ispocket=false;

    private float type8=0;//距离传感器;

    private float type90=0;//重力传感器1;
    private float type91=0;//重力传感器2;
    private float type92=0;//重力传感器3;
    private DevicePolicyManager dpm;
    private PowerManager.WakeLock mWakeLock;
    public  sensorListener( DevicePolicyManager dpm){
       this.dpm=dpm;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()){
            case Sensor.TYPE_GRAVITY:
               // System.out.println("重力传感器:"+event.values[0]+"  "+event.values[1]+"   "+event.values[2]);
                type90=event.values[0];
                type91=event.values[1];
                type92=event.values[2];
                if(event.values[0]>-0.5&&event.values[0]<0.5&&event.values[1]>-0.5&&event.values[1]<0.5&&event.values[2]>9.0){
                    if(ishorizontal)
                        return;
                    ishorizontal=true;
                    System.out.println("现在属于平放");
                   // mWakeLock.release();
                    dpm.lockNow();
                }else{
                    if(!ishorizontal)
                        return;
                    ishorizontal=false;
                    System.out.println("现在不属于平放");
                  //  mWakeLock.acquire();
                }
                break;
            case Sensor.TYPE_PROXIMITY:
                //System.out.println("距离传感器:"+event.values[0]);
                type8=event.values[0];
                if(type8==0.0&&type90<5&&type91>-5&&type92<5){
                    if(ispocket)
                        return;
                    ispocket=true;
                    System.out.println("口袋模式");
                    dpm.lockNow();
                }else{
                    if(!ispocket)
                        return;
                    ispocket=false;
                    System.out.println("不是口袋模式");
                    //mWakeLock.acquire();
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
