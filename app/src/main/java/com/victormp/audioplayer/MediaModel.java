package com.victormp.audioplayer;

// Modelo de los elementos media en la BBDD. Se usa para enviar los datos organizados a la ListView y cargar el reproductor
public class MediaModel {

    // Atributos
    private int id;
    protected String name;
    protected String url;

    // Constructor
    public MediaModel(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    // Getters & Setters
    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
