package com.bithuoche.paging;

import android.view.View;
import android.widget.TextView;

class ViewUtils {

    private final View rootView;

    private ViewUtils(View view) {
        rootView = view;
    }

    public static ViewUtils create(View view) {
        return new ViewUtils(view);
    }

    public ViewUtils setText(int id, String text) {
        View view = rootView.findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    public ViewUtils setOnClickListener(int id, View.OnClickListener onClickListener) {
        View view = rootView.findViewById(id);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        return this;
    }

    public ViewUtils setVisibility(int id, int visibility) {
        View view = rootView.findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }
}
