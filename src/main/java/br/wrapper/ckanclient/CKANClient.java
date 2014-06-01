package br.wrapper.ckanclient;

import br.wrapper.ckanclient.exceptions.DatasetException;
import br.wrapper.ckanclient.model.DatasetDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.jdeferred.DeferredManager;
import org.jdeferred.Promise;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.impl.DeferredObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Paulo on 31/05/14.
 */
public class CKANClient {

    public static DeferredManager manager = new DefaultDeferredManager();

    private URI rootUri;

    public CKANClient(String s) throws URISyntaxException {
        this.rootUri = new URI(s);
    }

    public CKANClient(URI uri) {
        this.rootUri = uri;
    }

    public Promise<List<DatasetDescription>, Throwable, Double> listDatasets() {
        final DeferredObject<List<DatasetDescription>, Throwable, Double> deferredObject =
                new DeferredObject<List<DatasetDescription>, Throwable, Double>();

        final CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();

        final HttpGet httpGet = new HttpGet(this.rootUri);
        client.start();
        client.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    final InputStream content = httpResponse.getEntity().getContent();
                    final List<DatasetDescription> datasetDescriptions = DatasetDescription.extractDatasets(content);
                    deferredObject.resolve(datasetDescriptions);
                    client.close();
                } catch (IOException e) {
                    deferredObject.reject(e);
                } catch (DatasetException e) {
                    deferredObject.reject(e);
                }
            }

            @Override
            public void failed(Exception e) {
                deferredObject.reject(e);
            }

            @Override
            public void cancelled() {
                deferredObject.reject(new Exception("Request cancelled."));
            }
        });

        return deferredObject.promise();
    }

    public Promise<DatasetDescription, Throwable, Double> getDataset(String dataset) {
        final DeferredObject<DatasetDescription, Throwable, Double> deferredObject = new DeferredObject<DatasetDescription, Throwable, Double>();

        final CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();

        final HttpGet httpGet = new HttpGet(this.rootUri.toString() + dataset);
        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json");

        client.start();
        client.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    final InputStream content = httpResponse.getEntity().getContent();
                    ObjectMapper mapper = new ObjectMapper();
                    final JsonNode jsonNode = mapper.readTree(content);
                    deferredObject.resolve(new DatasetDescription(jsonNode));
                    client.close();
                } catch (IOException e) {
                    deferredObject.reject(e);
                } catch (DatasetException e) {
                    deferredObject.reject(e);
                }
            }

            @Override
            public void failed(Exception e) {
                deferredObject.reject(e);
            }

            @Override
            public void cancelled() {
                deferredObject.reject(new Exception("Request cancelled."));
            }
        });

        return deferredObject.promise();
    }
}
