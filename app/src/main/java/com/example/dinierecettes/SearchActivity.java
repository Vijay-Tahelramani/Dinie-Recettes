package com.example.dinierecettes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar search_toolbar;
    private ImageButton search_back_btn;
    public static TextInputLayout search_recipe_layout;
    //private NavController SearchNavController;
    private TabLayout searchTabLayout;
    private ViewPager search_view_pager;
    private ViewPageAdapter viewPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*-------- App Bar Configuration-------*/
        search_toolbar = findViewById(R.id.search_toolbar);
        search_toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(search_toolbar);
        search_back_btn = findViewById(R.id.search_back_btn);
        search_recipe_layout = findViewById(R.id.search_recipe_layout);

        search_recipe_layout.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        search_back_btn.setOnClickListener(this);

        //SearchNavController = Navigation.findNavController(this,R.id.search_host_frahment);

        /*-------------Tab Layout Setup---------*/
        searchTabLayout = findViewById(R.id.search_tabs);
        search_view_pager = findViewById(R.id.search_view_pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,2);
        search_view_pager.setAdapter(viewPageAdapter);
        search_view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchTabLayout));
        searchTabLayout.setupWithViewPager(search_view_pager);
        searchTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                search_recipe_layout.getEditText().setText("");
                search_recipe_layout.setError(null);
                search_recipe_layout.setErrorEnabled(false);
                search_view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    /*------------ App Bar On Back icon pressed action-----------*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

    }

    @Override
    public void onClick(View view) {
        if(view==search_back_btn){
            HideKeyboard();
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        HideKeyboard();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HideKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HideKeyboard();
    }

    private void HideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
