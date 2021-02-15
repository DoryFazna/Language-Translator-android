package com.example.mobilecoursework2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

import java.util.ArrayList;

public class translate extends AppCompatActivity {

    ProgressBar progressBar;
    TextView translatedPhrase;
    Button translateButton;
    Spinner spinner;
    ListView phraseListView;
    private DBController controller;
    String selectedPhrase;
    private LanguageTranslator translationService;
    String selectedLanguage;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        translationService =initLanguageTranslatorService();
        MultiDex.install(this);
        selectedPhrase="";

        controller = new DBController(this);

        progressBar = findViewById(R.id.progressBar);
        translatedPhrase = findViewById(R.id.translated_phrase);
        translateButton = findViewById(R.id.translate_button);
        spinner = findViewById(R.id.spinner_languages);
        phraseListView = findViewById(R.id.savedPhrasesToTranslateList);

        translateButton.setEnabled(false);
        setSpinnerValues();
        setListValues();



    }

    public void translatePhrase(View view) {
        progressBar.setVisibility(View.VISIBLE);
        selectedLanguage = spinner.getSelectedItem().toString();
        code = controller.getCode(selectedLanguage);

        try {
            new TranslationTask().execute(selectedPhrase);
        }catch(Exception e){
            translatedPhrase.setText("Language NOT AVAILABLE");
        }

        translateButton.setEnabled(false);


    }

    public void setSpinnerValues(){
        String lang;
        ArrayList<String> langList = new ArrayList<>();
        Cursor cursor = controller.listAllSubscribedLanguages();
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                lang = cursor.getString(1);
                langList.add(lang);
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, langList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(selectedPhrase.equals("")))
                    translateButton.setEnabled(true);
                selectedLanguage = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + selectedLanguage,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }


    public void setListValues() {
        String phrase;
        final ArrayList<String> phraseList = new ArrayList<>();
        Cursor cursor = controller.listAllPhrases();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                phrase = cursor.getString(1);
                phraseList.add(phrase);
            }
        }


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(translate.this,R.layout.support_simple_spinner_dropdown_item,phraseList);

        phraseListView.setAdapter(adapter);

        phraseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                translateButton.setEnabled(true);
                selectedPhrase = (String) parent.getItemAtPosition(position);


            }
        });
    }




    private LanguageTranslator initLanguageTranslatorService(){
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(getString(R.string.language_translator_url));
        return service;

    }


    private class TranslationTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source(Language.ENGLISH)
                    .target(code)
                    .build();
            TranslationResult result =translationService.translate(translateOptions).execute().getResult();
            String firstTranslation =result.getTranslations().get(0).getTranslation();
            return firstTranslation;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            translatedPhrase.setText(s);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }



}
