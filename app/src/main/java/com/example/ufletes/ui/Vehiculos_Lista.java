package com.example.ufletes.ui;

public class Vehiculos_Lista {
    private String marca_v;
    private String tipo_v;
    private String vol_v;
    private String medida_v;
    private String pathFoto_v;

    public Vehiculos_Lista() { }

    public Vehiculos_Lista(String marca_v, String tipo_v, String vol_v, String medida_v, String pathFoto_v) {
        this.marca_v = marca_v;
        this.tipo_v = tipo_v;
        this.vol_v = vol_v;
        this.medida_v = medida_v;
        this.pathFoto_v = pathFoto_v;
    }

    public String getMarca_v() {
        return marca_v;
    }

    public void setMarca_v(String marca_v) {
        this.marca_v = marca_v;
    }

    public String getTipo_v() {
        return tipo_v;
    }

    public void setTipo_v(String tipo_v) {
        this.tipo_v = tipo_v;
    }

    public String getVol_v() {
        return vol_v;
    }

    public void setVol_v(String vol_v) {
        this.vol_v = vol_v;
    }

    public String getMedida_v() {
        return medida_v;
    }

    public void setMedida_v(String medida_v) {
        this.medida_v = medida_v;
    }

    public String getPathFoto_v() {
        return pathFoto_v;
    }

    public void setPathFoto_v(String pathFoto_v) {
        this.pathFoto_v = pathFoto_v;
    }
}
