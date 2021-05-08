package com.example.SuitCase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.SuitCase.Utils.DatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login, register;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        databaseHandler=new DatabaseHandler(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();

                if (databaseHandler.isLoginValid(usernameValue, passwordValue)) {

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                } else{
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Register here", Toast.LENGTH_SHORT).show();
            }
        });

    }
}