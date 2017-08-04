package thuannv.autoimageswitcher.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * @author thuannv
 * @since 04/08/2017
 */
public final class AndroidUtilities {
    private AndroidUtilities() {}

    private static final Point sDisplaySize = new Point(0, 0);

    public static Point getDisplaySize(Context context) {
        if (sDisplaySize.x == 0 && sDisplaySize.y == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getSize(sDisplaySize);
        }
        return sDisplaySize;
    }
}
