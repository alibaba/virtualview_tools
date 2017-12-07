1. 在模板目录下配置build.json文件，内容如下，注意顺序，模板顺序与生成的id有关:

```
{
  "template": [
    "532_topNav.xml",
    "532_fast_channel_item.xml",
    "532_tmall_hot.xml",
    "532_tmall_hot_item.xml",
    "532_top_guess_like.xml",
    "532_guess_like_item.xml",
    "532_brand_shop.xml",
    "532_brand_shop_item.xml",
    "532_brand_fast_buy.xml",
    "532_brand_fast_buy_item.xml",
    "532_user_recommend.xml",
    "532_user_recommend_item.xml",
    "532_life_research.xml",
    "532_life_research_item.xml",
    "532_image.xml",
    "532_topNav_4.xml",
    "532_user_juhuasuan_recommend_item.xml",
    "532_user_similar_product_item.xml",
    "610_industry_entry.xml",
    "618_scence_guess_like_item.xml"
  ]
}
```

2. 执行`java -jar compiler-tools.jar [path of template]`，会在模板目录下生成bin_template.out和byte_template.byte文件，前者是原始二进制文件，后缀是二进制文件的字节数组表示