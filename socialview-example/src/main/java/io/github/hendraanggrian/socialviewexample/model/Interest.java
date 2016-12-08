package io.github.hendraanggrian.socialviewexample.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.hendraanggrian.socialview.Hashtagable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Interest implements Hashtagable {

    private String content;
    private int count;

    public Interest(String content, int count) {
        this.content = content;
        this.count = count;
    }

    @NonNull
    @Override
    public String getHashtag() {
        return content;
    }

    @Nullable
    @Override
    public Integer getHashtagCount() {
        return count;
    }
}