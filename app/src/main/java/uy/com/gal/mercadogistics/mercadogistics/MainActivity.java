package uy.com.gal.mercadogistics.mercadogistics;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements  OnClickListener {
    private ProgressDialog pDialog;
    private FloatingActionButton scanBtn;
    private List<Envio> listaEnvios = new ArrayList<Envio>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (FloatingActionButton) findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        TareaWSGetEnviosPendientes tareaGetEnvios = new TareaWSGetEnviosPendientes();
        tareaGetEnvios.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        //if (id == R.id.menu_scan) {
        //    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //    scanIntegrator.initiateScan();
        // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //si se presiona el boton scan
        if (v.getId() == R.id.scan_button) {
            //llamamos a la aplicacion para escanear el codigo
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();

        }
    }

    //respuesta de la aplicacion que escanea el codigo
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //si hay resultado
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            TareaWSCambiarEstado tareaWSCambiarEstado = new TareaWSCambiarEstado();
            tareaWSCambiarEstado.execute(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se pudo escanear el codigo!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public class TareaWSCambiarEstado extends
            AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            String url = Globales.HOST
                    + "/shipments/"+params[0]+"?access_token="+ Globales.TOKEN;
            HttpPut put = new HttpPut(url);
            put.setHeader("content-type", "application/json");
            try {
                // Construimos el objeto  en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("status", Globales.ESTADO_ENVIADO);
                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);
                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                // Obtengo los datos de la respuesta Json
                Log.e("LOGEO-MAIN respuesta cambio estado", respStr);
              /*  boolean error = respJSON.getBoolean("Error");
                String mensajeError = respJSON.getString("MensajeError");
                boolean valor = respJSON.getBoolean("Valor");
                if (!error) {
                    Globales.USUARIO_ACTIVO.setConsultarLista_X(valor);
                } else {
                    Log.i("LOGEO-MAIN", "Error al obtener acceso ejecucion "
                            + mensajeError);
                    resul = false;
                }*/
            } catch (Exception ex) {
                Log.e("LOGEO-MAIN", "Exception ",ex);
                resul = false;
            }
            return resul;
        }
        @Override
        protected void onPreExecute () {
            // inicializo la animacion de carga
            pDialog.setMessage("Actualizando estado");
            pDialog.show();
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                try {//demora un poco para que al actualizar la lista se reflejen los cambios
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TareaWSGetEnviosPendientes tareaGetEnvios = new TareaWSGetEnviosPendientes();
                tareaGetEnvios.execute();
            } else {
                Toast.makeText(getBaseContext(),
                        "Error al cambiar el estado",
                        Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
    }

    public class TareaWSGetEnviosPendientes extends AsyncTask<String, Integer, Integer> {
        @SuppressLint("UseSparseArrays")
        @Override
        protected Integer doInBackground(String... params) {
            int result = 1;
            HttpClient httpClient = new DefaultHttpClient();
            String url = Globales.HOST
                    + "/orders/search?seller="+Globales.SELLER+"&access_token="+Globales.TOKEN;
            HttpGet get = new HttpGet(url);
            get.setHeader("content-type", "application/json");
            try {
                // Obtengo los datos de la respuesta Json
                HttpResponse response = httpClient.execute(get);
                String json_envios_string = EntityUtils.toString(response.getEntity());
               //Parseamos la respuesta a un objeto json
                JSONObject jsonEnvios = new JSONObject(json_envios_string);
               //Nos quedamos solo con los results
                JSONArray results = jsonEnvios.getJSONArray("results");
                Log.e("LOGEO-MAIN res", results.toString());
                if (results.length() > 0)// en caso de recibir una respuesta vacia
                {
                    listaEnvios.clear();
                    //para todos los results me quedo solo con los envios
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject ordenJSON = results.getJSONObject(i);
                        JSONObject envioJSON = ordenJSON.getJSONObject("shipping");
                        String idEnvio = envioJSON.getString("id");
                        if(idEnvio != "null")
                        {
                            JSONArray itemsJSON = ordenJSON.getJSONArray("order_items");
                            JSONObject itemJSON = itemsJSON.getJSONObject(0).getJSONObject("item");
                            JSONObject compradorJSON = ordenJSON.getJSONObject("buyer");
                            JSONObject telefonoJSON = compradorJSON.getJSONObject("phone");
                            JSONObject direccionJSON = envioJSON.getJSONObject("receiver_address");
                            JSONObject ciudadJSON = direccionJSON.getJSONObject("state");

                            Item item = new Item(
                                    itemJSON.getString("id"),
                                    itemJSON.getString("title"));
                            Comprador comprador = new Comprador(
                                    compradorJSON.getString("id"),
                                    compradorJSON.getString("first_name"),
                                    compradorJSON.getString("last_name"),
                                    telefonoJSON.getString("area_code")+ telefonoJSON.getString("number"),
                                    compradorJSON.getString("email"));
                            Envio envio = new Envio(
                                    envioJSON.getString("id"),
                                    ordenJSON.getString("id"),
                                    ciudadJSON.getString("name"),
                                    direccionJSON.getString("address_line"),
                                    direccionJSON.getString("comment"),
                                    envioJSON.getString("status"),
                                    item,
                                    comprador,
                                    ordenJSON.getInt("total_amount"),
                                    ordenJSON.getString("currency_id")) ;
                            Log.e("LOGEO-MAIN envio", envio.toString());
                            listaEnvios.add(envio);
                        }
                    }
                } else {
                    result = 0;
                    Log.e("LOGEO-MAIN", "No hay ordenes");
                    pDialog.dismiss();
                }
            } catch (Exception ex) {
                Log.e("LOGEO-MAIN", "Exception al obtener ordenes: ", ex);
                pDialog.dismiss();
                result = 0;
            }
                return result;

        }


            @Override
            protected void onPreExecute () {
                // inicializo la animacion de carga
                pDialog.setMessage("Obteniendo Envios");
                pDialog.show();
            }

            @Override
            protected void onPostExecute (Integer result){
                ListView lstListado = (ListView) findViewById(R.id.orders_list);
                switch (result) {
                    case 0://error en la llamada
                        Toast.makeText(getBaseContext(),"No es posible obtener envios",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1://proceso correcto
                        lstListado.setAdapter(new AdaptadorEnvios(
                                MainActivity.this, listaEnvios));
                        break;
                    case 2://sin envios
                        lstListado.setAdapter(new AdaptadorEnvios(
                                MainActivity.this, listaEnvios));
                        Toast.makeText(getBaseContext(),"No es posible obtener envios",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                pDialog.dismiss();
            }
        }
    }

