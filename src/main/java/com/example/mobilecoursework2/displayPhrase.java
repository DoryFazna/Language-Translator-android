package com.example.mobilecoursework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class displayPhrase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DBController controller;
    private Cursor cursor;
    private String phrase;
    boolean editMode;

    Button saveButton;
    public static EditText phrase_to_edit;
    LinearLayout editViewElements;

    MyAdapter adapter;


    ArrayList<Phrase> phraseList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrase);


        Intent intent = getIntent();
        editMode =intent.getBooleanExtra("EditMode",false);
        saveButton = findViewById(R.id.save_edit_btn);
        saveButton.setEnabled(false);

        editViewElements = findViewById(R.id.editViewElements);
        if(editMode) {
            phrase_to_edit = findViewById(R.id.chosen_txt);
            editViewElements.setVisibility(View.VISIBLE);

        }

        controller = new DBController(this);
        recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(displayPhrase.this);
        recyclerView.setLayoutManager(layoutManager);

        displayList();
    }


    public void displayPhraseOnEdit(View view) {
        adapter.editButtonClicked =true;
        saveButton.setEnabled(true);
        phrase_to_edit.setText(adapter.getSelectedPhrase());

    }

    public void savePhraseOnSave(View view) {
        String editedWord = phrase_to_edit.getText().toString();
        String oldPhrase = adapter.getSelectedPhrase();
        try {
            controller.updatePhrase(oldPhrase, editedWord);
            Toast.makeText(this,"Phrase "+oldPhrase+" is successfully edited as "+editedWord, Toast.LENGTH_SHORT).show();
            displayList();
        }catch(NullPointerException e){
            Toast.makeText(this,"Field is empty" , Toast.LENGTH_SHORT).show();
        }

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    public void displayList(){
        phraseList.clear();
        cursor = controller.listAllPhrases();

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                phrase = cursor.getString(1);
                Phrase p = new Phrase(phrase);
                phraseList.add(p);

            }
        }
        adapter = new MyAdapter(displayPhrase.this,phraseList,editMode);
        recyclerView.setAdapter(adapter);

    }
}
