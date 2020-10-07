package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText User;
    private EditText Password;
    private TextView attempt;
    private Button Login;
    private TextView Register;
    private int counter = 5;
    private CheckBox RememberMe;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         User = findViewById(R.id.etUserId);
         Password = findViewById(R.id.etPassword);
         attempt = findViewById(R.id.tvAttempt);
         Login = findViewById(R.id.btnlogin);
         Register = findViewById(R.id.tvRegister);
         RememberMe = findViewById(R.id.cbRememberMe);

         sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
         sharedPreferencesEditor = sharedPreferences.edit();

         if(sharedPreferences != null){

             String savedUsername = sharedPreferences.getString("Username", "");
             String savedPassword = sharedPreferences.getString("Password", "");

             RegistrationActivity.credentials = new Credentials(savedUsername, savedPassword);

             if(sharedPreferences.getBoolean("RememberMeCheckbox", false)){
                 User.setText(savedUsername);
                 Password.setText(savedPassword);
                 RememberMe.setChecked(true);
             }
         }

         RememberMe.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sharedPreferencesEditor.putBoolean("RememberMeCheckbox", RememberMe.isChecked());
                 sharedPreferencesEditor.apply();
             }
         });

         Register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
             }
         });

         //credentials.setUsername("Admin1");
         //credentials.setPassword("admin@2");

         attempt.setText("No. of attempts left : 5");

         Login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 String userName = User.getText().toString();
                 String userPassword = Password.getText().toString();

                 if(userName.isEmpty() || userPassword.isEmpty()){
                     Toast.makeText(MainActivity.this, "Please enter user name and password", Toast.LENGTH_LONG).show();
                 } else {
                     validate(User.getText().toString(), Password.getText().toString());
                 }
             }
         });
    }

    private void validate(String userName, String userPassword){

        if(RegistrationActivity.credentials!=null){
            if((userName.equals(RegistrationActivity.credentials.getUsername())) && (userPassword.equals(RegistrationActivity.credentials.getPassword()))){
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            } else{
                counter--;

                attempt.setText("No. of attempts left : " + String.valueOf(counter));

                if(counter==0){
                    Login.setEnabled(false);
                    Toast.makeText(MainActivity.this, "You have used all your attempts. Try again later!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}