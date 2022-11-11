package com.theanhdev.chatappnep.activities;

import static com.theanhdev.chatappnep.R.drawable.enter_btn;
import static com.theanhdev.chatappnep.R.drawable.successi_ic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.theanhdev.chatappnep.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private ImageView enterBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        binding();
        emailInput.setOnClickListener(v -> enterBtn.setImageDrawable(getResources().getDrawable(enter_btn)));
        enterBtn.setOnClickListener(v -> {
            addNewUser();
        });

    }
    private void binding() {
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        enterBtn = findViewById(R.id.enter);
        loader = findViewById(R.id.loader);
    }
    private void addNewUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        if (isValidEmail(email)) {
            if (is_Valid_Password(password)) {
                enterBtn.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        loader.setVisibility(View.GONE);
                        enterBtn.setImageDrawable(getResources().getDrawable(successi_ic));
                        enterBtn.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        loader.setVisibility(View.GONE);
                        enterBtn.setVisibility(View.VISIBLE);
                        Log.d("AAA", "createUserWithEmail:failure", task.getException());
                    }
                });
            } else {
                passwordInput.setError("A password must contain at least two digits.");
            }
        } else {
            emailInput.setError("Invalid email input.");
        }
    }

    public boolean is_Valid_Password(String password) {
        if (password.length() < 6) {
            passwordInput.setError("A password must have at least eight characters.");
            return false;
        }
        int charCount = 0;
        int numCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (is_Numeric(ch)) numCount++;
            else if (is_Letter(ch)) charCount++;
            else {
                passwordInput.setError("A password consists of only letters and digits.");
                return false;
            }
        }
        return (charCount >= 2 && numCount >= 2);
    }

    public static boolean is_Letter(char ch) {
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z');
    }
    public static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}