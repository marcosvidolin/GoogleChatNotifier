package com.vidolima.gcn;


import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class NotifierClient {
    private String webHookUrl;
    private String message;

    private NotifierClient(String webHookUrl, String message) {
        this.webHookUrl = webHookUrl;
        this.message = message;
    }

    public String getWebHookUrl() {
        return this.webHookUrl;
    }

    public String getMessage() {
        return this.message;
    }

    public static class Builder {
        private String webHookUrl;
        private String message;

        public Builder webHookUrl(String webHookUrl) {
            this.webHookUrl = webHookUrl;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public NotifierClient build() {
            return new NotifierClient(this.webHookUrl, this.message);
        }
    }

    public void sendNotification() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(getWebHookUrl());
        StringEntity entity = new StringEntity(getMessage());
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = httpclient.execute(httpPost);
        response.close();
    }
}
