package kr.co.fintalk.fintalkone;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by BeomyongChoi on 6/21/16.
 */
public class FTApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 커스텀 폰트 적용을 위한 외부 라이브러리를 통해 Normal, Bold 상태의 폰트 세팅
         */
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSansKR-Medium-Hestia.otf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSansKR-Bold-Hestia.otf"));
    }
}
