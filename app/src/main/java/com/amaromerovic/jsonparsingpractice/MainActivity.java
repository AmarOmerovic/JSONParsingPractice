package com.amaromerovic.jsonparsingpractice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.amaromerovic.jsonparsingpractice.data.Address;
import com.amaromerovic.jsonparsingpractice.data.Company;
import com.amaromerovic.jsonparsingpractice.data.Geo;
import com.amaromerovic.jsonparsingpractice.data.User;
import com.amaromerovic.jsonparsingpractice.databinding.ActivityMainBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private final String apiUrl = "https://jsonplaceholder.typicode.com/users";
    private List<User> users;
    private ActivityMainBinding mainBinding;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        queue = Volley.newRequestQueue(MainActivity.this);
        users = new ArrayList<>();
        index = 0;

        dataParsing();

        mainBinding.next.setOnClickListener(view -> {
            index++;
            if (index == users.size() - 1){
                mainBinding.next.setClickable(false);
            }
            mainBinding.previous.setClickable(true);
            displayUser();

        });

        mainBinding.previous.setOnClickListener(view -> {
            index--;
            if (index < 1){
                mainBinding.previous.setClickable(false);
            }
            mainBinding.next.setClickable(true);
            displayUser();
        });

    }

    private void dataParsing() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++){
                            users.add(new User(response.getJSONObject(i).getInt("id"),
                                            response.getJSONObject(i).getString("name"),
                                            response.getJSONObject(i).getString("username"),
                                            response.getJSONObject(i).getString("email"),
                                            new Address(response.getJSONObject(i).getJSONObject("address").getString("street"),
                                                    response.getJSONObject(i).getJSONObject("address").getString("suite"),
                                                    response.getJSONObject(i).getJSONObject("address").getString("city"),
                                                    response.getJSONObject(i).getJSONObject("address").getString("zipcode"),
                                                    new Geo(response.getJSONObject(i).getJSONObject("address").getJSONObject("geo").getDouble("lat"),
                                                            response.getJSONObject(i).getJSONObject("address").getJSONObject("geo").getDouble("lng"))
                                                    ),
                                            response.getJSONObject(i).getString("phone"),
                                            response.getJSONObject(i).getString("website"),
                                            new Company(response.getJSONObject(i).getJSONObject("company").getString("name"),
                                                        response.getJSONObject(i).getJSONObject("company").getString("catchPhrase"),
                                                        response.getJSONObject(i).getJSONObject("company").getString("bs"))));
                        }
                        displayUser();
                        mainBinding.previous.setClickable(false);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }, error -> Log.d("JSONParsing", "Parsing failed!" ));

        queue.add(jsonArrayRequest);
        queue.start();
    }

    @SuppressLint("SetTextI18n")
    private void displayUser() {
        User user = users.get(index);
        mainBinding.id.setText(getResources().getString(R.string.id) + "\n" + user.getId());
        mainBinding.username.setText(getResources().getString(R.string.username) + "\n" + user.getUsername());
        mainBinding.name.setText(getResources().getString(R.string.name) + "\n" + user.getName());
        mainBinding.email.setText(getResources().getString(R.string.email) + "\n" + user.getEmail());
        mainBinding.city.setText(getResources().getString(R.string.city) + "\n" + user.getAddress().getCity());
        mainBinding.zipcode.setText(getResources().getString(R.string.zipcode) + "\n" + user.getAddress().getZipcode());
        mainBinding.street.setText(getResources().getString(R.string.street) + "\n" + user.getAddress().getStreet());
        mainBinding.suite.setText(getResources().getString(R.string.suite) + "\n" + user.getAddress().getSuite());
        mainBinding.lat.setText(getResources().getString(R.string.lat) + "\n" + user.getAddress().getGeo().getLat());
        mainBinding.lng.setText(getResources().getString(R.string.lng) + "\n" + user.getAddress().getGeo().getLng());
    }
}