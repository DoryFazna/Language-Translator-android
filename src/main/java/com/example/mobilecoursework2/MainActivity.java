package com.example.mobilecoursework2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAddPhrase(View view) {
        Intent intent = new Intent(this,addPhrases.class);
        startActivity(intent);
    }

    public void openDisplayPhrase(View view) {
        Intent intent = new Intent(this,displayPhrase.class);
        startActivity(intent);
    }

    public void openEditPhrase(View view) {
        Intent intent = new Intent(this,displayPhrase.class);
        intent.putExtra("EditMode",true);
        startActivity(intent);
    }

    public void openLanguageSubscription(View view) {
        Intent intent = new Intent(this,subscribeLanguage.class);
        startActivity(intent);

    }

    public void openTranslate(View view) {
        Intent intent = new Intent(this,translate.class);
        startActivity(intent);
    }
}
