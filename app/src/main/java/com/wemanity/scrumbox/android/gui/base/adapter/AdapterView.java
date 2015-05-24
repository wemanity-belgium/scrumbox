package com.wemanity.scrumbox.android.gui.base.adapter;

public class AdapterView {
    private final int viewId;
    private final String propertyName;

    public AdapterView(int viewId, String propertyName) {
        this.viewId = viewId;
        this.propertyName = propertyName;
    }

    private AdapterView(Builder builder) {
        viewId = builder.viewId;
        propertyName = builder.propertyName;
    }

    public int getViewId() {
        return viewId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public static final class Builder {
        private final int viewId;
        private final String propertyName;

        public Builder(int viewId, String propertyName) {
            this.viewId = viewId;
            this.propertyName = propertyName;
        }

        public AdapterView build() {
            return new AdapterView(this);
        }
    }
}
