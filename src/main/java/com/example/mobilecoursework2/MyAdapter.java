package com.example.mobilecoursework2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    public int mSelectedItem = -1;
    public ArrayList<Phrase> arrayList;
    boolean editMode;
    public boolean editButtonClicked = false;

    String selected;


    public MyAdapter(Context context, ArrayList<Phrase> arrayList,boolean editMode) {
        this.context = context;
        this.arrayList = arrayList;
        this.editMode = editMode;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_file, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        if(editMode)
            holder.radioButton.setChecked(position == mSelectedItem);
        holder.loadData(position);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    public String getSelectedPhrase(){
        return selected;
    }

    // VIEW HOLDER ------------------------------------------------------------

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView item_txt;
        public RadioButton radioButton;

        //constructor
        public MyViewHolder(View itemView) {
            super(itemView);
            item_txt = itemView.findViewById(R.id.card_txt);
            if(editMode) {
                radioButton = itemView.findViewById(R.id.radio_btn);
                radioButton.setVisibility(View.VISIBLE);
            }
        }




        void loadData(final int position) {
            Phrase p = arrayList.get(position);
            item_txt.setText(p.getPhrase());

            if(editMode) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedItem = getAdapterPosition();
                        notifyDataSetChanged();
                        selected = item_txt.getText().toString();
                        Toast.makeText(context, selected, Toast.LENGTH_SHORT).show();

                        if(editButtonClicked)
                            displayPhrase.phrase_to_edit.setText(selected);
                    }
                };
                radioButton.setOnClickListener(listener);
                itemView.setOnClickListener(listener);
            }



        }
    }



}
