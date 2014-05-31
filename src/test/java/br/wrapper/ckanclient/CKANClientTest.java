package br.wrapper.ckanclient;

import br.wrapper.ckanclient.model.DatasetDescription;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Paulo on 31/05/14.
 */
public class CKANClientTest {

    private static final String CKAN_URL = "http://catalogo.seplande.al.gov.br/api/rest/dataset";

    @Test
    public void shouldListAvailableDatasetsString() throws URISyntaxException {
        CKANClient client = new CKANClient(CKAN_URL);
        Promise promiseOfList = client.listDatasets();

        promiseOfList.then(new DoneCallback<List<DatasetDescription>>() {
            @Override
            public void onDone(List<DatasetDescription> list) {
                assertThat(list).isNotEmpty();
            }
        });
    }

    @Test
    public void shouldListAvailableDatasetsURI() throws URISyntaxException {
        CKANClient client = new CKANClient(new URI(CKAN_URL));
        Promise promiseOfList = client.listDatasets();

        promiseOfList.then(new DoneCallback<List<DatasetDescription>>() {
            @Override
            public void onDone(List<DatasetDescription> list) {
                assertThat(list).isNotEmpty();
            }
        });
    }

    @Test
    public void shouldObtainDataset() throws URISyntaxException {
        CKANClient client = new CKANClient(CKAN_URL);
        Promise promiseOfDatasetDescription = client.getDataset("");

        promiseOfDatasetDescription
            .then(new DoneCallback<DatasetDescription>() {
                @Override
                public void onDone(DatasetDescription datasetDescription) {
                    assertThat(datasetDescription).isNotNull();
                }
            });
    }

}
