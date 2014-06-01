package br.wrapper.ckanclient.model.util;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Paulo on 01/06/14.
 */
public class MimeTypeManagerTest {

    @Test
    public void shouldMapToJSON(){
        final String s = MimeTypeManager.formatToMimeType(".json");
        assertThat(s).isEqualTo("application/json");
    }

    @Test
    public void shouldMapToXML(){
        final String s = MimeTypeManager.formatToMimeType(".xml");
        assertThat(s).isEqualTo("application/xml");
    }

    @Test
    public void shouldMapToCSV(){
        final String s = MimeTypeManager.formatToMimeType(".csv");
        assertThat(s).isEqualTo("application/csv");
    }
    @Test
    public void shouldMapToXLS(){
        final String s = MimeTypeManager.formatToMimeType(".xls");
        assertThat(s).isEqualTo("application/vnd.ms-excel");
    }

    @Test
    public void shouldMapToDefault(){
        final String s = MimeTypeManager.formatToMimeType(".pdf");
        assertThat(s).isEqualTo("application/octet-stream");
    }
}
