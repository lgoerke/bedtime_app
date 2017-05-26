package applab.bedtimeapp.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {
    private static final String BASE_URL = "http://applab.ai.ru.nl:8081";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static boolean wasInit = false;

    private static void init() {
        client.addHeader("Content-Type", "application/json");
        wasInit = true;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (!wasInit){
            init();
        }
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }


    public static void post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        if (!wasInit){
            init();
        }
        client.post(context,getAbsoluteUrl(url),entity,contentType,responseHandler);

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (!wasInit){
            init();
        }
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}