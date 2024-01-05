package org.arksena;

import lombok.Getter;

@Getter
public class Endpoint {
    private final String endpoint;
    private final String queryParams;
    private final String jsonKey;
    private final String sheetName;

    public Endpoint(String endpoint, String queryParams, String jsonKey, String sheetName) {
        this.endpoint = endpoint;
        this.queryParams = queryParams;
        this.jsonKey = jsonKey;
        this.sheetName = sheetName;
    }
}
