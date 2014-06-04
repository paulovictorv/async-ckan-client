package br.wrapper.ckanclient.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Paulo on 01/06/14.
 */
public class CKANResourceTest {

    @Test
    public void shouldCreateResourceWithMimeType() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final CKANResource resource = mapper.readValue(new File("ckan-client/src/test/resources/dataset-resource.json"), CKANResource.class);

        assertThat(resource.mimeType).isEqualTo("application/json");
    }

    @Test
    public void shouldObtainResourceWithSpecifiedMimeType() throws IOException, InterruptedException {
        final ObjectMapper mapper = new ObjectMapper();
        final CKANResource resource = mapper.readValue(new File("ckan-client/src/test/resources/dataset-resource.json"), CKANResource.class);

        Promise<InputStream,Throwable,Double> promise = resource.getResource();

        promise.done(new DoneCallback<InputStream>() {
            @Override
            public void onDone(InputStream stream) {
                assertThat(stream).isNotNull();
            }
        });

        promise.waitSafely();
    }

}
