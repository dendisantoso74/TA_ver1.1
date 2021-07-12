package com.example.ta_ver11;

import com.google.firebase.database.Exclude;
import java.util.List;



public class Node {
    private String documentId;
    private String lat;
    private String lon;
    private int bobot;
    private Double jarak;
    List<String> tags; //id node
    List<String> state;
    public Node() {
        //public no-arg constructor needed
    }
    public Node(int bobot, Double jarak, String lat, String lon, List<String> tags) {
        this.bobot = bobot;
        this.jarak = jarak;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags; //id node
        this.state = state;
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
//    public List<String> getTags() {
//        return tags;
//    }
    public List<String> getState() {
        return state;
    }
}