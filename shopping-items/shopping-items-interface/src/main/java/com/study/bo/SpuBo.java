package com.study.bo;

import com.study.pojo.Sku;
import com.study.pojo.Spu;
import com.study.pojo.SpuDetail;

import javax.persistence.Transient;
import java.util.List;


public class SpuBo extends Spu{
//    分类名称  -----> 根据分类  类目进行查找
    @Transient
    private String cname;
//    品牌名称
    @Transient
    private String bname;

//    商品详情
    @Transient
    private SpuDetail spuDetail;

//    sku列表
    @Transient
    private List<Sku> skus;

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

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
