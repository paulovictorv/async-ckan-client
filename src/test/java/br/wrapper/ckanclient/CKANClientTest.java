package br.wrapper.ckanclient;

import br.wrapper.ckanclient.model.DatasetDescription;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * Created by Paulo on 31/05/14.
 */
public class CKANClientTest {

    private static final String CKAN_URL = "http://catalogo.seplande.al.gov.br/api/rest/dataset";

    @Test
    public void shouldListAvailableDatasetsString() throws URISyntaxException, InterruptedException {
        CKANClient client = new CKANClient(CKAN_URL);
        Promise promiseOfList = client.listDatasets();
        final List<DatasetDescription> descriptions = new ArrayList<DatasetDescription>();


        promiseOfList.then(new DoneCallback<List<DatasetDescription>>() {
            @Override
            public void onDone(List<DatasetDescription> list) {
                assertThat(list).isNotEmpty();
                descriptions.addAll(list);
            }
        });

        promiseOfList.waitSafely();
        assertThat(descriptions).isNotEmpty();
    }

    @Test
    public void shouldListAvailableDatasetsURI() throws URISyntaxException, InterruptedException {
        CKANClient client = new CKANClient(new URI(CKAN_URL));
        Promise promiseOfList = client.listDatasets();
        final List<DatasetDescription> descriptions = new ArrayList<DatasetDescription>();

        promiseOfList.then(new DoneCallback<List<DatasetDescription>>() {
            @Override
            public void onDone(List<DatasetDescription> list) {
                assertThat(list).isNotEmpty();
                descriptions.addAll(list);
            }
        });

        promiseOfList.waitSafely();
        assertThat(descriptions).isNotNull();
    }

    @Test
    public void shouldObtainDataset() throws URISyntaxException, InterruptedException {
        CKANClient client = new CKANClient(CKAN_URL);
        Promise promiseOfDatasetDescription = client.getDataset("indicadores-taxa-de-reprovaaao-ensino-madio-rede-privada");


        promiseOfDatasetDescription
            .then(new DoneCallback<DatasetDescription>() {
                @Override
                public void onDone(DatasetDescription datasetDescription) {
                assertThat(datasetDescription).isNotNull();
                    System.out.println(datasetDescription.resources);
                }
            }, new FailCallback() {
                  @Override
                  public void onFail(Object o) {
                      fail();
                  }
              });

        promiseOfDatasetDescription.waitSafely();
    }

}
