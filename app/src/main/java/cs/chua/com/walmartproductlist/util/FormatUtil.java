package cs.chua.com.walmartproductlist.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by christopherchua on 10/7/17.
 */

public class FormatUtil {
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (source == null || source.trim().length() == 0) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
