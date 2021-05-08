package com.example.SuitCase;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.SuitCase.Model.ToPurchaseModel;
import com.example.SuitCase.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class  AddNewProduct extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newProductText;
    private Button newProductSaveButton;

    private DatabaseHandler db;

    public static AddNewProduct newInstance(){
        return new AddNewProduct();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_product, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newProductText = Objects.requireNonNull(getView()).findViewById(R.id.newProductText);
        newProductSaveButton = getView().findViewById(R.id.newProductButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String product = bundle.getString("product");
            newProductText.setText(product);
            assert product != null;
            if (product.length()>0)
                newProductSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.moneygreen));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newProductText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (s.toString().equals("")){
                     newProductSaveButton.setEnabled(false);
                     newProductSaveButton.setTextColor(Color.GRAY);
                 }
                 else {
                     newProductSaveButton.setEnabled(true);
                     newProductSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.moneygreen));
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newProductSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newProductText.getText().toString();
                if (finalIsUpdate){
                    db.updateProduct(bundle.getInt("id"), text);
                }
                else{
                    ToPurchaseModel product = new ToPurchaseModel();
                    product.setProduct(text);
                    product.setStatus(0);
                    db.insertProduct(product);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
