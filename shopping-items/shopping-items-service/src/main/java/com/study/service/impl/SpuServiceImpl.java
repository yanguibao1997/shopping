package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.bo.SpuBo;
import com.study.mapper.SkuMapper;
import com.study.mapper.SpuMapper;
import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.pojo.Category;
import com.study.pojo.Sku;
import com.study.pojo.Spu;
import com.study.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockService stockService;

    @Override
    public PageResult<SpuBo> querySpuByPage(Integer pageNo, Integer pageSize, String key, Boolean saleable) {
//        分页
        if(pageSize!=-1){
            PageHelper.startPage(pageNo,pageSize);
        }
//        查询
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        List<Spu> spus = spuMapper.selectByExample(example);

//        返回的是分类  和商品的名称    需要进行设置值
        List<SpuBo> spuBoList = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
//            根据品牌id   设置品牌名
            Brand brand = brandService.queryBrandByBid(spu.getBrandId());
            spuBo.setBname(brand.getName());

//            根据类目  设置分类 ----> 多个类目
            List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<String> cnames = new ArrayList<>();
            cids.forEach(cid -> {
//                获得名称
                Category category = categoryService.queryById(cid);
                cnames.add(category.getName());
            });
            spuBo.setCname(StringUtils.join(cnames, "/"));
            return spuBo;
        }).collect(Collectors.toList());

        PageInfo<SpuBo> spuBoPageInfo = new PageInfo<>(spuBoList);
        return new PageResult<>(new PageInfo<>(spus).getTotal(),spuBoPageInfo.getList());
    }

    @Override
    public void addSpu(Spu spu) {
//        设置为下架
        spu.setSaleable(false);
//        设置有效
        spu.setValid(true);
//        设置创建时间
        spu.setCreateTime(new Date());
//        设置更新时间
        spu.setLastUpdateTime(new Date());

//        增加数据
        spuMapper.insertSelective(spu);
    }

    @Override
    public void updateSpu(Spu spu) {
//        修改最新时间
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 上下架商品
     * @param spuId
     */
    @Override
    public void upOrDownSpuBySpuId(Long spuId,Boolean saleable) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        spu.setLastUpdateTime(new Date());
        spu.setSaleable(saleable);
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public void deleteSpuBySpuId(Long spuId) {
//        首先查出SKU   删除所有SKU和库存
        List<Sku> skus = skuService.querySkuBySpuId(spuId);
        skus.forEach( sku -> {
//            根据SKU 删除所有库存
            Long skuId = sku.getId();
            stockService.deleteStockBySkuId(skuId);
        });

//        删除SKU
        skuService.deleteSkuBySpuId(spuId);

//        修改SPU为不可用
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        spu.setLastUpdateTime(new Date());
        spu.setValid(false);
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public Spu querySpuBySpuId(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        return spu;
    }
}
