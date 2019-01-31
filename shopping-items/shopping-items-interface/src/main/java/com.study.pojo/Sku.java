package com.study.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    sku id
    private Long id;
//    spu id
    private Long spuId;
//    商品标题
    private String title;
//    商品的图片，多个图片以‘,’分割
    private String images;
//    销售价格，单位为分
    private Integer price;
//    特有规格属性在spu属性模板中的对应下标组合
    private String indexes;
//    sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
    private String ownSpec;
//    是否有效，0无效，1有效
    private Boolean enable;
//    添加时间
    private Date createTime;
//    最后修改时间
    private Date lastUpdateTime;

//    添加一个库存
    @Transient
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
