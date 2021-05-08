package com.example.SuitCase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.SuitCase.Adapter.ToPurchaseAdapter;
import com.example.SuitCase.Model.ToPurchaseModel;
import com.example.SuitCase.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView productRecyclerView;
    private ToPurchaseAdapter productsAdapter;
    private FloatingActionButton fab;
    private FloatingActionButton fabLogout;

    private List<ToPurchaseModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ToPurchaseAdapter(db,MainActivity.this);
        productRecyclerView.setAdapter(productsAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(productsAdapter));
        itemTouchHelper.attachToRecyclerView(productRecyclerView);

        fab = findViewById(R.id.fab);
        fabLogout = findViewById(R.id.fabLogout);

        productList = db.getAllProducts();
        Collections.reverse(productList);

        productsAdapter.setProducts(productList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewProduct.newInstance().show(getSupportFragmentManager(), AddNewProduct.TAG);
            }
        });

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        productList = db.getAllProducts();
        Collections.reverse(productList);
        productsAdapter.setProducts(productList);
        productsAdapter.notifyDataSetChanged();
    }
}