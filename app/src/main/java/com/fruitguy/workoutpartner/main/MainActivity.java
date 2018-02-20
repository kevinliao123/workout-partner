package com.fruitguy.workoutpartner.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.authentication.AuthenticationActivity;
import com.fruitguy.workoutpartner.chat.ChatFragment;
import com.fruitguy.workoutpartner.friendlist.FriendListFragment;
import com.fruitguy.workoutpartner.request.RequestFragment;
import com.fruitguy.workoutpartner.search.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainPageContract.View{

    @BindView(R.id.home_page_tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.function_tab)
    TabLayout mTabLayout;

    MainPageContract.Presenter mMainPagePresenter;

    private int[] IconResID = {R.mipmap.ic_chest_bench,R.mipmap.ic_launcher,R.mipmap.ic_add_partner, R.mipmap.ic_launcher_round};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupViewPager();

        mMainPagePresenter = new MainPagePresenter(this);
        if(!mMainPagePresenter.doesUserExist()) {
            Intent loginIntent = new Intent(MainActivity.this, AuthenticationActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                return true;
            case R.id.settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager() {
        List<Fragment> list = new ArrayList<>();
        list.add(new FriendListFragment());
        list.add(new SearchFragment());
        list.add(new ChatFragment());
        list.add(new RequestFragment());
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager(),list);
        mViewPager.setAdapter(sectionPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setTabLayoutIcon();
    }

    public void setTabLayoutIcon(){
        for(int i =0; i < IconResID.length;i++){
            mTabLayout.getTabAt(i).setIcon(IconResID[i]);
        }

    }

    @Override
    public void setPresenter(MainPageContract.Presenter presenter) {
        mMainPagePresenter = presenter;
    }
}
