package com.hendraanggrian.socialview;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * https://github.com/HendraAnggrian/socialview/issues/8
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4.class)
public class RegexTest {

    @Test
    public void englishNames() throws Exception {
        test(new String[]{
                "Manana",
                "CreacionDivina",
                "TuVendras",
                "CuatroArticulos",
                "MasNadaQueda",
                "SeFueEste",
                "EsteNoEra",
                "Name"
        }, '#', SocialViewImpl.PATTERN_HASHTAG);
    }

    @Test
    public void spanishNames() throws Exception {
        test(new String[]{
                "Mañana",
                "CreaciónDivina",
                "TúVendrás",
                "CuatroArtículos",
                "MásNadaQueda",
                "SeFueÉste",
                "ÉsteNoEra",
                "Ñame"
        }, '@', SocialViewImpl.PATTERN_MENTION);
    }

    private void test(@NonNull String[] names, char prefix, @NonNull Pattern pattern) {
        Object[] namesWithPrefix = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            namesWithPrefix[i] = prefix + names[i];
        }
        List<String> list = SocialViewImpl.listOf(Arrays.toString(namesWithPrefix), pattern);
        assertEquals(list.size(), names.length);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), names[i]);
        }
    }
}