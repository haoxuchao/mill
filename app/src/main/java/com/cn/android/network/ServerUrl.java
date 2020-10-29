package com.cn.android.network;

import com.cn.android.utils.SPUtils;

/**
 * Created by PC-122 on 2017/12/21.
 */
public class ServerUrl {

    public static  String defaultIp="http://192.168.0.118:8000/";
    public String useIp="http://192.168.100.190:8080";
    public  boolean s =  SPUtils.getBoolean("isS",false);
    public String ip = s ? useIp: defaultIp;
    public String agentIp=ip;

    //和user用户有关URL
    public String API = agentIp+"/btlcommon/UserCompMapping/";
    public static String getHtml="http://www.391k.com/api/xapi.ashx/info.json";
    public static String getPost="http://192.168.0.111:8012/app/photo/addAlbum";
    public static String uploadFile="http://47.105.171.135:3000/upload/";
    public static String downloadFile="http://p.gdown.baidu.com/67327e8c8685bca04ccdd877dea57de6d44a01c4b3dedcf2a3e14a80da15b6c5633bfee4dafdbfc9a23b0541b9c595fabff514f4f95de457c2d85bcf9c73b5f4fdce21e0a06cd1829cfe52a561251e8dd2cda14c3089995a37860ab1619402fe3e143b38212ed7ae6f73caf02cf2b7eb494c21cce868c5abbf05fce5593b27fcdeecc405ba08a97ec964642314f444ff96c586125818c11dd0ef497961b1c8fd24aa654001e8ce1b7da5d826ad0aa21f3651f5e0abd6be3f1538926fee90cd002550638cf98ad3fe3b05ecc7f9fa138fce273455fa5877c35a225c555a91eaa5ce69af76bc1f38d31220c93f73ecd73854cae34f9a1fc070bae0bcdcea929b9e2dc5a65113bc2c41";
    public static String getImage="http://images.csdn.net/20150817/1.jpg";
    public static String login=defaultIp+"/app/login/login";//登录POST
    public static String register=defaultIp+"/app/login/register";//用户注册,成功直接登录POST
    public static String sendSms=defaultIp+"/app/login/sendSms";//sendSmsGET
    public static String selectAppUserByUserid=defaultIp+"/app/user/selectAppUserByUserid";//个人信息GET
    public static String upload=defaultIp+"/app/user/upload";//上传图片GET
    public static String updateAppUser=defaultIp+"/app/user/updateAppUser";//基本信息设置GET
    public static String updateFirstPayWord=defaultIp+"/app/user/updateFirstPayWord";//设置支付密码(没有支付密码的时候)POST
    public static String updateNewPayWord=defaultIp+"/app/user/updateNewPayWord";//设置支付密码(有支付密码的时候)POST
    public static String updatePhone=defaultIp+"/app/user/updatePhone";//更改手机号POST
    public static String selectAppCommon=defaultIp+"/app/user/selectAppCommon";//客服二维码图片、开通商铺的保证金eb、账号激活金额、提现最低金额、提现手续费GET
    public static String realAppuser=defaultIp+"/app/user/realAppuser";//实名认证POST
    public static String openShop=defaultIp+"/app/shop/openShop";//开通商铺POST
    public static String addShop=defaultIp+"/app/user/addShop";//发布/编辑商品POST
    public static String addVideo=defaultIp+"/app/user/addVideo";//为它代言 POST
    public static String selectNewShopList=defaultIp+"/app/shop/selectNewShopList";//最新商品列表 GET
    public static String selectShopById=defaultIp+"/app/shop/selectShopById";//商品详情 GET
    public static String selectMyShopList=defaultIp+"/app/user/selectMyShopList";//我的商品列表 GET
    public static String selectVideoList=defaultIp+"/app/home/selectVideoList";//首页视频列表 GET
    public static String updateParise=defaultIp+"/app/home/updateParise";//点赞/取消点赞 POST
    public static String addFocus=defaultIp+"/app/home/addFocus";//关注POST
    public static String delShop=defaultIp+"/app/user/delShop";//删除商品POST
    public static String reciveOrder=defaultIp+"/app/user/reciveOrder";//我的订单（用户）===确认收货POST
    public static String selectMessageList=defaultIp+"/app/user/selectMessageList";//消息列表GET
    public static String selectOrderList=defaultIp+"/app/user/selectOrderList";//我的订单列表GET
    public static String selectPoster=defaultIp+"/app/user/selectPoster";//生成邀请码图片GET
    public static String selectUserFocusList=defaultIp+"/app/user/selectUserFocusList";//我的关注列表GET
    public static String selectShops=defaultIp+"/app/user/selectShops";//我的秘密店铺列表GET
    public static String delUserFocus=defaultIp+"/app/user/delUserFocus";//我的关注列表===取消关注POST
    public static String sendOrder=defaultIp+"/app/user/sendOrder";//我的订单（商家）===发货POST
    public static String addCartOrder=defaultIp+"/app/shop/addCartOrder";//商品==加入购物车POST
    public static String addOrder=defaultIp+"/app/shop/addOrder";//商品==立即购买(跳转到确认订单页面POST
    public static String delCartOrder=defaultIp+"/app/shop/delCartOrder";//购物车===删除POST
    public static String saveOrder=defaultIp+"/app/shop/saveOrder";//购物车==结算(跳转到确认订单页面)POST
    public static String selectCartOrderList=defaultIp+"/app/shop/selectCartOrderList";//购物车列表GET
    public static String selectShopListBySearch=defaultIp+"/app/shop/selectShopListBySearch";//搜索GET
    public static String updateCartOrderNum=defaultIp+"/app/shop/updateCartOrderNum";//购物车===改变数量POST
    public static String updatePayPassword=defaultIp+"/app/shop/updatePayPassword";//开通商铺===设置交易密码POST
    public static String payShop=defaultIp+"/app/shop/payShop";//商品支付POST
    public static String panduanPayPassword=defaultIp+"/app/shop/panduanPayPassword";//开通商铺/购买商品===判断交易密码GET
    public static String addAddress=defaultIp+"/app/address/addAddress";//添加用户地址POST
    public static String deleteAddress=defaultIp+"/app/address/deleteAddress";//删除用户地址POST
    public static String selectAddressByUserid=defaultIp+"/app/address/selectAddressByUserid";//我的地址列表POST
    public static String updateAddress=defaultIp+"/app/address/updateAddress";//编辑用户地址POST
    public static String addBiAccount=defaultIp+"/app/bi/addBiAccount";//求购==添加POST
    public static String addBiAccountByPid=defaultIp+"/app/bi/addBiAccountByPid";//出售==添加POST
    public static String finishBiAccount=defaultIp+"/app/bi/finishBiAccount";//我的交易==出售单 确认收款POST
    public static String payBiAccount=defaultIp+"/app/bi/payBiAccount";//我的交易==求购单 去支付POST
    public static String selectAccountDetials=defaultIp+"/app/bi/selectAccountDetials";//我的交易==详情GET
    public static String selectBiAccountList=defaultIp+"/app/bi/selectBiAccountList";//交易记录==列表GET
    public static String selectBiAccountListByUserid=defaultIp+"/app/bi/selectBiAccountListByUserid";//我的交易==求购单/出售单 列表 （status 求购单（type=3）：1去支付 2确认支付 出售单： 1等待支付 2确认收款 3已完成GET
    public static String seletOneBiMoney=defaultIp+"/app/bi/seletOneBiMoney";//单价GET
    public static String deleteBiAccount=defaultIp+"/app/bi/deleteBiAccount";//我的交易==求购单=取消发布 （type=1才有取消发布）POST
    public static String selectDownUserList=defaultIp+"/app/user/selectDownUserList";//我的团队列表GET
    public static String selectMillList=defaultIp+"/app/user/selectMillList";//矿机列表GET
    public static String buyAccount=defaultIp+"/app/user/buyAccount";//激活POST
    public static String useMill=defaultIp+"/app/user/useMill";//我的矿机列表==分红矿机启用POST
    public static String buyMill=defaultIp+"/app/user/buyMill";//矿机列表==购买矿机/POST
    public static String addVideoNum=defaultIp+"/app/home/addVideoNum";//看视频添加===数量POST
}
