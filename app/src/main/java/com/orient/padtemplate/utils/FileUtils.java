package com.orient.padtemplate.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.orient.padtemplate.core.data.model.TaskModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

}
