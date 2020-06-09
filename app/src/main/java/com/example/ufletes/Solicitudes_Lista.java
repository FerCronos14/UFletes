package com.example.ufletes;

public class Solicitudes_Lista {
    private String nombre_s;
    private String apellidop_s;
    private String apellidom_s;
    private String telefono_s;

    private String nombre_f_s;
    private String apellidop_f_s;
    private String apellidom_f_s;

    private String dirOrigen_s;
    private String dirDestino_s;
    private String statusSolicitud_s;
    private String fecha_s;
    private String idCliente_s;
    private String idFletero_s;

    public Solicitudes_Lista() {

    }

    public Solicitudes_Lista(String nombre_s, String apellidop_s, String apellidom_s, String telefono_s, String nombre_f_s, String apellidop_f_s, String apellidom_f_s, String dirOrigen_s, String dirDestino_s, String statusSolicitud_s, String fecha_s, String idCliente_s, String idFletero_s) {
        this.nombre_s = nombre_s;
        this.apellidop_s = apellidop_s;
        this.apellidom_s = apellidom_s;
        this.telefono_s = telefono_s;
        this.nombre_f_s = nombre_f_s;
        this.apellidop_f_s = apellidop_f_s;
        this.apellidom_f_s = apellidom_f_s;
        this.dirOrigen_s = dirOrigen_s;
        this.dirDestino_s = dirDestino_s;
        this.statusSolicitud_s = statusSolicitud_s;
        this.fecha_s = fecha_s;
        this.idCliente_s = idCliente_s;
        this.idFletero_s = idFletero_s;
    }

    public String getNombre_s() {
        return nombre_s;
    }

    public void setNombre_s(String nombre_s) {
        this.nombre_s = nombre_s;
    }

    public String getApellidop_s() {
        return apellidop_s;
    }

    public void setApellidop_s(String apellidop_s) {
        this.apellidop_s = apellidop_s;
    }

    public String getApellidom_s() {
        return apellidom_s;
    }

    public void setApellidom_s(String apellidom_s) {
        this.apellidom_s = apellidom_s;
    }

    public String getTelefono_s() {
        return telefono_s;
    }

    public void setTelefono_s(String telefono_s) {
        this.telefono_s = telefono_s;
    }

    public String getNombre_f_s() {
        return nombre_f_s;
    }

    public void setNombre_f_s(String nombre_f_s) {
        this.nombre_f_s = nombre_f_s;
    }

    public String getApellidop_f_s() {
        return apellidop_f_s;
    }

    public void setApellidop_f_s(String apellidop_f_s) {
        this.apellidop_f_s = apellidop_f_s;
    }

    public String getApellidom_f_s() {
        return apellidom_f_s;
    }

    public void setApellidom_f_s(String apellidom_f_s) {
        this.apellidom_f_s = apellidom_f_s;
    }

    public String getDirOrigen_s() {
        return dirOrigen_s;
    }

    public void setDirOrigen_s(String dirOrigen_s) {
        this.dirOrigen_s = dirOrigen_s;
    }

    public String getDirDestino_s() {
        return dirDestino_s;
    }

    public void setDirDestino_s(String dirDestino_s) {
        this.dirDestino_s = dirDestino_s;
    }

    public String getStatusSolicitud_s() {
        return statusSolicitud_s;
    }

    public void setStatusSolicitud_s(String statusSolicitud_s) {
        this.statusSolicitud_s = statusSolicitud_s;
    }

    public String getFecha_s() {
        return fecha_s;
    }

    public void setFecha_s(String fecha_s) {
        this.fecha_s = fecha_s;
    }

    public String getIdCliente_s() {
        return idCliente_s;
    }

    public void setIdCliente_s(String idCliente_s) {
        this.idCliente_s = idCliente_s;
    }

    public String getIdFletero_s() {
        return idFletero_s;
    }

    public void setIdFletero_s(String idFletero_s) {
        this.idFletero_s = idFletero_s;
    }
}