package com.jmunoz.springboot.webflux.app.models.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

// Cuando se mapea a este Document se transforma en un JSON binario, llamado BSON, que es el tipo de dato
// en el que se guardan los datos en MONGO.
//
// Se corrige porque es collection
@Document(collection = "productos")
public class Producto {

    // En MongoDB el id es un string
    @Id
    private String id;

    private String nombre;
    private Double precio;
    private Date createAt;

    // Constructor vacío para que Spring Data pueda crear los objetos
    public Producto() {
    }

    public Producto(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
