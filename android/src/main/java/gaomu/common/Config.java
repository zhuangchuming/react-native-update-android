package gaomu.common;

import android.Manifest;

/**
 * Created by cxj on 17-3-22.
 */

public class Config {

    public final static String VERSION_KEY = "version";

    public final static String DOWN_KEY = "downUrl";

    public final static String DOWN_ID_KEY = "downId";

    public final static String DIRECTORYNAME = "DIRECTORYNAME";

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

}
