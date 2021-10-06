package com.example.foweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText userField;
    Button btnToKnowWeather;
    TextView txtResult, txtCityName, txtLogo;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintView);
        userField = findViewById(R.id.userField);
        btnToKnowWeather = findViewById(R.id.btnToKnowWeather);
        txtLogo = findViewById(R.id.textViewLogo);
        txtResult = findViewById(R.id.textViewResult);
        txtCityName = findViewById(R.id.textViewCityName);

        btnToKnowWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userField.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите название города", Toast.LENGTH_LONG).show();
                } else {
                    String city = userField.getText().toString();
                    String key = "0d605b0b48ff10680b319c815f4bbdb4";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&units=metric&lang=ru";

                    new GetURLData().execute(url);
                }
            }
        });
    }
    private class GetURLData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            txtCityName.setText("");
            txtResult.setText("Получение данных...");
        }
        @Override
        protected String doInBackground(String... strings) {
            RequestQueue queue;
            queue = Volley.newRequestQueue(getApplicationContext());
            requestWeather(queue,strings[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
    void requestWeather(RequestQueue queue,String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject main = jsonObject.getJSONObject("main");

                    byte temp = (byte) main.getInt("temp");
                    txtLogo.setTextColor(getColor(R.color.black));
                    txtResult.setTextColor(getColor(R.color.black));
                    txtCityName.setTextColor(getColor(R.color.black));
                    userField.setTextColor(getColor(R.color.black));

                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                    btnToKnowWeather.setTextColor(getColor(R.color.black));

                    if(temp <= 0){
                        constraintLayout.setBackgroundColor(getColor(R.color.degreeL0));
                        btnToKnowWeather.setBackgroundColor(getColor(R.color.btn_degreeL0));
                        window.setStatusBarColor(getColor(R.color.btn_degreeL0));
                    } else if(temp <= 5) {
                        constraintLayout.setBackgroundColor(getColor(R.color.degreeL5));
                        btnToKnowWeather.setBackgroundColor(getColor(R.color.btn_degreeL5));
                        window.setStatusBarColor(getColor(R.color.btn_degreeL5));
                    } else if(temp <= 15) {
                        constraintLayout.setBackgroundColor(getColor(R.color.degreeL15));
                        btnToKnowWeather.setBackgroundColor(getColor(R.color.btn_degreeL15));
                        window.setStatusBarColor(getColor(R.color.btn_degreeL15));
                    } else if(temp <= 25) {
                        constraintLayout.setBackgroundColor(getColor(R.color.degreeL25));
                        btnToKnowWeather.setBackgroundColor(getColor(R.color.btn_degreeL25));
                        window.setStatusBarColor(getColor(R.color.btn_degreeL25));
                    } else if(temp > 25) {
                        constraintLayout.setBackgroundColor(getColor(R.color.degreeM25));
                        btnToKnowWeather.setBackgroundColor(getColor(R.color.btn_degreeM25));
                        window.setStatusBarColor(getColor(R.color.btn_degreeM25));
                    }

                    txtCityName.setText(jsonObject.getString("name"));
                    txtResult.setText("Температура: "+ temp+" °C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtResult.setText("Город не найден!");
                constraintLayout.setBackgroundColor(getColor(R.color.dark_bg));
                txtLogo.setTextColor(getColor(R.color.white));
                txtResult.setTextColor(getColor(R.color.white));
                txtCityName.setTextColor(getColor(R.color.white));
                userField.setTextColor(getColor(R.color.white));
                btnToKnowWeather.setBackgroundColor(getColor(R.color.gray_500));
                btnToKnowWeather.setTextColor(getColor(R.color.white));

                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getColor(R.color.gray_700));
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        });
        queue.add(stringRequest);
    }
}