/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

/**
 *
 * @author zlhso
 */
@Log4j2
public class HttpGetUtil {
    
    
    public static class GetResponse {

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

    public static GetResponse getJson(
            String url, 
            Map<String, Object> params) {
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
            HttpGet httpGet = new HttpGet(url);
            log.info("Params - {}", params);
            
            
            List<NameValuePair> nvps=new ArrayList<>();
            for(Entry<String,Object> entry:params.entrySet()){
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            URI uri=new URIBuilder(httpGet.getURI()).addParameters(nvps).build();
           
            httpGet.setHeader("Accept", "application/json");
            //httpGet.setHeader("Content-type", "application/json");
            httpGet.setURI(uri);
            
            HttpResponse response = client.execute(httpGet);
            GetResponse postResponse = new GetResponse();
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
            log.info("response:{}",postResponse.responseBody);
            return postResponse;
        } catch (IOException ex) {
            log.info(ex.getMessage());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        return null;
    }
}
