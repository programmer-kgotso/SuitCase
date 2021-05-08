package com.example.SuitCase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.SuitCase.Utils.DatabaseHandler;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, email, dob, country;
    RadioGroup gender;
    Button register, cancel;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        dob=findViewById(R.id.dob);
        country=findViewById(R.id.country);
        gender=findViewById(R.id.gender);
        register=findViewById(R.id.register);
        cancel=findViewById(R.id.cancel);

        databaseHandler = new DatabaseHandler(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                String emailValue = email.getText().toString();
                String dobValue = dob.getText().toString();
                String countryValue = country.getText().toString();
                RadioButton checkedBtn = findViewById(gender.getCheckedRadioButtonId());
                String genderValue = checkedBtn.getText().toString();

                if (usernameValue.length() > 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", usernameValue);
                    contentValues.put("password", passwordValue);
                    contentValues.put("email", emailValue);
                    contentValues.put("dob", dobValue);
                    contentValues.put("country", countryValue);
                    contentValues.put("gender", genderValue);

                    databaseHandler.insertUser(contentValues);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "Registration complete, login now", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RegisterActivity.this, "Enter the values", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
            }
        });


    }
}