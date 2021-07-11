package com.example.cartrader.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.cartrader.MainActivity;
import com.example.cartrader.state.AppState;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CarTraderApi {
    private static final String TAG = "CarTraderApi";

    public static void getJSON(String url, boolean isAuthNeeded, final ReadDataHandler rdh) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (isAuthNeeded) {
                        connection.setRequestProperty("Authorization", AppState.token);
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String row;

                    while ((row = reader.readLine()) != null) {
                        response += row + "\n";
                    }

                    reader.close();
                    connection.disconnect();

                } catch (Exception e) {
                    response = "[]";
                    Log.d(TAG, "Exception GET: " + e.getMessage());
                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                // super.onPostExecute(s);
                rdh.setJson(response);
                rdh.sendEmptyMessage(0);
            }
        };

        task.execute(url);
    }

    public static void postDataJSON(String url, final JSONObject data, boolean isAuthNeeded, final ReadDataHandler rdh) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String response) {
                rdh.setJson(response);
                rdh.sendEmptyMessage(0);
            }

            @Override
            protected String doInBackground(String... strings) {
                String response = "";
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    connection.setRequestProperty("Accept", "application/json");

                    if (isAuthNeeded) {
                        // TODO: implementirati da se dohvati JWT iz SharedPreferences
                        String jwt = "";
                        connection.setRequestProperty("Authorization", jwt);
                    }


                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(data.toString());
                    writer.flush();
                    writer.close();
                    connection.getOutputStream().close();
                    connection.connect();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String row;

                    while ((row = reader.readLine()) != null) {
                        response += row + "\n";
                    }

                } catch (Exception e) {
                    response = "[]";
                    Log.d(TAG, "Exception POST: " + e.getMessage());
                }
                return response;
            }
        };

        task.execute(url);
    }

    public static void putDataJSON(String url, final JSONObject data, final ReadDataHandler rdh) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("PUT");

                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    // Ovde moramo obavezno da dodamo token
                    // Ne sme da se vrsi PUT operacija bez autorizacije!
                    connection.setRequestProperty("Authorization", AppState.token);

                    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    connection.setRequestProperty("Accept", "application/json");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String row;

                    while ((row = reader.readLine()) != null) {
                        response += row + "\n";
                    }

                    reader.close();
                    connection.disconnect();

                } catch (Exception e) {
                    response = "[]";
                    Log.d(TAG, "Exception GET: " + e.getMessage());
                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                // super.onPostExecute(s);
                rdh.setJson(response);
                rdh.sendEmptyMessage(0);
            }
        };

        task.execute(url);
    }

    public static void deleteDataJSON(String url, final ReadDataHandler rdh) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("DELETE");

                    // Ovde moramo obavezno da dodamo token
                    // Ne sme da se vrsi DELETE operacija bez autorizacije!
                    connection.setRequestProperty("Authorization", AppState.token);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String row;

                    while ((row = reader.readLine()) != null) {
                        response += row + "\n";
                    }

                    reader.close();
                    connection.disconnect();

                } catch (Exception e) {
                    response = "[]";
                    Log.d(TAG, "Exception GET: " + e.getMessage());
                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                // super.onPostExecute(s);
                rdh.setJson(response);
                rdh.sendEmptyMessage(0);
            }
        };

        task.execute(url);
    }
}
