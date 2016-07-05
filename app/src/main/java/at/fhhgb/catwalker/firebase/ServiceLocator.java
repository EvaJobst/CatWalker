package at.fhhgb.catwalker.firebase;

/**
 * Created by Lisa on 14.06.2016.
 */
public class ServiceLocator {
    //stores instances
    //private static HashMap<Class, Object> instances = new HashMap<Class, Object>();
    private static final String TAG = "ServiceLocator";

    //PropertyChangedEvenets
    public static final String user_change = "user.change";
    public static final String user_add = "user.add";
    public static final String user_remove = "user.remove";

    public static final String university_add = "university.add";
    public static final String university_remove = "university.remove";
    public static final String university_change = "university.change";


    private static DataModel dataModel = new DataModel();
    private static Authentication auth = new Authentication();

    public static DataModel getDataModel(){
        return dataModel;
    }
    public static Authentication getAuth() {return  auth;}


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
