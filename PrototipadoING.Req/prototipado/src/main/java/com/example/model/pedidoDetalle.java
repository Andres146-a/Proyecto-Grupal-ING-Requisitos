package com.example.model;

public class pedidoDetalle {
    private int id;
    private int pedidoId;
    private int platilloId;
    private int cantidad;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPedidoId() {
        return pedidoId;
    }
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }
    public int getPlatilloId() {
        return platilloId;
    }
    public void setPlatilloId(int platilloId) {
        this.platilloId = platilloId;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Getters y Setters
    
}
