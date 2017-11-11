package cn.com.toolbase.bean;

import java.io.Serializable;

/**
 * Created by Mr.ye on 2017/11/11.
 */

public class BottomMoldBean implements Serializable {
    private String name;
    private Object value;

    public BottomMoldBean(String name, Object value) {
        this.name = name;
        this.value = value;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
