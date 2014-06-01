package br.wrapper.ckanclient.model;

import br.wrapper.ckanclient.model.util.MimeTypeManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Paulo on 31/05/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CKANResource {

    @JsonProperty("id")
    public String id;


    @JsonProperty("resource-group-id")
    public String resourceId;

    @JsonProperty("package-id")
    public String packageId;

    @JsonProperty("description")
    public String description;

    @JsonProperty("format")
    public String format;
    @JsonProperty("mimetype_inner")
    public String mimeType;

    @JsonCreator
    public CKANResource(@JsonProperty("id") String id,
                        @JsonProperty("resource_group_id") String resourceId,
                        @JsonProperty("package_id") String packageId,
                        @JsonProperty("description") String description,
                        @JsonProperty("format") String format,
                        @JsonProperty("mimetype_inner") String mimeType){

        this.id = id;
        this.resourceId = resourceId;
        this.packageId = packageId;
        this.description = description;
        this.format = format;
        this.mimeType = parseMime(mimeType);
    }

    private String parseMime(String mimeType) {
        if (mimeType == null){
            return MimeTypeManager.formatToMimeType(this.format);
        } else {
            return mimeType;
        }
    }

    @Override
    public String toString() {
        return "CKANResource{" +
                "id='" + id + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", packageId='" + packageId + '\'' +
                ", description='" + description + '\'' +
                ", format='" + format + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
