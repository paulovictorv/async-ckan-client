package br.wrapper.ckanclient.model.util;

import br.wrapper.ckanclient.model.HttpAsyncUtils;
import org.apache.http.HttpResponse;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * Created by Paulo on 04/06/14.
 */
public class HttpAsyncUtilsTest {

    @Test
    public void testDoGetSuccess() throws URISyntaxException, InterruptedException {

        Promise<HttpResponse,Throwable,Double> promise = HttpAsyncUtils.doGet(new URI("http://www.uol.com.br"));

        promise.done(new DoneCallback<HttpResponse>() {
            @Override
            public void onDone(HttpResponse httpResponse) {
                try {
                    assertThat(httpResponse.getEntity().getContent()).isNotNull();
                } catch (IOException e) {
                    fail();
                }
            }
        })
        .fail(new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable throwable) {
                fail();
            }
        });

        promise.waitSafely();
    }
}
