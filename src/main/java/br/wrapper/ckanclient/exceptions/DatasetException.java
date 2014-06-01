package br.wrapper.ckanclient.exceptions;

import java.io.IOException;

/**
 * Created by Paulo on 31/05/14.
 */
public class DatasetException extends Throwable {
    public DatasetException(String s, IOException e) {
        super(s,e);
    }

    public DatasetException(String s) {
        super(s);
    }
}
