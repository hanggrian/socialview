package com.example.socialview;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tablayout) TabLayout tabLayout;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return new TextViewFragment();
            else if (position == 1) return new EditTextFragment();
            else if (position == 2) return new SuggestionDefaultFragment();
            else return new SuggestionCustomFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "TextView";
            else if (position == 1) return "EditText";
            else if (position == 2) return "Suggestion default";
            else return "Suggestion custom";
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}