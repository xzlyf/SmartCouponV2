package com.xz.jlw2.constant;

public class Local {
    //选单网key
    public static final String APIKEY = "hcntgu1k3i";
    public static final String BASE_URL = "http://api.xuandan.com/DataApi/";
    //接口
    public static final String INDEX = "index";//普通商品\销量排行
    public static final String PYQGOODS = "PyqGoods";//朋友圈爆款
    public static final String PYQGOODSPAGE = "PyqGoodsPage";//微信文案
    public static final String GETGOODSPYQ = "GetGoodsPyq";//微信文案(新)
    public static final String HOTGOODS = "HotGoods";//选单预告
    public static final String ITEM = "item";//单品详情
    public static final String RANKLIST = "RankList";//佣金榜单
    public static final String TOP100 = "Top100";//2小时销量\全天销量
    public static final String DOWNGOODS = "Down_Goods";//下架产品
    public static final String CHECKITEM = "Checkitem";//检测商品是否存在
    public static final String GETITEMTKLLINK = "GetItemTklLink";//单品转链
    public static final String GOODSACTIVITY = "goods_activity";//双11预售

    //大淘客key
    public static final String APIKEY_DTK = "5d24967e3c4d6";//apikey
    public static final String VERSION_DTK = "v1.0.1";//接口版本
    public static final String APP_SECRET_DTK = "e25f590cc656794c86f7da47ea1ba545";//sign
    //大淘客接口
    public static final String HOTWORD = "https://openapi.dataoke.com/api/category/get-top100";//热词排行

    public static boolean cartChange = false;//购物车发生改变标识

    //handler数值
    public static final int CODE_1 = 999;//上拉加载
    public static final int CODE_2 = 998;//自定义功能1
    public static final int CODE_3 = 997;//自定义功能1
    public static final int CODE_4 = 996;//自定义功能1
    public static final int CODE_5 = 995;//购物车列表
    public static final int CODE_6 = 1001;//购物车列表刷新
    public static final int MODE_CART = 994;//购物车模式


}
