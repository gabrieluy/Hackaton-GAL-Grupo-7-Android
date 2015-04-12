package uy.com.gal.mercadogistics.mercadogistics;

/**
 * Created by GABRIEL on 11/04/2015.
 */
public class Envio {
    private String idEnvio;
    private String idOrden;
    private String ciudad;
    private String direccion;
    private String comentarios;
    private String estado;
    private Item item;
    private Comprador comprador;
    private int precioTotal;
    private  String moneda;

    public Envio(String idEnvio, String idOrden, String ciudad, String direccion, String comentarios, String estado, Item item, Comprador comprador, int precioTotal, String moneda) {
        this.idEnvio = idEnvio;
        this.idOrden = idOrden;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.comentarios = comentarios;
        this.estado = estado;
        this.item = item;
        this.comprador = comprador;
        this.precioTotal = precioTotal;
        this.moneda = moneda;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        return "Envio{" +
                "idEnvio='" + idEnvio + '\'' +
                ", idOrden='" + idOrden + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", estado='" + estado + '\'' +
                ", item=" + item +
                ", comprador=" + comprador +
                ", precioTotal=" + precioTotal +
                ", moneda='" + moneda + '\'' +
                '}';
    }
}
