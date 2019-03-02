package com.study.auth;

import com.study.entiy.UserInfo;
import com.study.utils.JwtUtils;
import com.study.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "E:\\test\\rsa.pub";

    private static final String priKeyPath = "E:\\test\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    /**
     *  根据密文，生成rsa公钥和私钥,并写入指定文件
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "ygb"), privateKey, 1);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoieWdiIiwiZXhwIjoxNTUxNTE5MTcwfQ.SvPMUCUcCD6ze6MZWKR7h43UKMNlCEaK0a_IAdneZTRen8Kir775-42GjInkoyyrz45u8g-O2CyVjm-A6MqaQTz9vDBo2rAOoykO5WqhXFBFXCrGZ_5CUt6Us1709HyKsEsAr_XsvRKh_qbbCyYC3OCGPhqolVgMKwlrjAgw940";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
