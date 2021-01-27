/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import lombok.extern.log4j.Log4j2;
/**
 *
 * @author zlhso
 */

@Log4j2
public class HttpPostUtil {
    
    

    public static class PostResponse {

        private Integer statusCode;
        private String responseBody;

        /**
         * @return the statusCode
         */
        public int getStatusCode() {
            return statusCode;
        }

        /**
         * @param statusCode the statusCode to set
         */
        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public void setResponseBody(String responseBody) {
            this.responseBody = responseBody;
        }

    }

    public static PostResponse postJson(
            String url, 
            Map<String, Object> postData) {
        
        PostResponse postResponse = new PostResponse();
        try {

            TrustStrategy trustStrategy = new TrustAllStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };

            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();

            SSLConnectionSocketFactory sslSocketFac = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFac).build();

            HttpClient client = httpClient;
            HttpPost httpPost = new HttpPost(url);
            log.info("Post Data - {}", postData);
            String json = JsonUtil.toJsonString(postData);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(httpPost);
            
            int statuscode = response.getStatusLine().getStatusCode();
            postResponse.statusCode = statuscode;
            try ( BufferedReader buf = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()))) {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = buf.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                postResponse.responseBody = sb.toString();
            }

            
        } catch (IOException ex) {
            log.info(ex.getMessage());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        return postResponse;
    }
}
