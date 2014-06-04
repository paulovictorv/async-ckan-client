package br.wrapper.ckanclient.model;

import br.wrapper.ckanclient.model.util.MimeTypeManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.HttpResponse;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

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

    public URI resourceUrl;

    @JsonCreator
    public CKANResource(@JsonProperty("id") String id,
                        @JsonProperty("resource_group_id") String resourceId,
                        @JsonProperty("package_id") String packageId,
                        @JsonProperty("description") String description,
                        @JsonProperty("format") String format,
                        @JsonProperty("mimetype_inner") String mimeType,
                        @JsonProperty("url") String url) throws URISyntaxException {

        this.id = id;
        this.resourceId = resourceId;
        this.packageId = packageId;
        this.description = description;
        this.format = format;
        this.mimeType = parseMime(mimeType);
        this.resourceUrl = new URI(url);
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
                ", resourceUrl=" + resourceUrl +
                '}';
    }

    public Promise<InputStream, Throwable, Double> getResource() {
        final DeferredObject<InputStream, Throwable, Double> deferred = new DeferredObject<InputStream, Throwable, Double>();

        Promise<HttpResponse, Throwable, Double> promise = HttpAsyncUtils.doGet(this.resourceUrl);

        promise.done(new DoneCallback<HttpResponse>() {
            @Override
            public void onDone(HttpResponse httpResponse) {
                try {
                    deferred.resolve(httpResponse.getEntity().getContent());
                } catch (IOException e) {
                    deferred.reject(e);
                }
            }
        }).fail(new FailCallback<Throwable>() {
                @Override
                public void onFail(Throwable throwable) {
                    deferred.reject(throwable);     
                }
       });

        return deferred.promise();
    }
}
