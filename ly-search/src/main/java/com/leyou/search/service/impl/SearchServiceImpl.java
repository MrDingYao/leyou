package com.leyou.search.service.impl;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.ISearchService;
import com.leyou.search.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description //TODO 商品搜索的服务
 * @Author santu
 * @Date 2018 - 10 - 15 23:42
 **/
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);


    /**
     * 分页查询商品信息
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {

        // 判断是否输入了搜索条件,如果没输，则返回null或者给个默认搜索条件
        if (StringUtils.isBlank(request.getKey())) {
            return null;
        }

        // 创建搜索工具
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 对结果进行过滤,只需要id,subTitle,skus这三个结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        // 设置查询条件
        QueryBuilder query = buildBasicQueryWithFilter(request);
        queryBuilder.withQuery(query);
        // 进行分页查询
        searchWithPageAndSort(request, queryBuilder);
        // 创建商品分类聚合的名字
        String categoryAggName = "category";
        // 创建商品品牌聚合的名字
        String brandAggName = "brand";
        // 添加商品分类的聚合和品牌的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        // 开始搜索，获得结果
        AggregatedPage<Goods> result = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());
        long total = result.getTotalElements();
        long totalPages = (total + request.getSize() - 1) / request.getSize();
        // 开始结果的解析,获得分类的集合和品牌的集合
        List<Category> categories = getCategoryAggResult(result.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(result.getAggregation(brandAggName));

        // TODO 判断分类集合内容是否只有一个，若是，则需要查询该分类下的规格参数
        List<Map<String,Object>> specs = null;
        if (categories.size() == 1) {
            specs = getSpecs(categories.get(0).getId(),query);
        }

        // 返回分页查询的结果
        return new SearchResult(total,totalPages,result.getContent(),categories,brands,specs);
    }

    /**
     * 生成含有过滤项的搜索器
     * @param request
     * @return
     */
    private QueryBuilder buildBasicQueryWithFilter(SearchRequest request) {
        // 创建bool查询器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 创建过滤查询器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        Map<String, String> filter = request.getFilter();
        // 遍历过滤项的键值对，设置过滤条件
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // 添加过滤条件
        boolQueryBuilder.filter(filterQueryBuilder);
        return boolQueryBuilder;
    }

    private List<Map<String, Object>> getSpecs(Long id, QueryBuilder query) {
        try {
            // 初始化返回值
            List<Map<String,Object>> specs = new ArrayList<>();
            // 查询该分类id下的所有的可用于搜索的规格参数
            List<SpecParam> params = this.specificationClient.querySpecParams(null, id, true, null);
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);
            // 遍历所有的规格参数
            params.forEach(p -> {
                // 给每个规格参数设置聚合
                queryBuilder.addAggregation(AggregationBuilders.terms(p.getName()).field("specs." + p.getName() + ".keyword"));

            });
            // 进行查询
            Map<String, Aggregation> aggs = this.esTemplate.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();
            // 解析聚合结果
            params.forEach(p -> {
                String key = p.getName();
                HashMap<String, Object> spec = new HashMap<>();
                spec.put("k",key);


                    spec.put("options", ((StringTerms) aggs.get(key)).getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));

                specs.add(spec);
            });
            return specs;
        } catch (Exception e) {
            LOGGER.error("聚合规格参数出现异常",e);
            return null;
        }
    }

    /**
     * 分页查询的方法
     * @param request
     * @param queryBuilder
     */
    private void searchWithPageAndSort(SearchRequest request, NativeSearchQueryBuilder queryBuilder) {
        // 设置分页查询
        queryBuilder.withPageable(PageRequest.of(request.getPage()-1, request.getSize()));
        // 判断是否有排序字段
        String sortBy = request.getSortBy();
        Boolean descending = request.getDescending();
        if (!StringUtils.isBlank(sortBy)) {
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.DESC : SortOrder.ASC));
        }
    }

    /**
     * 品牌聚合解析
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        try {
            LongTerms longTerms = (LongTerms) aggregation;
            List<Long> bids = longTerms.getBuckets().stream()
                    .map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            return this.brandClient.queryBrandByIds(bids);
        } catch (Exception e) {
            LOGGER.error("品牌聚合出现错误",e);
            return null;
        }

    }

    /**
     * 解析聚合查询的结果得到分类的集合
     * @param aggregation
     * @return
     */
    private List<Category> getCategoryAggResult(Aggregation aggregation) {
        try {
            List<Category> categories = new ArrayList<>();
            //if(aggregation instanceof LongTerms) {
                LongTerms longTerms = (LongTerms) aggregation;
                // 解析得到所有的分类id的集合
                List<Long> cids = longTerms.getBuckets().stream()
                        .map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
                // 查询cids对应的分类名字的集合
                List<String> names = this.categoryClient.queryNameByIds(cids);

                Category category = new Category();
                for (int i = 0; i < cids.size(); i++) {
                    category.setId(cids.get(i));
                    category.setName(names.get(i));
                    categories.add(category);
                }
                System.out.println(categories);
           // }
            return categories;
        } catch (Exception e) {
            LOGGER.error("分类聚合出现异常",e);
            return null;
        }
    }

    /**
     * 随着后台更新数据库而更新前台的索引库
     * @param id
     */
    @Override
    public void createIndex(Long id) {
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.indexService.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    /**
     * 接收到rabbitMQ发送来的delete消息后，删除索引
     * @param id
     */
    @Override
    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
