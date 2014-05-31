package br.wrapper.ckanclient.model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Paulo on 31/05/14.
 */
public class DatasetDescriptionTest {

    private static InputStream descriptionStream;

    @BeforeClass
    public static void onStart(){

    }

    @Test
    public void shouldExtractListFromStream(){
        final List<DatasetDescription> datasetDescriptions = DatasetDescription.extractDatasets(descriptionStream);
        assertThat(datasetDescriptions).isNotEmpty();
    }
}
