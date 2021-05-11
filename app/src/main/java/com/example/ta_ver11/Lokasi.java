package com.example.ta_ver11;

public class Lokasi {
    private String nama;
    private  String coordinat;
    private String jarak;

    public Lokasi() {
    }

    public Lokasi(String nama, String coordinat) {
        this.nama = nama;
        this.coordinat = coordinat;
        this.jarak = jarak;
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
}
