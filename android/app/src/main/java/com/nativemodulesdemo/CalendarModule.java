package com.nativemodulesdemo;

import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;

public class CalendarModule extends ReactContextBaseJavaModule {
    CalendarModule(ReactApplicationContext context) {
        super(context);
    }

    // Callback with Error and Result
//    @ReactMethod
//    public void createCalendarEvent(String name, String location, Callback callback) {
//        Integer eventId = 1;
//        callback.invoke(null, eventId);
//    }


// Callback with promise
    @ReactMethod
    public void createCalendarEvent(String name, String location, Promise promise) {
        try {
            Integer eventId = 1;
            promise.resolve(eventId);
        }catch(Exception e){
            promise.reject("Create Event Error: ", e);
        }
    }

//    @ReactMethod(isBlockingSynchronousMethod = true)

    @Override
    public String getName() {
        return "CalendarModule";
    }
}
