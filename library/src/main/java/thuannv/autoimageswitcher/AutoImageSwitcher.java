package thuannv.autoimageswitcher;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import thuannv.autoimageswitcher.utils.AndroidUtilities;
import thuannv.autoimageswitcher.utils.BitmapUtils;

/**
 * @author thuannv
 * @since 03/08/2017
 */
public class AutoImageSwitcher extends ImageSwitcher {

    private static final int DEFAULT_DURATION_MILLS = 4000;

    private static final String TAG = "AutoImageSwitcher";

    private final Handler mUIHandler = new Handler();

    private List<Drawable> mImageDrawables;

    private List<Integer> mImageResIds;

    private int mCurrentImage = 0;

    private long mAnimationDelay = 0;

    private Animation mInAnimation;

    private Animation mOutAnimation;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mImageDrawables == null || mImageDrawables.isEmpty()) {
                return;
            }
            mCurrentImage = (mCurrentImage + 1) % mImageDrawables.size();
            setImageDrawable(mImageDrawables.get(mCurrentImage));
            if (mAnimationDelay > 0) {
                mUIHandler.postDelayed(this, mAnimationDelay);
            } else {
                mUIHandler.postDelayed(this, DEFAULT_DURATION_MILLS);
            }
        }
    };

    public AutoImageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int resId;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoImageSwitcher);
        try {
            mAnimationDelay = a.getInteger(R.styleable.AutoImageSwitcher_delayAnimation, DEFAULT_DURATION_MILLS);

            resId = a.getResourceId(R.styleable.AutoImageSwitcher_inAnimation, R.anim.image_switcher_in_animation);
            mInAnimation = AnimationUtils.loadAnimation(context, resId);

            resId = a.getResourceId(R.styleable.AutoImageSwitcher_outAnimation, R.anim.image_switcher_out_animation);
            mOutAnimation = AnimationUtils.loadAnimation(context, resId);

            initImages(context, a);

            setFactory(defaultFactory());

            setImageDrawable(mImageDrawables.get(0));
            setInAnimation(mInAnimation);
            setOutAnimation(mOutAnimation);
            mUIHandler.postDelayed(mRunnable, mAnimationDelay);
        } finally {
            a.recycle();
        }
    }

    private ViewSwitcher.ViewFactory defaultFactory() {
        return new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageSwitcher.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                return imageView;
            }
        };
    }

    private void initImages(Context context, TypedArray a) {
        int resourceId = a.getResourceId(R.styleable.AutoImageSwitcher_images, -1);
        if (resourceId == -1) {
            return;
        }
        Resources resources = getResources();
        TypedArray imageArray = resources.obtainTypedArray(resourceId);
        try {
            int length = imageArray.length();
            mImageDrawables = new ArrayList<>(length);
            Point size = AndroidUtilities.getDisplaySize(context);
            for (int i = 0; i < length; i++) {
                resourceId = imageArray.getResourceId(i, -1);
                if (resourceId != -1) {
                    try {
                        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(resources, resourceId, size.x, size.y);
                        mImageDrawables.add(new BitmapDrawable(resources, bitmap));
                    } catch (Exception e) {
                        //ignored.
                    }
                }
            }
        } catch (Exception e) {
            mImageDrawables = null;
        } finally {
            imageArray.recycle();
        }
    }
}
