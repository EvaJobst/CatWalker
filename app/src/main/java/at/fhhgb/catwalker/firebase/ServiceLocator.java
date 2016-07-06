package at.fhhgb.catwalker.firebase;

/**
 * Stores static refernces to instances of the DataModel and the Authentication class so that all classes can use the same instances.
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

}
