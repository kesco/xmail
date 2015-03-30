package com.kescoode.xmail.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.kescoode.xmail.domain.internal.HtmlConverter;
import com.kescoode.xmail.domain.internal.HtmlSanitizer;

/**
 * 邮件使用的WebView
 * 
 * @author Jinsen Lin
 */
public class MailWebView extends WebView {
    
    public MailWebView(Context context) {
        super(context);
        
        initView(context);
    }

    public MailWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initView(context);
    }

    public MailWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MailWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        
        initView(context);
    }
    
    private void initView(Context context) {
        WebSettings webSettings = getSettings();

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION_CODES.HONEYCOMB < Build.VERSION.SDK_INT) {
            webSettings.setDisplayZoomControls(false);
        }
    }
    
    public void loadLocalData(String data) {
        String content = "<html><head><meta name=\"viewport\" content=\"width=device-width\"/>";
        content += HtmlConverter.cssStylePre();
        content += "</head><body>" + data + "</body></html>";

        String sanitizedContent = HtmlSanitizer.sanitize(content);
        loadDataWithBaseURL("http://", sanitizedContent, "text/html", "utf-8", null);
    }

    /**
     * Text自适应屏幕
     * 
     * 注意，只在4.4以上版本有效
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void textAutoSize(boolean useAlgorithm) {
        WebSettings settings = getSettings();
        WebSettings.LayoutAlgorithm layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL;
        if(useAlgorithm) {
            layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING;
        }

        settings.setLayoutAlgorithm(layoutAlgorithm);
    }
    
    public void blockNetworkData(boolean shouldBlockNetworkData) {
        getSettings().setBlockNetworkLoads(shouldBlockNetworkData);
    }
    
}
