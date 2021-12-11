/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.haoke91.a91edu.utils.rsa;


/**
 * @author lianyutao
 * @version 3.0.0
 * @description
 * @date 2018/8/1 17:42
 */
public class RsaTest {

    public final static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDZRTX34OBFhNI1IBF+af4JZc3\n" +
            "DUwYAR80rIZ2CA7+qKfgKQl5LBIfcaRGdtZoWg+C2uv2ZE9nmLBK478ymVOfha5m\n" +
            "V76cMqK3OuYM2+vARccunvTA7v30WWcnLc29L2WtUl9qkQsMfNHJsiSIFQjQIF0/\n" +
            "DkNxswY3sXcpjTcFKwIDAQAB";
    private final static String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMNlFNffg4EWE0jU\n" +
            "gEX5p/gllzcNTBgBHzSshnYIDv6op+ApCXksEh9xpEZ21mhaD4La6/ZkT2eYsErj\n" +
            "vzKZU5+FrmZXvpwyorc65gzb68BFxy6e9MDu/fRZZyctzb0vZa1SX2qRCwx80cmy\n" +
            "JIgVCNAgXT8OQ3GzBjexdymNNwUrAgMBAAECgYB3UT6NJHxVU3mrO7ipAm9D5RCW\n" +
            "GZSiA+1eaQwi/4DImY0KIYUbbYITmX3LWb5MMtLOAYmeCFtE08SztqeG6oJZEigh\n" +
            "d7Du4gEW4XyBTI5HxqbP6tfLjx9Z9WwarGTdmtXqc6f547yiWWnIjjyBtp2QUI3B\n" +
            "vHGrmvg/ACHZjBfzoQJBAOcp3mlOQbBQDDBGIo8QN68btMRmI3NTlilIIzV5+PIP\n" +
            "lKWesvuL4A+vEJ1Izt6akdHP23oNooaRbwsbDbwvLC0CQQDYY2XgP4rTE4nw1jGH\n" +
            "OYNjbXzClW58fcMziWGDf7z5iIVBwdVEV/Xpl0kjJ52O940aFdS2JIMSvfNaO3/z\n" +
            "0dW3AkBoZvmDeAuk9NI8b0Cmen2bwZin5zV1nbfBF0qHmXn84VqSXpWZlHfyvW90\n" +
            "ifCcixT2vLj0a/oOatrLGkHezzNVAkB7V0z+obmTzxNVgAIDPjkyN3phv6OWumjF\n" +
            "u40l0234HmU2sjHKHwlA+Mbrju00TM7VC5SPrgg4aqWHzVqFtBnJAkEAwF2EPqnt\n" +
            "UAjikcLMpzC0UYB7Il1aF/LK2RdYVRK8TFwoOnW4nSfPOqtqsYtANY6CyCGIB3Em\n" +
            "fC+Re95BtklEIQ==";

    public static void main(String[] args) throws Exception {
        jiami();
        jiemi();
        System.out.println(PUBLICKEY);

        System.out.println("------------------");
        System.out.println(PRIVATEKEY);
    }

    /*
     * 测试自己封装java端加密和解密的方法
     */
    static void jiami() {
        {
            String data = "13716151311";
            System.out.println("原始数据：" + data);
            data = RSAUtils.encryptedDataOnJava(data, PUBLICKEY);
            System.out.println("加密数据：" + data);
            System.out.println("解密数据：" + RSAUtils.decryptDataOnJava(data, PRIVATEKEY));
        }

        {
            String data = "123123";
            System.out.println("原始数据：" + data);
            data = RSAUtils.encryptedDataOnJava(data, PUBLICKEY);
            System.out.println("加密数据：" + data);
            System.out.println("解密数据：" + RSAUtils.decryptDataOnJava(data, PRIVATEKEY));
        }
    }

    static void jiemi() {
        String data = "EjQpJHWipha2GS+WU8pvIG82YlXihlobHK2eC/FbMlM6NDMUKtrbLnhgu+dOQcpL5YK8xNmEOIdLHB0k/WjNI5cYP9Wads6r4X91zZ1KvszhFUU0PGpuocBzH0E648dO2RTQ+tlLd3p/eHrsF7m2o0op1OqYx8xOS9EuEd8dIeA=";
        System.out.println("解密数据：" + RSAUtils.decryptDataOnJava(data, PRIVATEKEY));
    }

}
