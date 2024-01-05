package org.arksena;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectToAPI {
    private static final String API_KEY = "43a0cef4dd3b4c818a5328154582ef5d";
    private static final String ENDPOINT = "http://api.recrm.ru/json";

    public static String getData(String endpointPath, String queryParams) throws IOException {
        URL url = buildUrl(endpointPath, queryParams);
        return executeGetRequest(url);
    }

    private static String executeGetRequest(URL endpointURL) throws IOException {
        HttpURLConnection connection = openConnection(endpointURL);

        try {
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection);
            } else {
                throw new IOException("Error: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static HttpURLConnection openConnection(URL endpointURL) throws IOException {
        return (HttpURLConnection) endpointURL.openConnection();
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder jsonResponse = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }

            return jsonResponse.toString();
        }
    }

    private static URL buildUrl(String path, String queryParams) throws IOException {
        StringBuilder resultUrl = new StringBuilder(ENDPOINT)
                .append(path)
                .append("?key=")
                .append(API_KEY);

        if (!queryParams.isEmpty()) {
            resultUrl.append("&").append(queryParams);
        }

        return new URL(resultUrl.toString());
    }
}

