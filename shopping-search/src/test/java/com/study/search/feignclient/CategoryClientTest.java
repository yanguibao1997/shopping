package com.study.search.feignclient;

import com.study.ShoppingSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingSearch.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void testQueryCategoryNameByIds(){
        List<String> strings = categoryClient.queryCategoryNameByIds(Arrays.asList(1l, 2l, 3l));

        strings.forEach(s -> System.out.println("s = " + s));
    }
}