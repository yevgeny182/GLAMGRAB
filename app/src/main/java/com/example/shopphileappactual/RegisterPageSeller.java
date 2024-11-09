package com.example.shopphileappactual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPageSeller extends AppCompatActivity {
    GlamGrabAuthentication db;
    EditText shopname, userName, pass_word, conf_password;
    ImageButton back;
    Button register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore otherData;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page_seller);


        back = findViewById(R.id.chevronLeft);
        db = new GlamGrabAuthentication(this);

        shopname = findViewById(R.id.shopName);
        userName = findViewById(R.id.userNameSeller);
        pass_word = findViewById(R.id.passwordSeller);
        register = findViewById(R.id.registerButton);
        conf_password = findViewById(R.id.passwordSeller2);

        mAuth = FirebaseAuth.getInstance();
        otherData = FirebaseFirestore.getInstance();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                String password = pass_word.getText().toString();
                String shopName = shopname.getText().toString();
                String confirmPass = conf_password.getText().toString();

                 String usertype = "seller";
                 String home = "";
                 boolean isSuccessful = false;



//                 Log.d("RegisterData", "Username: "
//                         + username + ", Password: " + password +
//                         ", UserType: " + usertype + ", ShopName: "
//                         + shopName + ", Home: " + home);
//
//                 boolean inserted = db.registerUser(username, password, usertype,shopName, home);
//                     if(inserted){
//                         Toast.makeText(RegisterPageSeller.this, "You are registered to GLAMGRAB✨ as Seller.", Toast.LENGTH_SHORT).show();
//                         startActivity(new Intent(RegisterPageSeller.this, LoginPage.class));
//                     }else{
//                         Toast.makeText(RegisterPageSeller.this, "Failed Registration, Registration Error", Toast.LENGTH_SHORT).show();
//                     }
                if(password.equals(confirmPass)) {
                    if(isValidPassword(password)) {
                        registerNewUserSeller(username, password, usertype, shopName, home);
                    } else {
                        Toast.makeText(RegisterPageSeller.this, "Password must be at least 8 characters, " +
                                "with uppercase, lowercase, digit, and special character.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterPageSeller.this, "Passwords do not match. Please retype.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void registerNewUserSeller(String username, String password, String usertype, String shopName, String home) {
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            Toast.makeText(RegisterPageSeller.this, "You are registered to GLAMGRAB✨ as Seller.", Toast.LENGTH_SHORT).show();

                            // Call otherUserData with userID to store additional data
                            otherUserData(username, usertype, shopName, home);

                            // Redirect to login after successful registration and data insertion

                        } else {
                            Toast.makeText(RegisterPageSeller.this, "Registration Error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void otherUserData(String email, String usertype, String shopName, String home) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("userType", usertype);
        userData.put("shopName", shopName);
        userData.put("home", home);
        otherData.collection("users")
                .add(userData)  // Firestore auto-generates a unique ID
                .addOnSuccessListener(documentReference -> {
                    mAuth.signOut();
                    Toast.makeText(RegisterPageSeller.this, "Data Insertion Success!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterPageSeller.this, LoginPage.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterPageSeller.this, "Insertion Failed",  Toast.LENGTH_SHORT).show();
                });
    }


    public boolean isValidPassword(String password){
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:'\",.<>?/]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        boolean isMatch = matcher.matches();
        Log.d("PasswordCheck", "Overall Match: " + isMatch);
        return isMatch;
    }
}



