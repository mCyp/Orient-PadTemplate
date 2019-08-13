package com.orient.padtemplate.utils;

import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.TextView;

/**
 * 字符串的辅助工具类
 *
 * @Auther JieWang on 2017/11/14.
 */

@SuppressWarnings("WeakerAccess")
public class StringsUtils {

    /**
     * 测量textView 里面的字符串的长度
     *
     * @param textView TextView
     * @param text     输入的文本
     * @return 字符串的长度
     */
    public static float getTextWidth(TextView textView, String text) {
        TextPaint textPaint = textView.getPaint();
        return textPaint.measureText(text);
    }

    /**
     * 根据文本的大小得出高度，可能会涉及到行距，这里不是特别准确
     *
     * @param size 字体的大小
     * @return 高度
     */
    public static float getTextHeight(int size, int margin) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(size);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fTop = fontMetrics.top;
        float fBottom = fontMetrics.bottom;
        return fBottom - fTop + margin * 2;
    }


    /**
     * 返回一串文本总的高
     *
     * @return 高度
     */
    public static float getTotalHeight(int aveWid, int margin, int textSize, TextView textView, String text) {
        float cellWidth = StringsUtils.getTextWidth(textView, text);
        int lines = (int) (cellWidth / aveWid) + 1;
        float height = StringsUtils.getTextHeight(textSize, margin);
        return (lines) * height + 2 * margin;
    }

    /**
     * 得到一串缩略的字符
     *
     * @return 一串缩略的字符
     */
    public static String getSimpleString(int aveWid, TextView textView, String text) {
        // 20size的字体的宽度大约为21，得到一行最多可以放多少个字母
        int lineMaxCount = (int) (aveWid / getTextHeight(14,2));
        int lineMinCount = aveWid / 40;
        int width = (int) getTextWidth(textView, text);
        // 如果两行显示不了
        if (width < aveWid * 2)
            return text;
        // 我就显示一行多 最后加上...
        String str = text.substring(0, lineMaxCount);
        float strWidth = getTextWidth(textView, str);
        for (int i = lineMaxCount; i > lineMinCount; i--) {
            if (strWidth > aveWid + 11) {
                str = str.substring(0, i);
            } else {
                break;
            }
        }
        str = str + " ... ";
        return str;
    }


}
