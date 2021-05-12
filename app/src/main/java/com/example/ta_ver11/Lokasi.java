package com.example.ta_ver11;

import android.content.Intent;

public class Lokasi {
    private String nama;
    private String coordinat;
    private String jarak;
    private String lat;
    private String lon;

    public Lokasi() {
    }

    public Lokasi(String nama, String coordinat, String jarak, String lat, String lon) {
        this.nama = nama;
        this.coordinat = coordinat;
        this.jarak = jarak;
        this.lat = lat;
        this.lon = lon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getCoordinat() {
        return coordinat;
    }

    public void setCoordinat(String coordinat) {
        this.coordinat = coordinat;
    }

    public String getJarak() {  return jarak; }

    public void setJarak(String jarak) { this.jarak = jarak; }

    public String getLat() {  return lat; }

    public void setLat(String lat) { this.lat = lat; }

    public String getLon() {  return lon; }

    public void setLon(String lon) { this.lon = lon; }
}
