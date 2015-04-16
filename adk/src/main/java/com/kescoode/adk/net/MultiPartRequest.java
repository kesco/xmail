package com.kescoode.adk.net;

import android.webkit.MimeTypeMap;
import com.google.common.io.Files;
import com.kescoode.adk.net.volley.AuthFailureError;
import com.kescoode.adk.net.volley.Response;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持Multi-Part请求的Request
 *
 * @author Kesco Lin
 */
/* package */ class MultiPartRequest extends BaseRequest {
    private static final String CRLF = "\r\n";
    private static final String TWOHYPHENS = "--";
    private static final String BOUNDARY = "--WebKitFormBoundary7MA4YWxkTrZu0gW";
    private static final String CONTENT_DESCRIPTITION = "Content-Disposition: form-data; name=";
    private static final String CONTENT_TPYE = "Content-Type: ";

    private Map<String, File> files = new HashMap<>();


    public MultiPartRequest(int method, String url, Response.Listener<Object> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        // TODO: 可以改为BaseRequest的实现方法
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MimeTypeMap mimeMap = MimeTypeMap.getSingleton();

        try {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                File file = entry.getValue();
                output.write((TWOHYPHENS + BOUNDARY + CRLF).getBytes());
                output.write((CONTENT_DESCRIPTITION + "\"" + entry.getKey() + "\"; filename=\"" + file.getName() + "\"" + CRLF).getBytes());
                String mime = mimeMap.getMimeTypeFromExtension(file.getName().split("\\.")[1]);
                output.write((CONTENT_TPYE + mime + CRLF).getBytes());
                output.write(CRLF.getBytes());
                output.write(FileUtils.readFileToByteArray(file));
                output.write(CRLF.getBytes());
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                output.write((TWOHYPHENS + BOUNDARY + CRLF).getBytes());
                output.write((CONTENT_DESCRIPTITION + "\"" + entry.getKey() + "\"" + CRLF).getBytes());
                output.write(CRLF.getBytes());
                output.write(entry.getValue().getBytes());
                output.write(CRLF.getBytes());
            }
            output.write((TWOHYPHENS + BOUNDARY + TWOHYPHENS + CRLF).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

}
