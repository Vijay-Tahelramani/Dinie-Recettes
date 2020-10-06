package com.example.dinierecettes;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;




public class HomeFragment extends Fragment {

    NavController navController;
    private Context context;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(),R.id.home_child_fragment);
        context = getActivity().getApplicationContext();

        /*-------------Tab Layout Setup---------*/
        tabLayout = view.findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){

                    case 0:
                        navController.navigate(R.id.explore_recipe_fragment);
                        break;
                    case 1:
                        navController.navigate(R.id.cuisine_Fragment);
                        break;

                    case 2:
                        navController.navigate(R.id.category_fragment);
                        break;


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.search_recipe_icon){
            Intent i = new Intent(context,SearchActivity.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
