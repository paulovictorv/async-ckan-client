package br.wrapper.ckanclient.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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

}
