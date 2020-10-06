package com.example.dinierecettes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class EditRecipeActivity extends AppCompatActivity {

    public Toolbar era_toolbar;
    public NavController EraNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        era_toolbar = findViewById(R.id.era_toolbar);
        era_toolbar.setTitle("Edit Recipe");
        setSupportActionBar(era_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EraNavController = Navigation.findNavController(this, R.id.era_host_fragment);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(EraNavController.getCurrentDestination().getId()== R.id.editRecipe1Fragment){
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        else {
            EraNavController.popBackStack();
        }
    }
}
