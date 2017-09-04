package com.mclans.plugin.supersync.entity;

import java.io.Serializable;

public class PackData implements Serializable{
    private String packname;
    private Object body;
    public PackData(String packname, Object body) {
        this.packname = packname;
        this.body = body;
    }
    public String getPackName() {
        return this.packname;
    }
    public Object getBody() {
        return this.body;
    }
}
