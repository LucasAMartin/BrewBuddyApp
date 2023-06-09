package com.example.brewbuddycs380;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ShoppingCart extends AppCompatActivity {

    ListView listView;
    String[] coffeeNames;
    int[] coffeeImages;
    String[] coffeeDescriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView= findViewById(R.id.list_view);


        coffeeNames = new String[UserService.shoppingCart.size()];
        coffeeImages = new int[UserService.shoppingCart.size()];
        coffeeDescriptions = new String[UserService.shoppingCart.size()];

        //fills the arrays to put in the coffee adapter
        System.out.println("The size of the cart is: "+UserService.shoppingCart.size());
        System.out.println("The cart contains: "+UserService.shoppingCart.toString());
        for(int i = 0; i<UserService.shoppingCart.size();i++){
            coffeeNames[i]=UserService.shoppingCart.get(i).getName();
            coffeeImages[i]=UserService.shoppingCart.get(i).getDrawableId();
            coffeeDescriptions[i]=UserService.shoppingCart.get(i).getDescription();
        }
        //makes the coffee adapter
        CoffeeAdapter adapter = new CoffeeAdapter(this, coffeeNames, coffeeDescriptions, coffeeImages);
        //then puts the adatper of the view
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //change view to fullscreen view of the text
            }
        });
        findViewById(R.id.checkout_button).setOnClickListener(l ->{
            runOnUiThread(() -> UserService.checkOut());
            Toast.makeText(getApplicationContext(), "Checked out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RecommendationScreen.class));
        });
    }
}