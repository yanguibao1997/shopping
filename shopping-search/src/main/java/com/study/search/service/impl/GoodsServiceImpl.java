package com.study.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.bo.SpuBo;
import com.study.pojo.*;
import com.study.search.feignclient.*;
import com.study.search.pojo.Goods;
import com.study.search.pojo.SearchPage;
import com.study.search.pojo.SearchPageResult;
import com.study.search.repository.GoodsRepository;
import com.study.search.service.GoodsService;
import com.study.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
@Service
public class GoodsServiceImpl implements GoodsService{

//   聚合分类的名称
    public static final String AGGS_CATEGORY="category";
//    聚合品牌的名称
    public static final String AGGS_BRANDID="brandId";

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private SpuDetailClient spuDetailClient;

    @Autowired
    private SpecParamClient specParamClient;

    @Autowired
    private GoodsRepository goodsRepository;

    Logger logger= LoggerFactory.getLogger(GoodsServiceImpl.class);


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
    public SearchPageResult searchGoodsPage(SearchPage searchPage) {
        String key = searchPage.getKey();

//        创建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();

//        对结果进行筛选  拿出需要就可以  提高效率
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","price"},null));
/*

//        查询  ----->  key  与  all(可分词   标题+分类+品牌)
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("all",key));

*/

        QueryBuilder queryBuilder = this.creatBoolQuery(searchPage);
        nativeSearchQueryBuilder.withQuery(queryBuilder);

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


//        进行聚合  需要查询的分类   品牌
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(GoodsServiceImpl.AGGS_CATEGORY).field("cid3"));


        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(GoodsServiceImpl.AGGS_BRANDID).field("brandId"));


        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(nativeSearchQueryBuilder.build());


//        取出所有 品牌   分类
        Aggregations aggregations = search.getAggregations();

//        获得品牌的集合
        List<Brand> brands = this.searchAggsBrand(aggregations.get(GoodsServiceImpl.AGGS_BRANDID));

//        获得分类的集合
        List<Category> categories = this.searchAggsCategory(aggregations.get(GoodsServiceImpl.AGGS_CATEGORY));

//        解析结果
        long totalElements = search.getTotalElements();
        long totalPage;
        if(totalElements%searchPage.getPageSize()!=0){
            totalPage = (totalElements/searchPage.getPageSize())+1;
        }else{
            totalPage = totalElements/searchPage.getPageSize();
        }

//        存放
        List<Map<String,Object>> specs=null;
//        当只有一个分类的时候才会有参数
        if(categories.size()==1){
            specs=this.searchAggsSpecs(categories.get(0).getId(),queryBuilder);
        }


        return new SearchPageResult(totalElements,totalPage,search.getContent(),categories,brands,specs);
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


    /**
     * 获得聚合的品牌
     * @param aggregation
     * @return
     */
    private List<Brand> searchAggsBrand(Aggregation aggregation){
        List<Brand> brands=new ArrayList<>();

        LongTerms brandIdTerms= (LongTerms) aggregation;
        for (LongTerms.Bucket bucket : brandIdTerms.getBuckets()) {
            long brandId = bucket.getKeyAsNumber().longValue();
//            获得品牌
            brands.add(brandClient.queryBrandById(brandId));
        }
        return brands;
    }

    /**
     * 获得聚合的分类
     * @param aggregation
     * @return
     */
    private List<Category> searchAggsCategory(Aggregation aggregation){
        List<Category> categories=new ArrayList<>();
        LongTerms categoryTerms= (LongTerms) aggregation;

        List<Long> category_longs=new ArrayList<>();
        if(categoryTerms.getBuckets().size()==0){
            return null;
        }
        for (LongTerms.Bucket bucket : categoryTerms.getBuckets()) {
            long categoryId = bucket.getKeyAsNumber().longValue();
            category_longs.add(categoryId);
        }
        return categoryClient.queryCategoryByids(category_longs);
    }

    private List<Map<String,Object>> searchAggsSpecs(Long cid,QueryBuilder queryBuilder){
        List<Map<String,Object>> specs=new ArrayList<>();

//        获取所有可以搜索的 参数
        List<SpecParam> specParams = specParamClient.querySpecParamByCidGid(cid, null, true);

//        进行聚合
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();

        nativeSearchQueryBuilder.withQuery(queryBuilder);

//        聚合每个参数下各个值      字段名格式-----> specs.CPU品牌.keyword
        specParams.forEach( specParam -> {
            String spec_param_name = specParam.getName();
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(spec_param_name).field("specs."+spec_param_name+".keyword"));
        });
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(nativeSearchQueryBuilder.build());

//        取出聚合桶放入集合中  格式List<Map<String,Object>>
        Map<String, Aggregation> stringAggregationMap = search.getAggregations().asMap();

        specParams.forEach( specParam -> {
            Map<String,Object> map=new HashMap<>();
            String spec_param_name = specParam.getName();

//            设置参数名称  放入可以中
            map.put("filter_key",spec_param_name);
            Aggregation a = stringAggregationMap.get(spec_param_name);
            StringTerms valueTerms= (StringTerms) a;

            List<String> options=new ArrayList<>();
            valueTerms.getBuckets().forEach(bucket -> {
//                聚合的参数值放入
                options.add(bucket.getKeyAsString());
            });
            map.put("options",options);
            specs.add(map);
        });

        return specs;
    }


//    为过滤  提供 boolquery 方法
    private QueryBuilder creatBoolQuery(SearchPage searchPage){
        BoolQueryBuilder queryBuilder=QueryBuilders.boolQuery();
//        查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all",searchPage.getKey()).operator(Operator.AND));

//        过滤查询
        Map<String, String> filters = searchPage.getSearchFilters();
        if(filters!=null) {
            BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key=="categories" || key=="brands"){
                    key=(key=="categories")?"cid3":"brandId";
                }else{
                    key=("specs."+key+".keyword");
                }

                boolQueryBuilder.must(QueryBuilders.termQuery(key, value));
            }
            queryBuilder.filter(boolQueryBuilder);
        }
        return queryBuilder;
    }


    @Override
    public void createIndex(Long spuId) throws IOException {
        // 查询spu
        Spu spu = this.spuClient.querySpuBySpuId(spuId);
        if(null == spu){
            logger.error("索引对应的spu不存在，spuId：{}", spuId);
            // 抛出异常，让消息回滚
            throw new RuntimeException();
        }
        // 查询sku信息
        List<Sku> skus = this.skuClient.querySkuBySpuId(spuId);
        // 查询详情
        SpuDetail spuDetail = this.spuDetailClient.querySpuDetailBySpuId(spuId);
        // 查询商品分类名称
        List<String> strings = this.categoryClient.queryCategoryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (null == skus || null == spuDetail || null ==strings ) {
            logger.error("索引对应的spu详情及sku不存在，spuId：{}", spuId);
            // 抛出异常，让消息回滚
            throw new RuntimeException();
        }

        // 准备sku集合
        List<Map<String, Object>> skuList = new ArrayList<>();
        // 准备价格集合
        Set<Long> price = new HashSet<>();
        for (Sku s : skus) {
            price.add((long) s.getPrice());
            Map<String, Object> sku = new HashMap<>();
            sku.put("id", s.getId());
            sku.put("price", s.getPrice());
            sku.put("image", StringUtils.isBlank(s.getImages()) ? "" : s.getImages().split(",")[0]);
            sku.put("title", s.getTitle());
            skuList.add(sku);
        }

        ObjectMapper mapper=new ObjectMapper();
        // 获取商品详情中的规格模板
        List<Map<String, Object>> specTemplate = mapper.readValue(spuDetail.getSpecialSpec(), new TypeReference<List<Map<String, Object>>>() {});
        Map<String, Object> specs = new HashMap<>();
        // 过滤规格模板，把所有可搜索的信息保存到Map中
        specTemplate.forEach(m -> {
            List<Map<String, Object>> params = (List<Map<String, Object>>) m.get("params");
            params.forEach(p -> {
                if ((boolean) p.get("searchable")) {
                    if (p.get("v") != null) {
                        specs.put(p.get("k").toString(), p.get("v"));
                    } else if (p.get("options") != null) {
                        specs.put(p.get("k").toString(), p.get("options"));
                    }
                }
            });
        });

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(strings, " ")); //全文检索字段
        goods.setPrice(new ArrayList<>(price));
        goods.setSkus(mapper.writeValueAsString(skuList));
        goods.setSpecs(specs);

        // 保存数据到索引库
        this.goodsRepository.save(goods);
    }

    @Override
    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId);
    }

}
