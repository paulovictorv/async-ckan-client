package br.wrapper.ckanclient.model;

import br.wrapper.ckanclient.exceptions.DatasetException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Paulo on 31/05/14.
 */
public class DatasetDescriptionTest {

    private static InputStream descriptionStream;
    private static JsonNode datasetDescription;

    @BeforeClass
    public static void onStart() throws IOException {
        descriptionStream = new FileInputStream(new File("ckan-client/src/test/resources/datasets.json"));
        final InputStream datasetStream = new FileInputStream(new File("ckan-client/src/test/resources/dataset-description.json"));

        ObjectMapper mapper = new ObjectMapper();
        datasetDescription = mapper.readTree(datasetStream);
    }

    @Test
    public void shouldExtractListFromStream() throws DatasetException {
        final List<DatasetDescription> datasetDescriptions = DatasetDescription.extractDatasets(descriptionStream);
        assertThat(datasetDescriptions)
                .isNotEmpty()
                .isNotNull()
                .contains(new DatasetDescription("indicadores-taxa-de-reprovaaao-ensino-madio-rede-privada"));
    }

    @Test
    public void shouldCreateFromJsonNode() throws DatasetException {
        final DatasetDescription desc = new DatasetDescription(datasetDescription);
        assertThat(desc.resources).hasSize(4);
    }
}
