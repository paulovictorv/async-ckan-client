package br.wrapper.ckanclient;

import br.wrapper.ckanclient.model.DatasetDescription;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.jdeferred.Promise;
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

        client.execute(httpGet, new DatasetListCallback(deferredObject));

        return deferredObject.promise();
    }

    public Promise<DatasetDescription, Throwable, Double> getDataset(String s) {

        return null;
    }

    private class DatasetListCallback implements FutureCallback<HttpResponse> {

        private final DeferredObject deferredObject;

        public DatasetListCallback(DeferredObject deferredObject){
            this.deferredObject = deferredObject;
        }

        @Override
        public void completed(HttpResponse httpResponse) {
            try {
                final InputStream content = httpResponse.getEntity().getContent();
                final List<DatasetDescription> datasetDescriptions = DatasetDescription.extractDatasets(content);
                deferredObject.resolve(datasetDescriptions);
            } catch (IOException e) {
                e.printStackTrace();
                deferredObject.reject(new Exception());
            }
        }

        @Override
        public void failed(Exception e) {

        }

        @Override
        public void cancelled() {

        }
    }

}
