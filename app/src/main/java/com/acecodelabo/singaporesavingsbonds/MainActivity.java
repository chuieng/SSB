package com.acecodelabo.singaporesavingsbonds;

import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
/**
 * Created by Chui Eng on 11/3/2017.
 */
public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager homeMgr = getFragmentManager();
                    homeMgr.beginTransaction().replace(R.id.contentLayout,homeFragment,homeFragment.getTag()).commit();

                    return true;
                case R.id.navigation_historyGraph:
                    HistoryGraphFragment historyGraphFragment = new HistoryGraphFragment();
                    FragmentManager historyGraphMgr = getFragmentManager();
                    historyGraphMgr.beginTransaction().replace(R.id.contentLayout,historyGraphFragment,historyGraphFragment.getTag()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager homeMgr = getFragmentManager();
        homeMgr.beginTransaction().replace(R.id.contentLayout,homeFragment,homeFragment.getTag()).commit();

    }

}
