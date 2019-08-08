package com.orient.padtemplate.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.orient.padtemplate.base.app.App;

import java.util.HashSet;
import java.util.Set;

/**
 * SharedPreferences的辅助工具类
 * <p>
 * Author WangJie
 * Created on 2019/8/5.
 */
public class AppPrefUtils {
    private static SharedPreferences sp = App.getInstance().getSharedPreferences("pad", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor;

    static {
        editor = sp.edit();
    }

    /*
       Boolean数据
    */
    public static void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /*
        默认 false
     */
    public static Boolean getBoolean(String key) {
        return sp.getBoolean(key, true);
    }

    /*
        String数据
     */
    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /*
        默认 ""
     */
    public static String getString(String key) {
        return sp.getString(key, "");
    }

    /*
        Int数据
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /*
        默认 0
     */
    public static int getInt(String key) {
        return sp.getInt(key, 0);
    }

    /*
        Long数据
     */
    public static void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /*
        默认 0
     */
    public static long getLong(String key) {
        return sp.getLong(key, 0);
    }

    /*
        Set数据
     */
    public static void putStringSet(String key, Set<String> set) {
        editor.putStringSet(key, set);
        editor.commit();
    }

    /*
        默认空set
     */
    public static Set<String> getStringSet(String key) {
        Set<String> set = new HashSet<>();
        return sp.getStringSet(key, set);
    }

    /*
        删除key数据
     */
    public static void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
}
