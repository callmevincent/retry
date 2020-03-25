package com.iyb.retry.service;

import com.alibaba.fastjson.JSON;
import com.iyb.retry.annotation.RetryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

@Service
public class RequestService implements IFunctions {
    private static Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Override
    @RetryStrategy
    public Object run(Object[] objects, Object factors) {
        Object v = objects[1];
        String req;

        if (v == null)
            req = null;
        else if (v instanceof JSON)
            req = ((JSON) v).toJSONString();
        else
            req = JSON.toJSONString(v);

        String result = request((String) objects[0], req, 300);
        Objects.requireNonNull(factors, "模拟出错");
        return result;
    }

    private static String request(String urlstr, String req, int timeout) {
        String res = null;

        //System.out.println("req: " + urlstr + " >> " + req);

        HttpURLConnection conn = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            sslContext.init(null, tm, new java.security.SecureRandom());
            ;
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (conn instanceof HttpsURLConnection)
                ((HttpsURLConnection) conn).setSSLSocketFactory(ssf);
            byte[] info = req == null ? null : req.getBytes(StandardCharsets.UTF_8);
            if (info != null)
                conn.setRequestProperty("Content-Length", String.valueOf(info.length));

            conn.connect();

            try (OutputStream out = conn.getOutputStream()) {
                if (info != null)
                    out.write(info);
                out.flush();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (InputStream in = conn.getInputStream()) {
                byte[] b = new byte[1024];
                int c = 0;
                while ((c = in.read(b)) >= 0) {
                    baos.write(b, 0, c);
                }
            }
            baos.close();

            res = baos.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        //System.out.println("res: " + res);

        return res;
    }

    private static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
