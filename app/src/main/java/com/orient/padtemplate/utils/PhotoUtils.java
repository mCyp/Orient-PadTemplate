package com.orient.padtemplate.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.orient.padtemplate.common.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 调用系统相机工具类  COPY BY GNY\
 * <p>
 * Created by LYC on 2017/2/21.
 */
public class PhotoUtils {

    /**
     * 得到某个路径下图片的数量
     *
     * @param path      路径
     * @param Extension 扩展名
     * @return 数量
     */
    public static int getAlbumCountByPath(String path, String Extension) {
        ArrayList<String> lstFile = new ArrayList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return 0;
        for (File f : files) {
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                    lstFile.add(f.getPath());
                }
            }
        }
        return lstFile.size();
    }

    /**
     * 得到照片的存储路径
     *
     * @param tableId 表id
     * @param cellId  单元格id
     * @return String
     */
    public static String getTableStoragePath(String tableId, String cellId) {
        return Environment.getExternalStorageDirectory() + Common.Constant.SECOND_PATH
                + File.separator
                + "table"
                + File.separator
                + tableId
                + File.separator
                + cellId
                + File.separator;
    }


    /**
     * 搜索目录，扩展名，是否进入子文件夹
     *
     * @param path      路径
     * @param Extension 扩展名
     * @return List<Bitmap>
     */
    public static List<Bitmap> getAlbumByPath(String path, String Extension) {
        List<Bitmap> bitmaps = new LinkedList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        // 发生OOM异常的简单处理
        InputStream inputStream = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;

        try {
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                        inputStream = new FileInputStream(f);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        if (bitmap != null) {
                            bitmaps.add(bitmap);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    options = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmaps;
    }

    public static Bitmap getSignByPath(String path) {
        Bitmap bitmap;
        File file = new File(path);
        if (!file.isFile())
            return null;
        bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null)
            return null;
        return bitmap;

    }

    // 得到图片的路径
    public static List<String> getPath(String path, String Extension) {
        List<String> paths = new LinkedList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        for (File f : files) {
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                    paths.add(f.getPath());
                }
            }
        }
        return paths;
    }

    // 删除图片
    public static boolean deleteFile(String path) {
        if(TextUtils.isEmpty(path))
            return true;
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
            return deleteFile.delete();
        }
        return true;
    }


    /**
     * 创建详细需要的地址
     */
    @SuppressLint("SimpleDateFormat")
    public static Uri getMediaUriFromFile(String path) {
        File mediaStorageDir = new File(path);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // 创建mediaFile
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(path, "IMG_" + timeStamp + Common.Constant.PHOTO_NAME_SUFFIX);
        return Uri.fromFile(mediaFile);
    }
}
