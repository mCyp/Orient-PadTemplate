package com.orient.padtemplate.widget.dialog;

/**
 * Author WangJie
 * Created on 2019/8/10.
 */
public class SelectionItem {

    private String name;
    private boolean isSelect;

    public SelectionItem(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
