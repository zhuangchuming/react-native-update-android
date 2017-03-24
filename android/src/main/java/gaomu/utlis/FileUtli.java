package gaomu.utlis;

import android.os.Environment;

import java.io.File;

/**
 * Created by cxj on 17-3-22.
 */

public class FileUtli {

    public static String getSDPath() {
        String sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory().toString();
        } else{
            return "";
        }
        return sdDir;
    }


    public static String getFolderName(String filePath) {
        if (filePath == null || filePath.trim().equals("")) {
            return "";
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    public static void deleteAllFiles(File file) {
        if(file == null) return;

        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteAllFiles(f);
            }
            file.delete();
        }
    }


}
