package uy.com.gal.mercadogistics.mercadogistics;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity implements  OnClickListener {
    private Button btnAceptar;
    private EditText etUsuario, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnAceptar = (Button)findViewById(R.id.login_button);
        etUsuario = (EditText)findViewById(R.id.usuario_edit_text);
        etPassword = (EditText)findViewById(R.id.password_edit_text);
        btnAceptar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //si se presiona el boton
        if(v.getId()==R.id.login_button){
            Intent i = new Intent(this, MainActivity.class);
            finish();
            startActivity(i);
        }
    }

    // tarea asincronica login
    public class TareaWSLogin extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost(Globales.HOST
                    + "/ALGO/");

            post.setHeader("content-type", "application/json");

            try {
                // Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("ejemplo", Globales.APP_ID);

                Log.i("LOGEO-LOGIN", "JSON login: " + dato.toString());
                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);

                // Obtengo los datos de la respuesta Json
                boolean error = respJSON.getBoolean("Error");

                // esto se puede poner mejor
                if (error) {
                    Log.i("LOGEO-LOGIN", "Error en login: ");
                    resul = false;
                }
            } catch (Exception ex) {
                Log.e("LOGEO-LOGIN", "Exception Login: ", ex);

                resul = false;

            }
            return resul;
        }
        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                Log.i("LOGEO-LOGIN", "Login Correcto!");

            } else {
                Log.i("LOGEO-LOGIN", "Error login");
                Toast.makeText(getBaseContext(),
                        "Error Login",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}