package com.hendraanggrian.socialview;

import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Person {

    @NonNull public final String name;

    public Person(@NonNull String name) {
        this.name = name;
    }
}