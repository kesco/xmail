package com.kescoode.adk.net;

import com.kescoode.adk.net.volley.*;
import com.kescoode.adk.net.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by kesco on 15/3/3.
 */
/* package */ class BaseRequest extends Request<Object> {
    protected String contentType = "application/x-www-form-urlencoded";
    protected String encoding = "utf-8";
    protected Map<String, String> headers = Collections.emptyMap();
    protected Map<String, String> params = Collections.emptyMap();
    protected JSONObject json = null;

    private Response.Listener<Object> listener;

    public BaseRequest(int method, String url, Response.Listener<Object> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.listener = listener;
    }

    @Override
    protected String getParamsEncoding() {
        return encoding;
    }

    @Override
    public String getBodyContentType() {
        return contentType + "; charset=" + getParamsEncoding();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (null != json) {
            return json.toString().getBytes();
        } else {
            return super.getBody();
        }
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public void setContentType(String type) {
        this.contentType = type;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        // TODO: 回调以后要重构，加上GSON解析，现在写的不够好
        Class<?> responseType = ((DoneListener) listener).getType();
        String parsed;
        if (String.class == responseType) {
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
            return Response.success((Object) parsed, HttpHeaderParser.parseCacheHeaders(response));
        } else if (JSONObject.class == responseType) {
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success((Object) new JSONObject(parsed),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
//        } else if (JSONArray.class == responseType) {
        } else {
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success((Object) new JSONArray(parsed),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }
}
