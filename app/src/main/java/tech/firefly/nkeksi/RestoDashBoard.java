package tech.firefly.nkeksi;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RestoDashBoard extends AppCompatActivity {

    ViewPager restoSlide;
    TabLayout restoTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_dash_board);

        restoSlide = (ViewPager) findViewById(R.id.resto_slide);
        restoTab = (TabLayout) findViewById(R.id.detail_menu_tab);

        restoTab.addTab(restoTab.newTab().setText("Details"));
        restoTab.addTab(restoTab.newTab().setText("Menu"));
        restoTab.addTab(restoTab.newTab().setText("Review"));

        restoSlide.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(restoTab));
        restoTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                restoSlide.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        restoSlide.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));


    }

    public class TabsPagerAdapter extends FragmentPagerAdapter{

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new fragDetails();
                case 1:
                    return new fragMenu();
                case 2:
                    return new fragReview();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }



}
