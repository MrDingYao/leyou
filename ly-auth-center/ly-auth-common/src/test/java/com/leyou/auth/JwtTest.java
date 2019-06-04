//package com.leyou.auth;
//
//
//import com.leyou.auth.entity.UserInfo;
//import com.leyou.auth.utils.JwtUtils;
//import com.leyou.auth.utils.RsaUtils;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.security.PrivateKey;
//import java.security.PublicKey;
//
///**
// * @Description //TODO
// * @Author santu
// * @Date 2018 - 10 - 22 19:10
// **/
//public class JwtTest {
//
//    // 公钥存放路径
//    private static final String pubKeyPath = "C:\\Users\\dy181\\Documents\\RsaKey\\rsa.pub";
//
//    // 私钥存放路径
//    private static final String priKeyPath = "C:\\Users\\dy181\\Documents\\RsaKey\\rsa.pri";
//
//    private PublicKey publicKey;
//
//    private PrivateKey privateKey;
//
//    /**
//     * 生成公钥和私钥的方法
//     * @throws Exception
//     */
//    @Test
//    public void testRsa() throws Exception {
//        RsaUtils.generateKey(pubKeyPath,priKeyPath,"123456");
//    }
//
//    /**
//     * 读取公钥和私钥
//     * @throws Exception
//     */
//    @Before
//    public void testGetRsa() throws Exception {
//        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
//        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
//    }
//
//    /**
//     * 生成一个用户加密后的token
//     * @throws Exception
//     */
//    @Test
//    public void testGenerateToken() throws Exception {
//        String token = JwtUtils.generateToken(new UserInfo(27L, "你可真是个弟弟"), this.privateKey, 5);
//        System.out.println(token);
//    }
//
//    @Test
//    public void testGetInfoFromToken() throws Exception {
//        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjcsInVzZXJuYW1lIjoi5L2g5Y-v55yf5piv5Liq5byf5byfIiwiZXhwIjoxNTQwMjA4MDQ0fQ.RpHfXI_Xnywn7996mopsrtmo_6Fm8P_i-HteV2Ya4bqEhXmByGVbJGC4Ff3ZIloxIz94IhOWN2VNB0xDbLJp5rnd-cCVM_6QRb0REtdGzyEkUZgZQVmpPlM2Q1uX0dxD4H8vsJBIbiEqtZhg2w1VXECXWE1p-sYrkLWakoqVUJA";
//        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.publicKey);
//        System.out.println("id：" + userInfo.getId());
//        System.out.println("username：" + userInfo.getUsername());
//    }
//}
