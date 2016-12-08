package io.github.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class Label {

    @NonNull final String label;

    Label(@NonNull String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}