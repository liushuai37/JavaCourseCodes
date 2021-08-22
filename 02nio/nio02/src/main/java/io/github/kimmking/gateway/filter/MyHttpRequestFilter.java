package io.github.kimmking.gateway.filter;

import com.sun.tools.javac.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.internal.StringUtil;
import sun.security.provider.MD5;
import sun.security.rsa.RSASignature;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHttpRequestFilter implements HttpRequestFilter{
    private static final Charset DEFAULT_CHARACTER=Charset.forName("utf-8");
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        ByteBuf content = fullRequest.content();
       String md5 = this.getMd5(content.array());
       fullRequest.headers().add("request-signature",md5);
    }
    private String getMd5(byte[] bytes){
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
