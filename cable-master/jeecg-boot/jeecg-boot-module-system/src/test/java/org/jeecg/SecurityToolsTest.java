package org.jeecg;

import cn.hutool.json.JSONObject;
import org.jeecg.common.util.security.SecurityTools;
import org.jeecg.common.util.security.entity.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SecurityToolsTest {

    @Test
    public void Test11() {
        List list = new ArrayList();
        list.add("aa");
        list.add("bb");
        list.add("aa");
        list.add("cc");
        HashSet set = new HashSet<>(list);
        Boolean result = set.size() == list.size() ? true : false;
        if (!result) System.out.println(result + "有重复数据");
        else if (result) System.out.println(result + "没有重复数据");
    }

    @Test
    public void Test() {
        MyKeyPair mkeyPair = SecurityTools.generateKeyPair();

        JSONObject msg = new JSONObject();
        msg.put("name", "党政辉");
        msg.put("age", 50);
        JSONObject identity = new JSONObject();
        identity.put("type", "01");
        identity.put("no", "210882165896524512");
        msg.put("identity", identity);

        // 签名加密部分
        SecuritySignReq signReq = new SecuritySignReq();
        // data为要加密的报文字符串
        signReq.setData(msg.toString());
        // 为rsa私钥
        signReq.setPrikey(mkeyPair.getPriKey());
        // 调用签名方法
        SecuritySignResp sign = SecurityTools.sign(signReq);
        // 打印出来加密数据
        // signData为签名数据
        // data为aes加密数据
        // asekey为ras加密过的aeskey
        System.out.println(new JSONObject(sign).toStringPretty());

        // 验签解密部分
        SecurityReq req = new SecurityReq();
        //对方传过来的数据一一对应
        req.setAesKey(sign.getAesKey());
        req.setData(sign.getData());
        req.setSignData(sign.getSignData());
        //我们的公钥
        req.setPubKey(mkeyPair.getPubKey());
        //验签方法调用
        SecurityResp securityResp = SecurityTools.valid(req);
        //解密报文data为解密报文
        //sucess 为验签成功失败标志 true代码验签成功，false代表失败
        System.out.println(new JSONObject(securityResp).toStringPretty());
    }

    /**
     * CompletableFuture 异步回调测试
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        // 无返回值的 runAsync 异步回调
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "runAsync->Void");
        });
        System.out.println("111");
        completableFuture.get();
    }

    /**
     * CompletableFuture 异步回调测试2
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException {
        // 有返回值的 supplyAsync 异步回调
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "supplyAsync->Integer");
            int i = 10 / 0;
            return 1024;
        });

        System.out.println(completableFuture.whenComplete((u, t) -> {
            System.out.println("u->" + u);  // 正常结果
            System.out.println("t->" + t);  // 错误结果 java.lang.ArithmeticException: / by zero
        }).exceptionally((e) -> {
            System.out.println(e.getMessage());
            return 404; // 能够获取到异常情况的值
        }).get()); // 能够获取到返回值
    }
}
