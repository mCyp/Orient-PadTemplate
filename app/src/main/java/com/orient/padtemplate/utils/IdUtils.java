package com.orient.padtemplate.utils;

import java.util.UUID;

/**
 * id的辅助工具类
 *
 * Author WangJie
 * Created on 2019/8/12.
 */
public class IdUtils {

    public static String createId(){
        return UUID.randomUUID().toString();
    }
}
