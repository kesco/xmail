package com.kescoode.adk.net;

import com.kescoode.adk.net.volley.TimeoutError;
import com.kescoode.adk.net.volley.VolleyError;
import com.kescoode.adk.net.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by kesco on 15/3/11.
 */
public class NetError extends VolleyError {
    private final VolleyError error;

    /* package */ NetError(VolleyError error) {
        this.error = error;
    }

    public int statusCode() {
        if (error.networkResponse == null) {
            if (error instanceof TimeoutError) {
                return 407;
            }
            return 0;
        }
        return error.networkResponse.statusCode;
    }

    @Override
    public String toString() {
        if (error.networkResponse == null) {
            String message = error.getMessage();
            if (message == null) {
                return "";
            } else {
                return message;
            }
        }
        String parsed;
        try {
            parsed = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(error.networkResponse.data);
        }
        return parsed;
    }

}
