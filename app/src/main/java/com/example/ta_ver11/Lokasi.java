package com.example.ta_ver11;

public class Lokasi {
    private String nama;
    private String terdekat;
    private Double jarak;
    private String lat;
    private String lon;

    public Lokasi() {
    }

    public Lokasi(String nama, String terdekat, Double jarak, String lat, String lon) {
        this.nama = nama;
        this.terdekat = terdekat;
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

    public String getTerdekat() {
        return terdekat;
    }

    public void setTerdekat(String terdekat) {
        this.terdekat = terdekat;
    }

    public Double getJarak() {  return jarak; }

    public void setJarak(Double jarak) { this.jarak = jarak; }

    public String getLat() {  return lat; }

    public void setLat(String lat) { this.lat = lat; }

    public String getLon() {  return lon; }

    public void setLon(String lon) { this.lon = lon; }



}
