package com.tec_mob.wheaty;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Networking {

    private static URL httpGetUrl(String urlString) {
        URL url;
        {
            try {
                url = new URL(urlString);
                return url;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static HttpURLConnection httpGetConnection(String urlString, String requestMethod) {
        URL url = httpGetUrl(urlString);
        HttpURLConnection connection;
        {
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(requestMethod);
                return connection;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static InputStream httpGetStream(String urlString, String requestMethod) {
        HttpURLConnection connection = httpGetConnection(urlString, requestMethod);
        InputStream inputStream;
        {
            try {
                inputStream = new BufferedInputStream(connection.getInputStream());
                return inputStream;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String convertStreamToString(InputStream inputStream) {
        // Agregamos la respuesta en el buffer para leerla
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        // Creamos un constructor de String.
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) { // Leemos cada línea hasta la ultima

                stringBuilder.append(line).append('\n'); // Agregamos cada línea y un espacio nuevo.
            }
        } catch (IOException e) { // Capturamos una posible excepción que pueda ocurrir.
            e.printStackTrace(); // Imprimimos la excepción en consola para saber qué pasó.
        }
        return stringBuilder.toString();
    }




}
