package com.example.dinierecettes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Add_Ingre_Quantity_Fragment extends Fragment implements View.OnClickListener{

    public NavController HomeNavController;
    private Context context;
    private ArrayList<Ingredients_List> selected_items_list;
    private ArrayList<Ingredients_Quantity_List> final_ingredients_quantity_list;
    private ConstraintLayout add_quantity_error_layout;
    private ImageButton add_quantity_hide_error_btn;
    private TableLayout ingredients_table;
    private MaterialButton add_recipe3_next_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__ingre__quantity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        HomeNavController = Navigation.findNavController(getActivity(),R.id.home_host_fragment);

        add_quantity_error_layout = view.findViewById(R.id.add_quantity_error_layout);
        add_quantity_hide_error_btn = view.findViewById(R.id.add_quantity_hide_error_btn);
        add_recipe3_next_btn = view.findViewById(R.id.add_recipe3_next_btn);
        add_quantity_hide_error_btn.setOnClickListener(this);
        add_recipe3_next_btn.setOnClickListener(this);
        ingredients_table = view.findViewById(R.id.ingredients_table);

        if(getArguments()!=null){
            selected_items_list = getArguments().getParcelableArrayList("selected ingredients");
        }
        new AttachIngredientsToTable().execute();
        final_ingredients_quantity_list = new ArrayList<Ingredients_Quantity_List>();

    }


    public class AttachIngredientsToTable extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CreateDynamicView();
                }
            });
            return "";

        }
    }
    private void CreateDynamicView(){


        for(int i=0;i<selected_items_list.size();i++){
            TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.ingre_quantity_table_view_items,null);
            TextView item_Name = item_Row.findViewById(R.id.ingre_item_name);
            item_Name.setText(selected_items_list.get(i).getIngredientItemName());

            TextInputLayout add_quantity_input_layout = item_Row.findViewById(R.id.add_quantity_input_layout);
            TextInputLayout add_quantity_unit_layout = item_Row.findViewById(R.id.add_quantity_unit_layout);


            AutoCompleteTextView add_quantity_unit = add_quantity_unit_layout.findViewById(R.id.add_quantity_unit);
            ArrayAdapter<String> QuantityUnitArray = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,Constants.quantity_unit_array);
            //QuantityUnitArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            add_quantity_unit.setAdapter(QuantityUnitArray);

            ingredients_table.addView(item_Row);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view == add_quantity_hide_error_btn){
            add_quantity_error_layout.setVisibility(View.GONE);
        }
        else if(view==add_recipe3_next_btn){
            String Quantity="",QuantityUnit="";
            int TableChildCount = ingredients_table.getChildCount();
            for(int i=0;i<TableChildCount;i++) {
                View viewChild = ingredients_table.getChildAt(i);
                if (viewChild instanceof TableRow) {
                    View viewChild2 = ((TableRow) viewChild).getChildAt(0);
                    if(viewChild2 instanceof ConstraintLayout){
                        View viewChild3 = ((ConstraintLayout) viewChild2).getChildAt(1);
                        if(viewChild3 instanceof LinearLayout){
                            View viewChild4 = ((LinearLayout) viewChild3).getChildAt(0);
                            if(viewChild4 instanceof TextInputLayout){
                                Quantity = ((TextInputLayout) viewChild4).getEditText().getText().toString();
                            }
                            View viewChild5 = ((LinearLayout) viewChild3).getChildAt(1);
                            if (viewChild5 instanceof TextInputLayout) {
                                View viewChild6 = ((TextInputLayout) viewChild5).getChildAt(0);
                                View viewChild7 = ((FrameLayout)viewChild6).getChildAt(0);
                                if(viewChild7 instanceof AutoCompleteTextView){
                                    QuantityUnit = ((AutoCompleteTextView) viewChild7).getText().toString();
                                }
                            }

                        }
                    }
                }
                if(Quantity.equals("")|QuantityUnit.equals("")){
                    add_quantity_error_layout.setVisibility(View.VISIBLE);
                    final_ingredients_quantity_list.clear();
                    break;
                }
                else {
                    String ItemID = selected_items_list.get(i).getIngredientItemId();
                    String ItemQuantity = Quantity + " " + QuantityUnit;
                    final_ingredients_quantity_list.add(new Ingredients_Quantity_List(ItemID, ItemQuantity));
                    Quantity="";
                    QuantityUnit="";
                }
            }
            if(final_ingredients_quantity_list.size() == selected_items_list.size()){
                add_quantity_error_layout.setVisibility(View.GONE);
                JSONArray Ingre_Quantity_JSONArray = new JSONArray();
                try {
                    for (int i=0;i<final_ingredients_quantity_list.size();i++){
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("ingredient_id",final_ingredients_quantity_list.get(i).getIngredientItemId());
                        jsonObject.put("ingredient_amount",final_ingredients_quantity_list.get(i).getIngredientItemQuantity());
                        Ingre_Quantity_JSONArray.put(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PreferenceData.SetRecipeIngredients(context,Ingre_Quantity_JSONArray.toString());
                HomeNavController.navigate(R.id.action_add_Ingre_Quantity_Fragment_to_add_Recipe_InstructionsFragment);
            }
        }

    }
}
