package com.example.hachimi.repository.model;

/**
 * 包名称： com.example.hachimi.repository.data
 * 类名称：SettingData
 * 类描述：设置RV具备的数据，包括每个item的名字和ID
 * 创建人：韦西波
 */
public class SettingData {
    private String settingName;
    private int settingID;

    public SettingData(String name, int id) {
        this.settingID = id;
        this.settingName = name;
    }

    public int getSettingID() {
        return settingID;
    }

    public String getSettingName() {
        return settingName;
    }
}
