package uy.com.gal.mercadogistics.mercadogistics;

import android.content.Intent;
import android.net.Uri;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
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
        etUsuario.setText(Globales.SELLER);
        etPassword.setText(Globales.TOKEN);
    }

    @Override
    public void onClick(View v) {
        //si se presiona el boton
        if(v.getId()==R.id.login_button){
            //no se logro hacer funcionar correctamente al login
            Globales.SELLER = etUsuario.getText().toString();
            Globales.TOKEN = etPassword.getText().toString();
            Intent i = new Intent(getBaseContext(), MainActivity.class);
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
            String url = Globales.HOST
                    + "/oauth/token?grant_type=authorization_code&client_id="+Globales.APP_ID+"&client_secret="+Globales.SECURITY_KEY+"&code="+Globales.ACCESS_CODE+"&redirect_uri=http://localhost/Default.aspx";
            HttpPost post = new HttpPost(url);
            try {
                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                //asigno el token
                Log.i("LOGEO-LOGIN", "Respuesta:"+ respStr);
                Globales.TOKEN = respJSON.getString("access_token");
                // esto se puede poner mejor
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