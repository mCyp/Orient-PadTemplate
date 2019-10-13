package com.orient.padtemplate.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.orient.padtemplate.base.app.App;
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
     * @param tableId 表id
     * @param cellId  单元格id
     * @return String
     */
    public static String getTableStoragePath(String tableId, String cellId) {
        return App.getInstance().getExternalCacheDir()
                + File.separator
                + "table"
                + File.separator
                + tableId
                + File.separator
                + cellId
                + File.separator;
    }

    /**
     * 得到故障记录的存储路径
     * @param troubleId 故障记录的id
     * @return 路径
     */
    public static String getTroubleStoragePath(String troubleId) {
        String path = App.getInstance().getExternalCacheDir()
                + File.separator
                + "trouble"
                + File.separator
                + troubleId
                + File.separator;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
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

    /**
     * 搜索目录，扩展名，是否进入子文件夹
     */
    public static List<Bitmap> getAlbumByPath(String path, String Extension, int reqWidth, int reqHeight) {
        List<Bitmap> bitmaps = new LinkedList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        // 发生OOM异常的简单处理
        InputStream inputStream = null;

        try {
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        inputStream = new FileInputStream(f);
                        BitmapFactory.decodeStream(inputStream, null, options);
                        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                        options.inJustDecodeBounds = false;
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmaps;
    }

    /*
        计算压缩比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;


            // 计算inSampleSize值
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Uri getImageUri(String p) {
        Uri imageUri;
        File file = new File(p);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    file      /* directory */
            );
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(image);
            } else {
                imageUri = FileProvider.getUriForFile(App.getInstance(), "com.orient.operation.fileprovider", image);
            }
            return imageUri;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
