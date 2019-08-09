package com.orient.padtemplate.widget.suspension;

/**
 * Author WangJie
 * Created on 2019/8/9.
 */
public class Item implements ISuspensionInterface {

    private String name;
    private String type;

    public Item(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
