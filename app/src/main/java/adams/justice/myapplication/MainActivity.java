package adams.justice.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    WebView browserView;
    ProgressDialog mProgress;
    String mCurrentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Removes the title bar in the application
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpBrowserView();

        //The website which is loaded to the webview
        browserView.loadUrl("https://justiceadamsuni.github.io/index.html"); // ToDo: changeUrl
        mCurrentUrl = "https://justiceadamsuni.github.io/index.html";  // ToDo: changeUrl
    }

    private void setUpToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.toolbar_icon);
        getSupportActionBar().setTitle("   Advisor Match");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpBrowserView() {
        browserView = (WebView)findViewById(R.id.webkit);

        //Enable Javascripts
        browserView.getSettings().setJavaScriptEnabled(true);

        browserView.setWebViewClient(new MyWebViewClient());
        browserView.setVerticalScrollBarEnabled(false);
        browserView.setHorizontalScrollBarEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        boolean isHomePage = mCurrentUrl.contains("justiceadamsuni.github.io/index") || mCurrentUrl.contains("cornerstone2k16");
        Log.d("LOGZ : up", Boolean.toString(isHomePage) + "URL: " + mCurrentUrl);
        if (!isHomePage) { // ToDo: changeUrl
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
            mCurrentUrl = url; // ToDo: changeUrl

            Log.d("LOGZ", url);
            if (!url.contains("justiceadamsuni.github.io")) { // ToDo: changeUrl
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgress = new ProgressDialog(MainActivity.this, R.style.MyTheme);
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
