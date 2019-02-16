package com.study.service;

import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.pojo.Sku;
import com.study.pojo.Spu;

import java.util.List;

public interface SpuService {
    public PageResult<SpuBo> querySpuByPage(Integer pageNo, Integer pageSize, String key, Boolean saleable);

    public void addSpu(Spu spu);

    void updateSpu(Spu spu);

    void upOrDownSpuBySpuId(Long spuId,Boolean saleable);

    void deleteSpuBySpuId(Long spuId);

    Spu querySpuBySpuId(Long spuId);
}
