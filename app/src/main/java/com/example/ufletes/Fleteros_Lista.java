package com.example.ufletes;

public class Fleteros_Lista {
    private String nombre;
    private String apellidom;
    private String apellidop;
    private String telefono;
    private String pathFoto_v;

    public Fleteros_Lista() {

    }

    public Fleteros_Lista(String nombre, String apellidom, String apellidop, String telefono, String pathFoto_v) {
        this.nombre = nombre;
        this.apellidom = apellidom;
        this.apellidop = apellidop;
        this.telefono = telefono;
        this.pathFoto_v = pathFoto_v;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPathFoto_v() {
        return pathFoto_v;
    }

    public void setPathFoto_v(String pathFoto_v) {
        this.pathFoto_v = pathFoto_v;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidom() {
        return apellidom;
    }

    public void setApellidom(String apellidom) {
        this.apellidom = apellidom;
    }

    public String getApellidop() {
        return apellidop;
    }

    public void setApellidop(String apellidop) {
        this.apellidop = apellidop;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
