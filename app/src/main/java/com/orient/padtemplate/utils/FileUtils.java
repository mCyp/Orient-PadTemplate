package com.orient.padtemplate.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.orient.padtemplate.base.app.App;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.model.TaskModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author WangJie
 * Created on 2019/8/5.
 */
public class FileUtils {

    /**
     * 获取TaskModel
     *
     * @return TaskModel
     */
    public static TaskModel copyTaskModel(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            String fileNames[] = context.getAssets().list("");
            for (String fileName : fileNames) {
                if (fileName.contains("task.json")) {
                    InputStream inputStream = assetManager.open(fileName);
                    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
                    Object object = new Gson().fromJson(reader, new TypeToken<TaskModel>() {
                    }.getType());
                    TaskModel card = (TaskModel)object;
                    reader.close();
                    return card;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取单元格
     *
     * @return List<Cell>
     */
    public static List<Cell> copyCell(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            String fileNames[] = context.getAssets().list("");
            for (String fileName : fileNames) {
                if (fileName.contains("cells.json")) {
                    InputStream inputStream = assetManager.open(fileName);
                    JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
                    Object object = new Gson().fromJson(reader, new TypeToken<List<Cell>>() {
                    }.getType());
                    List<Cell> data = (List<Cell>)object;
                    reader.close();
                    return data;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建调用系统相机需要的Uri
     * @param p 父目录
     */
    public static Uri getImageUri(String p) {
        Uri imageUri;
        File file = new File(p);
        if (!file.exists()) {
            file.mkdirs();
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
                imageUri = FileProvider.getUriForFile(App.getInstance(), "com.orient.padtemplate.fileprovider", image);
            }
            return imageUri;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
