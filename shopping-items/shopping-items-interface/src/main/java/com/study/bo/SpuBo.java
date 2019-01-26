package com.study.bo;

import com.study.pojo.Spu;


public class SpuBo extends Spu{
//    分类名称  -----> 根据分类  类目进行查找
    private String cname;
//    品牌名称
    private String bname;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
