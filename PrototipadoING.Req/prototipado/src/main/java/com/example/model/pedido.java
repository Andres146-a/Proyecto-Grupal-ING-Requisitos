package com.example.model;

import javafx.beans.property.*;

public class pedido {
    private final IntegerProperty id;
    private final StringProperty cliente;
    private final StringProperty fecha;
    private final StringProperty estado;

    public pedido() {
        this.id = new SimpleIntegerProperty();
        this.cliente = new SimpleStringProperty();
        this.fecha = new SimpleStringProperty();
        this.estado = new SimpleStringProperty();
    }

    public pedido(int id, String cliente, String fecha, String estado) {
        this.id = new SimpleIntegerProperty(id);
        this.cliente = new SimpleStringProperty(cliente);
        this.fecha = new SimpleStringProperty(fecha);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getCliente() {
        return cliente.get();
    }

    public StringProperty clienteProperty() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente.set(cliente);
    }

    public String getFecha() {
        return fecha.get();
    }

    public StringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getEstado() {
        return estado.get();
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }
}
