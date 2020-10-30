package com.demo.appmonitor;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    private MyBinder binder = new MyBinder();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
    public class MyBinder extends Binder {
        public MyService getService(){  //创建获取service的方法
            return MyService.this; //放回当前的Service类
        }
    }
}
