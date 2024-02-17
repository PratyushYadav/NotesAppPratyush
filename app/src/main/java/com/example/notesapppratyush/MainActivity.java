package com.example.notesapppratyush;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Checking if user is logged in, if not, show login fragment
        if (!isLoggedIn()) {
            showLoginFragment();
        } else {
            showNotesFragment();
        }
    }
    private boolean isLoggedIn() {
        // Checking if user is logged in using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Boolean check= sharedPreferences.getBoolean("flag", false);
        return check;
    }
    private void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new LoginFragment())
                .commit();
    }
    public void showNotesFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NotesFragment())
                .commit();
    }
}
