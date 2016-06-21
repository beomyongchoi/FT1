package kr.co.fintalk.fintalkone;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by beomyong on 6/21/16.
 */
public class CustomStartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSansKR-Medium-Hestia.otf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSansKR-Medium-Hestia.otf"));
    }
}
