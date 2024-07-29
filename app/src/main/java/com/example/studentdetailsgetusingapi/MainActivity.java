package com.example.studentdetailsgetusingapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.file.ClosedFileSystemException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.entryPassword);
        button=(Button) findViewById(R.id.go);
        textView=(TextView) findViewById(R.id.text);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                String search=editText.getText().toString();
                if (search.isEmpty()){
                    editText.setError("Please enter entry passcode");
                }else {
                    String url="https://apps.technicalhub.io/old/techpanel2/api/anniversary/?id="+search;
                    //Toast.makeText(MainActivity.this, "url" +url, Toast.LENGTH_SHORT).show();
                    RequestQueue queue=Volley.newRequestQueue(MainActivity.this,new HurlStack(null,newSslSocketFactory()));
                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String rollnumber = response.getString("roll_number");
                                String name = response.getString("name");
                                String clg=response.getString("college");
                                String brn=response.getString("branch");
                                String pout=response.getString("pass out");
                                String genr=response.getString("gender");
                                String mail=response.getString("email");
                                String dayhstr=response.getString("dayscholars_hostlars");
                                String broot=response.getString("bus root");
                                String epass=response.getString("entry pass");
                                String epasscode=response.getString("entry passcode");
                                String sts=response.getString("status");
                               // Toast.makeText(MainActivity.this, "Response"+response, Toast.LENGTH_SHORT).show();
                                textView.setText("Roll Number :"+rollnumber+"\n"+"Name :"+name+"\n"+"College :"+clg+"\n"+"Branch :"+brn+"\n"+"PassOut :"+pout+"\n"+"Gender :"+genr+"\n"+"Email :"+mail
                                        +"\n"+"DaysScholars or Hostlars :"+dayhstr+"\n"+"Bus Root :"+broot+"\n"+"Entry Pass :"+epass+"\n"+"Entry PassCode :"+epasscode+"\n"+"Status :"+sts+"\n");
                                if(sts.equals("null")){
                                    textView.setText("Invalid Entry Passcode");
                                   // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, " Exception Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Invalid url", Toast.LENGTH_SHORT).show();

                        }
                    });
                    //Volley.newRequestQueue(MainActivity.this).add(request);
                    queue.add(request);

                }
            }
        });

    }
    private SSLSocketFactory newSslSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}