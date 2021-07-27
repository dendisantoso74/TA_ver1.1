package com.example.ta_ver11;

import com.google.firebase.database.Exclude;

import java.io.DataOutput;
import java.util.List;



public class Node {
    private String documentId;
    private String lat;
    private String lon;
    private int bobot;
    private Double jarak;
   // private Double jarakAsli; //tambah jarak asli

    List<String> tags; //id node
    List<String> state;
    List<Double> jarakAsli;

    public Node() {
        //public no-arg constructor needed
    }
    public Node(int bobot, Double jarak, String lat, String lon, List<String> tags, List<Double> jarakAsli) {
        this.bobot = bobot;
        this.jarak = jarak;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags; //id node
        this.state = state;
        this.jarakAsli = jarakAsli;
        //this.jarakAsli = jarakAsli;//tambah jarak asli



    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public int getBobot() {
        return bobot;
    }
    public Double getJarak() {
        return jarak;
    }
    public String getLat() {
        return lat;
    }
    public String getLon() {
        return lon;
    }
//    public Double getJarakAsli() { //tambah jarak asli
//        return jarakAsli;
//    }
//    public List<String> getTags() {
//        return tags;
//    }
    public List<String> getState() {
        return state;
    }
    public List<Double> getJarakAsli() {
        return jarakAsli;
    }


}