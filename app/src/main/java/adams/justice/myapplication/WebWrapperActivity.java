package adams.justice.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Optional;
import java.util.function.Consumer;

public class WebWrapperActivity extends AppCompatActivity {

    //The word to check against in the URL's
    private static final String APP_ROOT_STRING = "principal";
    private static final String HOMEPAGE_URL = "https://www.principal.com/";

    private WebView browserView;
    private ProgressDialog mProgress;
    private String mCurrentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Removes the title bar in the application
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_wrapper);
        setUpToolbar();
        setUpBrowserView();

        //Load Website
        browserView.loadUrl(HOMEPAGE_URL);
        mCurrentUrl = HOMEPAGE_URL;
    }

    private void setUpToolbar() {
        Optional<ActionBar> supportActionBar = Optional.of(getSupportActionBar());
        supportActionBar.ifPresent(new Consumer<ActionBar>() {
            @Override
            public void accept(ActionBar actionBar) {
                actionBar.setIcon(R.drawable.toolbar_icon);
                actionBar.setTitle("   Advisor Match");
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        });
    }

    private void setUpBrowserView() {
        browserView = (WebView)findViewById(R.id.webkit);

        //Enable Javascript
        browserView.getSettings().setJavaScriptEnabled(true);
        browserView.setWebViewClient(new MyWebViewClient());
        browserView.setVerticalScrollBarEnabled(false);
        browserView.setHorizontalScrollBarEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        boolean isHomePage = mCurrentUrl.equals(HOMEPAGE_URL);
        if (!isHomePage) {
            browserView.goBack();
        } else {
            //Close the app
            finish();
        }

        return super.onNavigateUp();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mCurrentUrl = url;

            if (!url.toLowerCase().contains(APP_ROOT_STRING.toLowerCase())) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgress = new ProgressDialog(WebWrapperActivity.this, R.style.MyTheme);
            mProgress.show();

            mCurrentUrl = url;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgress.hide();

            super.onPageFinished(view, url);
        }
    }
}
