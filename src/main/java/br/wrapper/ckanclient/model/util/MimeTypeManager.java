package br.wrapper.ckanclient.model.util;

/**
 * Created by Paulo on 01/06/14.
 */
public class MimeTypeManager {
    public static String formatToMimeType(String mimeType) {
        if (mimeType.equals(".json"))
            return "application/json";
        else if (mimeType.equals(".xml"))
            return "application/xml";
        else if (mimeType.equals(".csv"))
            return "application/csv";
        else if (mimeType.equals(".xls"))
            return "application/vnd.ms-excel";

        return "application/octet-stream";
    }
}
