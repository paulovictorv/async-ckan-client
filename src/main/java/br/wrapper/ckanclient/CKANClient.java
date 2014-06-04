package br.wrapper.ckanclient;

import br.wrapper.ckanclient.exceptions.DatasetException;
import br.wrapper.ckanclient.model.DatasetDescription;
import br.wrapper.ckanclient.model.HttpAsyncUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.jdeferred.DeferredManager;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
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

        HttpAsyncUtils.doGet(this.rootUri)
                .done( new DoneCallback<HttpResponse>() {
                    @Override
                    public void onDone(HttpResponse httpResponse) {
                        try {
                            final InputStream content = httpResponse.getEntity().getContent();
                            final List<DatasetDescription> datasetDescriptions = DatasetDescription.extractDatasets(content);
                            deferredObject.resolve(datasetDescriptions);
                        } catch (IOException e) {
                            deferredObject.reject(e);
                        } catch (DatasetException e) {
                            deferredObject.reject(e);
                        }
                    }
                })
                .fail( new FailCallback<Throwable>() {
                    @Override
                    public void onFail(Throwable throwable) {
                        if (throwable instanceof HttpResponseException){
                            deferredObject.reject(new DatasetException("Dataset not found"));
                        } else {
                            deferredObject.reject(throwable);
                        }
                    }
                });

        return deferredObject.promise();
    }

    public Promise<DatasetDescription, Throwable, Double> getDataset(String dataset) {
        final DeferredObject<DatasetDescription, Throwable, Double> deferredObject = new DeferredObject<DatasetDescription, Throwable, Double>();

        try {
            HttpAsyncUtils.doGet(new URI(this.rootUri.toString() + "/" + dataset))
                    .done( new DoneCallback<HttpResponse>() {
                        @Override
                        public void onDone(HttpResponse httpResponse) {
                            try {
                                final int statusCode = httpResponse.getStatusLine().getStatusCode();
                                if (statusCode != 200){
                                    deferredObject.reject(new DatasetException("Dataset not found"));
                                } else {
                                    final InputStream content = httpResponse.getEntity().getContent();
                                    ObjectMapper mapper = new ObjectMapper();
                                    final JsonNode jsonNode = mapper.readTree(content);
                                    deferredObject.resolve(new DatasetDescription(jsonNode));
                                }
                            } catch (IOException e) {
                                deferredObject.reject(e);
                            } catch (DatasetException e) {
                                deferredObject.reject(e);
                            }
                        }
                    })
                    .fail(new FailCallback<Throwable>() {
                        @Override
                        public void onFail(Throwable throwable) {
                            deferredObject.reject(throwable);
                        }
                    });

        } catch (URISyntaxException e) {
           deferredObject.reject(e);
        }


        return deferredObject.promise();
    }
}
