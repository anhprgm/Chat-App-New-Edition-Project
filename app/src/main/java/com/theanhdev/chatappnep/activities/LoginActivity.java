package com.theanhdev.chatappnep.activities;

import static com.theanhdev.chatappnep.R.drawable.enter_btn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theanhdev.chatappnep.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private ImageView enterBtn;
    private FirebaseAuth mAuth;
    private TextView textSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        binding();
        signUpChangeScreen();
        emailInput.setOnClickListener(v -> enterBtn.setImageDrawable(getResources().getDrawable(enter_btn)));
    }
    private void binding() {
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        enterBtn = findViewById(R.id.enter);
        ProgressBar loader = findViewById(R.id.loader);
        textSignIn = findViewById(R.id.signUpText);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            enterBtn.setOnClickListener(v -> loginUser());
        }
    }

    private void signUpChangeScreen() {
        textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}