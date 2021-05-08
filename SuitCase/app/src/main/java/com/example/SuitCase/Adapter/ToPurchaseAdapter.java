package com.example.SuitCase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.example.SuitCase.AddNewProduct;
import com.example.SuitCase.MainActivity;
import com.example.SuitCase.Model.ToPurchaseModel;
import com.example.SuitCase.R;
import com.example.SuitCase.Utils.DatabaseHandler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToPurchaseAdapter extends RecyclerView.Adapter<ToPurchaseAdapter.ViewHolder> {

    private List<ToPurchaseModel> toPurchaseList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToPurchaseAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
         View itemView = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.product_layout, parent, false);
         return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        db.openDatabase();

        final ToPurchaseModel item = toPurchaseList.get(position);
        holder.product.setText(item.getProduct());
        holder.product.setChecked(toBoolean(item.getStatus()));
        holder.product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(item.getId(), 1);
                }
                else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });

        //sending a pre-filled sms
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey friend, I found this awesome product i like and was hoping you could get it for me ,its called ");
                getContext().startActivity(shareIntent);
            }
        });

    }

    private boolean toBoolean(int n){
        return n != 0;
    }

    @Override
    public int getItemCount(){
        return toPurchaseList.size();
    }

    public Context getContext(){
        return activity;
    }

    public void setProducts(List<ToPurchaseModel> toPurchaseList){
        this.toPurchaseList = toPurchaseList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        ToPurchaseModel item = toPurchaseList.get(position);
        db.deleteProduct(item.getId());
        toPurchaseList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToPurchaseModel item = toPurchaseList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("product", item.getProduct());
        AddNewProduct fragment = new AddNewProduct();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewProduct.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox product;
        ImageButton imageButton;

        ViewHolder(View view){
            super(view);
            product = view.findViewById(R.id.purchasedCheckBox);
            imageButton = view.findViewById(R.id.smsButton);
        }
    }

}
