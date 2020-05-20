package com.example.ufletes;

public class Articulos_Lista {
    private String nombre_a;
    private String path_Articulo;
    private String cant_a;
    private String descri_a;

    public Articulos_Lista() { }

    public Articulos_Lista(String nombre_a, String path_Articulo, String cant_a, String descri_a) {
        this.nombre_a = nombre_a;
        this.path_Articulo = path_Articulo;
        this.cant_a = cant_a;
        this.descri_a = descri_a;
    }

    public String getNombre_a() {
        return nombre_a;
    }

    public void setNombre_a(String nombre_a) {
        this.nombre_a = nombre_a;
    }

    public String getPath_Articulo() {
        return path_Articulo;
    }

    public void setPath_Articulo(String path_Articulo) {
        this.path_Articulo = path_Articulo;
    }

    public String getCant_a() {
        return cant_a;
    }

    public void setCant_a(String cant_a) {
        this.cant_a = cant_a;
    }

    public String getDescri_a() {
        return descri_a;
    }

    public void setDescri_a(String descri_a) {
        this.descri_a = descri_a;
    }
}
