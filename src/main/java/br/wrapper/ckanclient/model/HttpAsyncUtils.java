package br.wrapper.ckanclient.model;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Paulo on 01/06/14.
 */
public class HttpAsyncUtils {
    public static Promise<HttpResponse, Throwable, Double> doGet(final URI resourceUrl) {
        final CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        final DeferredObject<HttpResponse, Throwable, Double> deferred = new DeferredObject<HttpResponse, Throwable, Double>();
        final HttpGet httpGet = new HttpGet(resourceUrl);

        client.start();
        client.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
                    if (statusCode >= 400){
                        deferred.reject(new HttpResponseException(statusCode, reasonPhrase));
                    } else {
                        deferred.resolve(httpResponse);
                    }
                    client.close();
                } catch (IOException e) {
                    deferred.reject(e);
                }
            }

            @Override
            public void failed(Exception e) {
                try {
                    deferred.reject(e);
                    client.close();
                } catch (IOException e1) {
                    deferred.reject(e1);
                }
            }

            @Override
            public void cancelled() {
                try {
                    deferred.reject(new Exception("Request cancelled"));
                    client.close();
                } catch (IOException e) {
                }
            }
        });

        return deferred.promise();
    }
}
