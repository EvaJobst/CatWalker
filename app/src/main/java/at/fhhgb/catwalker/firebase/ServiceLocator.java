package at.fhhgb.catwalker.firebase;

import android.util.Log;

import java.io.Console;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.ServiceLoader;

/**
 * Created by Lisa on 14.06.2016.
 */
public class ServiceLocator {
    //stores instances
    //private static HashMap<Class, Object> instances = new HashMap<Class, Object>();
    private static final String TAG = "ServiceLocator";


    private static DataModel dataHandler = new DataModel();
    public static DataModel getDataHandler(){
        return dataHandler;
    }

/*
    //register parameterless class, only creates a new instance if there is none until now
    public static <T> boolean register(Class T)
    {
        if(!instances.containsKey(T)){
            try {
                instances.put(T, T.newInstance());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        return true;
    }

    //register class with parameters
    public static <T> boolean register(Class T, Object[] params)
    {
        if(!instances.containsKey(T)){
            Object[] classes = new Object[params.length];
            for (int i=0; i<params.length ;i++) {
                classes[i] = params[i].getClass();
            }
            try {
                Constructor<T> constructor = T.getConstructor((Class<?>[]) classes);

                T instance = constructor.newInstance(params);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static <T> T getInstance(Class T){
        if(instances.containsKey(T))
            return (T)instances.get(T);
        return null;
    }
    */
}
