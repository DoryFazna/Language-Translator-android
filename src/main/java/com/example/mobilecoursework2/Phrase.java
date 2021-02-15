package com.example.mobilecoursework2;

public class Phrase implements Comparable<Phrase> {
    String phrase;

    public Phrase(String phrase) {
        this.phrase = phrase;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public int compareTo(Phrase o) {
        return this.phrase.compareTo(o.getPhrase());
    }
}
