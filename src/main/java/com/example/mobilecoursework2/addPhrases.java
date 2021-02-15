package com.example.mobilecoursework2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class addPhrases extends AppCompatActivity {

    private EditText txtPhrase;
    private DBController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        txtPhrase = findViewById(R.id.edit_txt_phrase);
        controller = new DBController(this);
    }

    public void addPhrase(View view) {

        String phrase = txtPhrase.getText().toString();
        try{
            controller.insertPhrase(phrase);
            Toast.makeText(this,"Phrase '"+phrase+"' is added successfully",Toast.LENGTH_SHORT).show();

        }catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(this,"ALREADY EXISTS",Toast.LENGTH_SHORT).show();
        }
        txtPhrase.setText("");

    }
}
