package com.cn.android.helper;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现简单，代码量少，官方提供稳定的依赖代码，感知组件（Acitivity，Fragment，Servcie等）生命周期，不存在内存泄露
 */
public class LiveDataHelper {
    private static LiveDataHelper mInstance;
    private static Map<String, MyMutableLiveData> mLiveDatas = new HashMap<>();

    private LiveDataHelper() {

    }

    public static LiveDataHelper get() {
        if (mInstance == null) {
            synchronized (LiveDataHelper.class) {
                if (mInstance == null) {
                    mInstance = new LiveDataHelper();
                }
            }
        }
        return mInstance;
    }
    public <T> MyMutableLiveData<T> with(String key ,Class<T> type){
        if (!mLiveDatas.containsKey(key)){
            mLiveDatas.put(key,new MyMutableLiveData());
        }
        return mLiveDatas.get(key);
    }
    public MyMutableLiveData<Object> with(String key){
        return with(key,Object.class);
    }

    public <T> void post(String key, T t){
        if(Looper.getMainLooper() == Looper.myLooper()){
            with(key).setValue(t);
        }else {
            with(key).postValue(t);
        }
    }

    public static class MyMutableLiveData<T> extends MutableLiveData {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void hook(@NonNull Observer<T> observer) throws Exception {
            //get wrapper's version
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(this);
            Class<?> classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be bull!");
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);
            //get livedata's version
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(this);
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion);
        }
    }

//    //注册订阅
//    LiveDataHelper.get()
//        .with("key_test", String.class)
//        .observe(this, new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String s) {
//        }
//    });

//    发送消息
//    LiveDataHelper.get().with("key_test").setValue(s);


}
