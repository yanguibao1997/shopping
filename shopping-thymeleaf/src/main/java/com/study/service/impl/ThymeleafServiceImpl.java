package com.study.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.study.feignclient.*;
import com.study.pojo.*;
import com.study.service.ThymeleafService;
import com.study.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThymeleafServiceImpl implements ThymeleafService {

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private SpecGroupClient specGroupClient;

    @Autowired
    private SpecParamClient specParamClient;

    @Autowired
    private SpuDetailClient spuDetailClient;

    @Autowired
    private BrandClient brandClient;

    @Override
    public Map<String, Object> loadThymeleaf(Long spuId) {
        Map<String,Object> allMap=new HashMap<>();

//        获得spu
        Spu spu = spuClient.querySpuBySpuId(spuId);
        allMap.put("spuKey",spu);

        //        品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        allMap.put("brandKey",brand);

//        分类集合
        List<Category> categories = categoryClient.queryCategoryByids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        allMap.put("categoriesKey",categories);

//        skus
        List<Sku> skus = skuClient.querySkuBySpuId(spuId);
        allMap.put("skusKey",skus);

//        specParam
        List<SpecParam> specParams1 = specParamClient.querySpecParamByCidGid(spu.getCid3(), null, null);
        allMap.put("specParamsKey",specParams1);

//        获得组以及参数
        List<SpecGroup> specGroups = specGroupClient.querySpecGroupByCid(spu.getCid3());
//        将参数带上
        specGroups.forEach( specGroup -> {
//            查新组下的参数
            List<SpecParam> specParams = specParamClient.querySpecParamByCidGid(spu.getCid3(), specGroup.getId(), null);
            specGroup.setSpecParams(specParams);
        });
        allMap.put("specGroupsKey",specGroups);

//        specParam
        List<SpecParam> specParams = specParamClient.querySpecParamByCidGid(spu.getCid3(), null, null);
        Map<Long,String> sps=new HashMap<>();
        specParams.forEach(specParam -> {
            sps.put(specParam.getId(),specParam.getName());
        });
        allMap.put("spsKey",sps);

//        spuDetail
        SpuDetail spuDetail = spuDetailClient.querySpuDetailBySpuId(spuId);
        allMap.put("spuDetailKey",spuDetail);
        Map<Long, Object> genericSpecMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, Object.class);
        Map<String, Object> specialSpectMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<String, Object>>() {});
        allMap.put("genericSpecMapKey",genericSpecMap);
        allMap.put("specialSpectMapKey",specialSpectMap);
        return allMap;
    }
}
