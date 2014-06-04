package br.wrapper.ckanclient.model;

import br.wrapper.ckanclient.exceptions.DatasetException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 31/05/14.
 */
public class DatasetDescription {



    public static List<DatasetDescription> extractDatasets(InputStream content) throws DatasetException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            final List<String> datasetNames = mapper.readValue(content, new TypeReference<List<String>>() {
            });

            List<DatasetDescription> descriptions = new ArrayList<DatasetDescription>();
            for (String dtName : datasetNames) {
                descriptions.add(new DatasetDescription(dtName));
            }
            return descriptions;
        } catch (IOException e) {
            throw new DatasetException("Invalid dataset", e);
        }
    }


    public String datasetName;

    public List<CKANResource> resources;

    public DatasetDescription(String dtName) {
        datasetName = dtName;
    }

    public DatasetDescription(JsonNode jsonNode) throws DatasetException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode resourcesNode = jsonNode.get("resources");
            this.resources = mapper.readValue(resourcesNode.toString(), new TypeReference<List<CKANResource>>() {});
        } catch (IOException e) {
            throw new DatasetException("Impossible to parse value", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatasetDescription)) return false;

        DatasetDescription that = (DatasetDescription) o;

        if (datasetName != null ? !datasetName.equals(that.datasetName) : that.datasetName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return datasetName != null ? datasetName.hashCode() : 0;
    }

    public CKANResource getResourceWithMimeType(String s) {
        for (CKANResource resource : resources) {
            if (resource.mimeType.equals(s))
                return resource;
        }
        return null;
    }
}
