package com.study.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.study.api.SkuApi;
import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.pojo.Sku;
import com.study.pojo.SpecParam;
import com.study.pojo.SpuDetail;
import com.study.search.feignclient.*;
import com.study.search.pojo.Goods;
import com.study.search.pojo.SearchPage;
import com.study.search.repository.GoodsRepository;
import com.study.search.service.GoodsService;
import com.study.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private SpuDetailClient spuDetailClient;

    @Autowired
    private SpecParamClient specParamClient;

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 根据 spu 然后来补充Goods中的属性
     * @param spuBo
     * @return
     */
    @Override
    public Goods goodsService(SpuBo spuBo) {
        Goods goods=new Goods();

        BeanUtils.copyProperties(spuBo,goods);

//        需要 all  ====> 所有需要被搜索的信息，包含标题，分类，甚至品牌
        StringBuilder all=new StringBuilder();
        all.append(spuBo.getTitle());

//        需要查找分类放入all中
        List<String> strings = categoryClient.queryCategoryNameByIds(Arrays.asList(goods.getCid1(), goods.getCid2(), goods.getCid3()));
        String categoryNames = StringUtils.join(strings, " ");
        all.append(categoryNames);

//        需要把品牌放入all中
        Brand brand = brandClient.queryBrandById(goods.getBrandId());
        all.append(brand.getName());

        goods.setAll(all.toString());


//        需要 price  获得每个sku 然后将价格加入  是个集合
        List<Sku> skus = skuClient.querySkuBySpuId(goods.getId());
        List<Long> prices=new ArrayList<>();
        skus.forEach( sku -> {
            prices.add(Long.valueOf(sku.getPrice()));
        });
        goods.setPrice(prices);

//        需要 sku  =====> sku信息的json结构
        goods.setSkus(JsonUtils.serialize(skus));


//        需要 specs ====> 可搜索的规格参数，key是参数名，值是参数值

//        首先查出 通用规格参数 和  特有规格参数
        SpuDetail spuDetail = spuDetailClient.querySpuDetailBySpuId(goods.getId());
//        转对象
        Map<Long,String> genericSpecMap=JsonUtils.parseMap(spuDetail.getGenericSpec(),Long.class,String.class);
        Map<Long,List<String>> specialSpecMap=JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long,List<String>>>() {});
//        查询 所有可以搜索的参数规格参数
        List<SpecParam> specParams = specParamClient.querySpecParamByCidGid(goods.getCid3(), null, true);

        Map<String,Object> specs=new HashMap<>();
        for (SpecParam specParam : specParams) {
//            判断通用   特有
            if(specParam.getGeneric()){
//                通用
                String value = genericSpecMap.get(specParam.getId());
//                判断数字类型  添加数值区间
                if(specParam.getNumeric()){
                    value = chooseSegment(value, specParam);
                }
                specs.put(specParam.getName(),value);
            }else{
//                特有
                specs.put(specParam.getName(),specialSpecMap.get(specParam.getId()));
            }
        }
        goods.setSpecs(specs);

        return goods;
    }

    /**
     * 搜索Goods
     * @param searchPage
     * @return
     */
    @Override
    public PageResult<Goods> searchGoodsPage(SearchPage searchPage) {
        String key = searchPage.getKey();

//        创建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();

//        对结果进行筛选  拿出需要就可以  提高效率
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","price"},null));

//        查询  ----->  key  与  all(可分词   标题+分类+品牌)
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("all",key));

        String sortBy = searchPage.getSortBy();
        Boolean descending = searchPage.getDescending();
//        排序
        if(StringUtils.isNotBlank(searchPage.getSortBy())){
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending? SortOrder.DESC : SortOrder.ASC));
        }

//        进行分页  ---->elasticSearch默认从0开始
        Integer pageNo = searchPage.getPageNo();
        Integer pageSize = searchPage.getPageSize();

        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNo-1,pageSize));

        Page<Goods> search = goodsRepository.search(nativeSearchQueryBuilder.build());

//        解析结果
        long totalElements = search.getTotalElements();
        long totalPage;
        if(totalElements%searchPage.getPageSize()!=0){
            totalPage = (totalElements/searchPage.getPageSize())+1;
        }else{
            totalPage = totalElements/searchPage.getPageSize();
        }

        return new PageResult<Goods>(totalElements,totalPage,search.getContent());
    }


    /**
     * 计算数值特有属性的区间
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
