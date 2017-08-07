package thuannv.autoimageswitcher.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author thuannv
 * @since 04/08/2017
 */
public class LinkTextView extends android.support.v7.widget.AppCompatTextView {

    private LinkClickListener mListener;

    public LinkTextView(Context context) {
        super(context);
        init(context, null);
    }

    public LinkTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinkTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int[] array = new int[]{android.R.attr.text};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, array);
        try {
            String text = typedArray.getString(0);
            setTextInternal(text);
        } finally {
            typedArray.recycle();
        }
    }

    protected void setTextInternal(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            super.setText(text);
        } else {
            super.setText(Html.fromHtml(text.toString()));
            setMovementMethod(LinkMovementMethod.getInstance());
            Spannable spannable = (Spannable) super.getText();

            int spanStart, spanEnd;
            URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
            for (URLSpan urlSpan : spans) {
                LinkSpan linkSpan = new LinkSpan(urlSpan.getURL());
                spanStart = spannable.getSpanStart(urlSpan);
                spanEnd = spannable.getSpanEnd(urlSpan);
                spannable.setSpan(linkSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.removeSpan(urlSpan);
            }
        }
    }

    public void setText(String text) {
        setTextInternal(text);
    }

    public void setLinkClickListener(LinkClickListener listener) {
        mListener = listener;
    }

    /**
     * OnLinkClick
     */
    public interface LinkClickListener {
        void onClick(String url);
    }

    /**
     * LinkSpan
     */
    private class LinkSpan extends URLSpan {

        public LinkSpan(String url) {
            super(url);
        }

        public LinkSpan(Parcel source) {
            super(source);
        }

        @Override
        public void onClick(View widget) {
            String url = getURL();
            if (mListener != null) {
                mListener.onClick(url);
            }
        }

        public final Creator<LinkSpan> CREATOR = new Creator<LinkSpan>() {

            @Override
            public LinkSpan createFromParcel(Parcel source) {
                return new LinkSpan(source);
            }

            @Override
            public LinkSpan[] newArray(int size) {
                return new LinkSpan[size];
            }
        };
    }
}
