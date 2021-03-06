
package com.sdust.zhihudaily.mainpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.sdust.zhihudaily.R;
import com.sdust.zhihudaily.base.BaseAppCompatActivity;
import com.sdust.zhihudaily.base.BaseFragment;
import com.sdust.zhihudaily.interfaces.NavigationDrawerCallbacks;
import com.sdust.zhihudaily.mainpage.daily.DailyPresenter;
import com.sdust.zhihudaily.mainpage.daily.DailyStoriesFragment;
import com.sdust.zhihudaily.mainpage.navigation.NavigationFragment;
import com.sdust.zhihudaily.mainpage.navigation.NavigationPresenter;
import com.sdust.zhihudaily.mainpage.theme.ThemePresenter;
import com.sdust.zhihudaily.mainpage.theme.ThemeStoriesFragment;

/**
 * Created by Kevin on 2016/5/25.
 * MainActivity
 */
public class MainActivity extends BaseAppCompatActivity implements NavigationDrawerCallbacks {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationFragment mNavigationFragment;
    private CharSequence mTitle = "";
    private int lastPosition = 0;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDrawer();
        if (savedInstanceState == null) {
            mNavigationFragment.selectItem(NavigationFragment.getDefaultNavDrawerItem());
        }
    }

    private void setUpDrawer() {
        mNavigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mNavigationFragment.setup(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mActionBarToolbar);
        new NavigationPresenter(mNavigationFragment);
        mTitle = getTitle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navigation_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment lastFragment = fm.findFragmentByTag(getTag(lastPosition));
        if (lastFragment != null) {
            ft.detach(lastFragment);
        }

        Fragment fragment = fm.findFragmentByTag(getTag(position));
        if (fragment == null) {
            fragment = getFragmentItem(position);
            ft.add(R.id.container, fragment, getTag(position));
        } else {
            ft.attach(fragment);
        }

        ft.commit();
        lastPosition = position;

        mTitle = mNavigationFragment.getTitle(position);
        setActionBar();

    }
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);//显示title,不设置默认为true
        actionBar.setTitle(mTitle);
    }

    private Fragment getFragmentItem(int position) {
        Fragment fragment = BaseFragment.newInstance(position, mNavigationFragment.getSectionId(position));
        if(fragment instanceof DailyStoriesFragment) {
            new DailyPresenter((DailyStoriesFragment)fragment);
        } else {
            new ThemePresenter((ThemeStoriesFragment)fragment);
        }
        return fragment;
    }

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (mNavigationFragment.isDrawerOpen()) {
            mNavigationFragment.closeDrawer();
        } else {
            if (mNavigationFragment.getCurrentSelectedPosition() != NavigationFragment.getDefaultNavDrawerItem()) {
                mNavigationFragment.selectItem(NavigationFragment.getDefaultNavDrawerItem());
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    super.onBackPressed();
                }

            }
        }
    }


    private String getTag(int position) {
        switch (position) {
            case 0:
                return DailyStoriesFragment.TAG;
            default:
                return ThemeStoriesFragment.TAG + position;
        }
    }

}
