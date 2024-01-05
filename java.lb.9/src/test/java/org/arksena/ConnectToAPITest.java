package org.arksena;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import static org.arksena.ConnectToAPI.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConnectToAPITest {


    @Disabled
    @Test
    public void testGetData() throws Exception {
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
        String mockResponse = "Mocked response";
        InputStream mockInputStream = new ByteArrayInputStream(mockResponse.getBytes());

        // Set the response code to indicate success (200 OK)
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        when(ConnectToAPI.getData(anyString(), anyString())).thenCallRealMethod(); // Call the real method

        String response = ConnectToAPI.getData(anyString(), anyString());

        verify(mockConnection).disconnect();

        assertEquals(mockResponse, response);
    }




    @Test
    public void testConstructUrlWithoutArguments() throws Exception {
        String path = "/testEndpoint";
        String arguments = "";
        URL url = ConnectToAPI.buildUrl(path, arguments);
        assertEquals("http://api.recrm.ru/json/testEndpoint?key=43a0cef4dd3b4c818a5328154582ef5d", url.toString());
    }

    @Test
    public void testConstructUrlWithArguments() throws Exception {
        String path = "/testEndpoint";
        String arguments = "param1=value1&param2=value2";
        URL url = ConnectToAPI.buildUrl(path, arguments);
        assertEquals("http://api.recrm.ru/json/testEndpoint?key=43a0cef4dd3b4c818a5328154582ef5d&param1=value1&param2=value2", url.toString());
    }

    @Test
    public void testConstructUrlEmptyPath() throws Exception {
        String path = "";
        String arguments = "param1=value1";
        URL url = ConnectToAPI.buildUrl(path, arguments);
        assertEquals("http://api.recrm.ru/json?key=43a0cef4dd3b4c818a5328154582ef5d&param1=value1", url.toString());
    }
}
