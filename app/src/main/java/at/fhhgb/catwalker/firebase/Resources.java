package at.fhhgb.catwalker.firebase;

import at.fhhgb.catwalker.data.LocalData;

/**
 * This class stores everything creates the instances of classes that need to be accessed from multiple views.
 * Contains:
 *  - dataHandler : manages Databasequeries
 *  - localData : stores the data and throws PropertyChangedEvents when it changes
 *  - auth: handles firebase authentication
 */
public class Resources {

    //PropertyChangedEvenets
    public static final String user_change = "user.change";
    public static final String user_add = "user.add";
    public static final String user_remove = "user.remove";

    public static final String university_add = "university.add";
    public static final String university_remove = "university.remove";
    public static final String university_change = "university.change";

    private static Authentication auth = new Authentication();
    private static LocalData localData = new LocalData();
    private static DataModel dataModel = new DataModel(localData);

    public static DataModel getDataModel() {
        return dataModel;
    }

    public static LocalData getLocalData() {
        return localData;
    }

    public static Authentication getAuth() {
        return auth;
    }

}
