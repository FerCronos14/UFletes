package com.example.ufletes;

public class Solicitudes_Lista {
    private String nombre_s;
    private String apellidop_s;
    private String telefono_s;
    private String dirOrigen_s;
    private String dirDestino_s;
    private String statusSolicitud_s;

    public Solicitudes_Lista() {

    }

    public Solicitudes_Lista(String nombre_s, String apellidop_s, String telefono_s, String dirOrigen_s, String dirDestino_s, String statusSolicitud_s) {
        this.nombre_s = nombre_s;
        this.apellidop_s = apellidop_s;
        this.telefono_s = telefono_s;
        this.dirOrigen_s = dirOrigen_s;
        this.dirDestino_s = dirDestino_s;
        this.statusSolicitud_s = statusSolicitud_s;
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

    public String getTelefono_s() {
        return telefono_s;
    }

    public void setTelefono_s(String telefono_s) {
        this.telefono_s = telefono_s;
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
}
