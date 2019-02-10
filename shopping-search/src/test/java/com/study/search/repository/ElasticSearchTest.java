package com.study.search.repository;

import com.study.ShoppingSearch;
import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.search.feignclient.SpuClient;
import com.study.search.pojo.Goods;
import com.study.search.service.GoodsService;
import com.study.search.service.impl.GoodsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingSearch.class)
public class ElasticSearchTest{

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void createIndexTest(){
//        创建索引
        elasticsearchTemplate.createIndex(Goods.class);

//        创建Mapping
        elasticsearchTemplate.putMapping(Goods.class);
    }

    /**
     * 添加数据
     */
    @Test
    public void loadGoods(){
//        需要查询所有的spu
        PageResult<SpuBo> spuBoPageResult = spuClient.querySpuPage(1, Integer.MAX_VALUE, null, null);

        for (SpuBo spuBo : spuBoPageResult.getItems()) {
            Goods goods = goodsService.goodsService(spuBo);

            goodsRepository.save(goods);
        }
    }
}