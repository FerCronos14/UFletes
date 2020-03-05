package com.example.ufletes;

public class Articulos_Lista {
    private String nombre_Articulo;
    private String path_Articulo;
    //private float valoracion;
    private Integer cantidad_Articulo;
    private String descripcion_Articulo;

    public Articulos_Lista(String nombre_Articulo, String path_Articulo, Integer cantidad_Articulo, String descripcion_Articulo) {
        this.nombre_Articulo = nombre_Articulo;
        this.path_Articulo = path_Articulo;
        this.cantidad_Articulo = cantidad_Articulo;
        this.descripcion_Articulo = descripcion_Articulo;
    }

    public String getNombre_Articulo() {
        return nombre_Articulo;
    }

    public void setNombre_Articulo(String nombre_Articulo) {
        this.nombre_Articulo = nombre_Articulo;
    }

    public String getPath_Articulo() {
        return path_Articulo;
    }

    public void setPath_Articulo(String path_Articulo) {
        this.path_Articulo = path_Articulo;
    }

    public Integer getCantidad_Articulo() {
        return cantidad_Articulo;
    }

    public void setCantidad_Articulo(Integer cantidad_Articulo) {
        this.cantidad_Articulo = cantidad_Articulo;
    }

    public String getDescripcion_Articulo() {
        return descripcion_Articulo;
    }

    public void setDescripcion_Articulo(String descripcion_Articulo) {
        this.descripcion_Articulo = descripcion_Articulo;
    }
}
