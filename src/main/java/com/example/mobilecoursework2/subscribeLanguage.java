package com.example.mobilecoursework2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class subscribeLanguage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DBController controller;
    private Cursor cursor;
    private String phrase;
    Button updateButton;
    static String[] languages;
    ListView listView;

    static ArrayList<String> selectedLanguages = new ArrayList<>();
    static ArrayList<String> removedLanguages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_language);
        controller = new DBController(this);


        listView = findViewById(R.id.listViewLanguages);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Cursor cursor = controller.listAllLanguages();
        int numOfLanguages = cursor.getCount();
        final String[] allLanguages = new String[numOfLanguages];

        for (int i = 0; i < numOfLanguages; i++) {
            cursor.moveToNext();
            String lang = cursor.getString(1);
            allLanguages[i]=lang;
        }

//prevvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

        Cursor c = controller.listAllSubscribedLanguages();
        final ArrayList<String > prevSelectedLang= new ArrayList<>();
        if(c.getCount()>0){
            while (c.moveToNext()){
                String lang = c.getString(1);
                prevSelectedLang.add(lang);

            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(subscribeLanguage.this,R.layout.text_checkbox,R.id.check_box,allLanguages){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_checkbox, parent, false);
                }
                CheckedTextView ctv = convertView.findViewById(R.id.check_box);
                ListView listview = parent.findViewById(R.id.listViewLanguages);


                if(prevSelectedLang.contains(allLanguages[position])){
                    listview.setItemChecked(position,true);

                    ctv.setChecked(true);
                }
                ctv.setText(allLanguages[position]);


                return convertView;

            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateButton.setEnabled(true);

                String selectedItem = ((TextView)view).getText().toString();

                if(prevSelectedLang.contains(selectedItem)){
                    removedLanguages.add(selectedItem);

                }else{
                    selectedLanguages.add(selectedItem);
                }
                int i=0;
            }
        });
        updateButton = findViewById(R.id.update_btn);
        updateButton.setEnabled(false);
    }

    public void updateSubscription(View view) {
        for(String lang : selectedLanguages){
            controller.updateSubscription(lang,"true");
        }

        for(String lang : removedLanguages){
            controller.updateSubscription(lang,"false");
        }
        Toast.makeText(subscribeLanguage.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
        updateButton.setEnabled(false);

    }

}