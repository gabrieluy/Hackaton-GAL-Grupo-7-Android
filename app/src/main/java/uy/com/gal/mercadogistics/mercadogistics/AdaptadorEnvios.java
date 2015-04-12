package uy.com.gal.mercadogistics.mercadogistics;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Currency;
import java.util.List;

/**
 * Created by GABRIEL on 11/04/2015.
 */
public class AdaptadorEnvios  extends ArrayAdapter<Envio> {
    ActionBarActivity context;
    List<Envio> datos;

    AdaptadorEnvios(ActionBarActivity context, List<Envio> datos) {
        super(context, R.layout.listitem_envio, datos);
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder;
        if (item == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.listitem_envio, null);
            holder = new ViewHolder();
            AsignarElementeosHolder(item, holder);
            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }
        try {
            LlenarControlesBascios(position, holder);
        } catch (Exception ex) {
            Log.e("LOGEO-AE", "Exception al llenar controles :", ex);
        }
        return (item);
    }
    private void LlenarControlesBascios(int position, ViewHolder holder) {
        holder.nombreItem.setText(datos.get(position).getItem().getNombre());
        holder.direccion.setText(datos.get(position).getDireccion());
        holder.nombreCliente.setText(datos.get(position).getComprador().getNombre());
        holder.apellidoCliente.setText(datos.get(position).getComprador().getApellido());
        holder.telefonoCliente.setText(datos.get(position).getComprador().getTelefono());
        holder.precio.setText(Integer.toString(datos.get(position).getPrecioTotal()));
        holder.moneda.setText(datos.get(position).getMoneda());
        holder.ciudad.setText(datos.get(position).getCiudad());
        if(datos.get(position).getComentarios() == "null"){
            holder.comentarios.setVisibility(View.GONE);
            holder.lblComentarios.setVisibility(View.GONE);
        }else{
            holder.comentarios.setVisibility(View.VISIBLE);
            holder.lblComentarios.setVisibility(View.VISIBLE);
        }
        holder.comentarios.setText(datos.get(position).getComentarios());
        holder.estado.setText(Traducir(datos.get(position).getEstado()));
        holder.email.setText(datos.get(position).getComprador().getEmail());
    };

    private void AsignarElementeosHolder(View item, ViewHolder holder) {
        holder.nombreItem = (TextView) item.findViewById(R.id.nombre_item_text_view);
        holder.direccion = (TextView) item.findViewById(R.id.direccion_text_view);
        holder.nombreCliente = (TextView) item.findViewById(R.id.nombre_usuario_text_view);
        holder.apellidoCliente = (TextView) item.findViewById(R.id.apellido_usuario_text_view);
        holder.telefonoCliente = (TextView) item.findViewById(R.id.telefono_text_view);
        holder.precio = (TextView) item.findViewById(R.id.precio_text_view);
        holder.moneda = (TextView) item.findViewById(R.id.moneda_text_view);
        holder.ciudad = (TextView) item.findViewById(R.id.ciudad_text_view);
        holder.lblComentarios = (TextView) item.findViewById(R.id.comentarios_label);
        holder.comentarios = (TextView) item.findViewById(R.id.comentarios_text_view);
        holder.estado = (TextView) item.findViewById(R.id.estado_text_view);
        holder.email = (TextView) item.findViewById(R.id.email_text_view);

    }
    static class ViewHolder {
        TextView nombreItem;
        TextView direccion;
        TextView nombreCliente;
        TextView apellidoCliente;
        TextView telefonoCliente;
        TextView precio;
        TextView moneda;
        TextView ciudad;
        TextView lblComentarios;
        TextView comentarios;
        TextView email;
        TextView estado;
    }

    public String Traducir(String nombreIngles) {
        switch (nombreIngles) {
            case "pending":
                return "pendiente";
            case "handling":
                return "procesado";
            case "ready_to_ship":
                return "listo enviar";
            case "shipped":
                return "enviado";
            case "delivered":
                return "entregado";
            case "not_delivered":
                return "no entregado";
            case "cancelled":
                return "cancelado";
            case "confirmed":
                return "confirmada";
            case "to_be_agreed":
                return "acordar envio";
            case "payment_required":
                return "Pago Requerido";
            case "payment_in_process":
                return "Pago en Proceso";
            case "paid":
                return "Pagada";

            default:
                return nombreIngles;
        }
    }
}
