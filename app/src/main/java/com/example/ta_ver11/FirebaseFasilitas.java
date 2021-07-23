package com.example.ta_ver11;

import com.google.firebase.database.Exclude;

import java.util.List;

public class FirebaseFasilitas {
    private String documentId;
    private String lat;
    private String lon;

    private Double jarak;


    List<String> tags; //id node
    //List<String> state;
    public FirebaseFasilitas() {
        //public no-arg constructor needed
    }
    public FirebaseFasilitas(Double jarak, String lat, String lon, List<String> tags) {

        this.jarak = jarak;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags; //id node
        //this.state = state;


    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

//    public List<String> getState() {
//        return state;
//    }


}
