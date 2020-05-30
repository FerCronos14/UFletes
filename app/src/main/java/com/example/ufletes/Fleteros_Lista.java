package com.example.ufletes;

public class Fleteros_Lista {
    private String nombre;
    private String apellidom;
    private String apellidop;
    private String telefono;
    private String pathFoto_v;
    private String idDocFletero;

    private String marca_v;
    private String tipo_v;
    private String vol_v;
    private String medida_v;

    public Fleteros_Lista() {

    }

    public Fleteros_Lista(String nombre, String apellidom, String apellidop, String telefono, String pathFoto_v, String idDocFletero, String marca_v, String tipo_v, String vol_v, String medida_v) {
        this.nombre = nombre;
        this.apellidom = apellidom;
        this.apellidop = apellidop;
        this.telefono = telefono;
        this.pathFoto_v = pathFoto_v;
        this.idDocFletero = idDocFletero;
        this.marca_v = marca_v;
        this.tipo_v = tipo_v;
        this.vol_v = vol_v;
        this.medida_v = medida_v;
    }

    public String getNombre() {
        return nombre;
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

    public String getPathFoto_v() {
        return pathFoto_v;
    }

    public void setPathFoto_v(String pathFoto_v) {
        this.pathFoto_v = pathFoto_v;
    }

    public String getIdDocFletero() {
        return idDocFletero;
    }

    public void setIdDocFletero(String idDocFletero) {
        this.idDocFletero = idDocFletero;
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
}
