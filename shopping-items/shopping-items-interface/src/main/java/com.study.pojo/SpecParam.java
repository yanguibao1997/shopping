package com.study.pojo;

import javax.persistence.*;

@Table(name = "tb_spec_param")
public class SpecParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;

    @Column(name = "group_id")
    private Long groupId;
    private String name;

//    是否是数字类型参数，true或false
    @Column(name = "`numeric`")
    private boolean numeric;

//    数字类型参数的单位，非数字类型可以为空
    private String unit;

//    是否是sku通用属性，true或false
    @Column(name = "generic")
    private boolean generic;

//    是否用于搜索过滤，true或false
    @Column(name = "searching")
    private boolean searching;

//    数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0
    private String segments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }
}
