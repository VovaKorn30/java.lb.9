package org.arksena;

public class Main {
    public static void main(String[] args) {
        Endpoint[] endpoints = getEndpoints();

        for (Endpoint endpoint : endpoints) {
            String jsonResponse = getDataWithExceptionsHandler(endpoint);
            convertJsonToExcelAndHandleExceptions(jsonResponse, "result", endpoint.getJsonKey(), endpoint.getSheetName());
        }
    }

    private static Endpoint[] getEndpoints() {
        return new Endpoint[]{
                new Endpoint("/countries", "", "countries", "countries"),
                new Endpoint("/cities", "", "cities", "cities"),
                new Endpoint("/districts", "&city_id=216", "districts", "kyiv_districts"),
                new Endpoint("/street/allstreets", "", "streets", "all_streets"),
                new Endpoint("/street/streets", "&city_id=216", "streets", "kyiv_streets"),
                new Endpoint("/metro/stations", "&city_id=216", "metro_stations", "kyiv_metro_stations"),
                new Endpoint("/routes/bycity", "&city_id=216", "routes", "kyiv_routes"),
                new Endpoint("/estatetypes/groups", "", "groups", "groups"),
                new Endpoint("/estatetypes", "&group_id=14", "types", "kyiv_estate_types"),
                new Endpoint("/finish/all", "", "finishes", "real_estate_finishes_types"),
                new Endpoint("/dictionary/values", "&type=5", "values", "real_estate_conditioning_types"),
                new Endpoint("/dictionary/values", "&type=12", "values", "real_estate_water_supplying_types"),
                new Endpoint("/dictionary/values", "&type=14", "values", "real_estate_gas_supplying_types"),
                new Endpoint("/dictionary/values", "&type=10", "values", "real_estate_electricity_supplying_types"),
                new Endpoint("/dictionary/values", "&type=11", "values", "real_estate_canalization_types"),
                new Endpoint("/estate/search", "&id=city_title=Киев&min_price_meter=340&max_price_meter=500", "results", "kyiv_estates"),
                new Endpoint("/estate/searchlastedited", "&id=city_title=Киев&min_price_meter=340&max_price_meter=500&new=1", "results", "changed_estate"),
                new Endpoint("/agent/all", "", "agents", "agents"),
                new Endpoint("/picture/EstateCoverPhoto", "&estate_id=379&width=200&height=150&crop=1&watermark=0", "pictures", "estate_cover_picture"),
                new Endpoint("/picture/EstatePhoto", "&estate_id=379&width=200&height=150&crop=1&watermark=0", "pictures", "estate_pictures"),
                new Endpoint("/picture/EstateLayout", "&estate_id=379&width=200&height=150&crop=1&watermark=0", "pictures", "layout_pictures"),
                new Endpoint("/picture/BuildingPhoto", "&building_id=251&width=200&height=150&crop=1&watermark=0", "pictures", "building_pictures"),
                new Endpoint("/contragent/getall", "", "contragents", "contragents"),
        };
    }

    private static String getDataWithExceptionsHandler(Endpoint endpoint) {
        try {
            String jsonResponse = ConnectToAPI.getData(endpoint.getEndpoint(), endpoint.getQueryParams());
            System.out.println(jsonResponse);
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void convertJsonToExcelAndHandleExceptions(String jsonResponse, String filename, String dataArrayKey, String sheetName) {
        if (jsonResponse != null) {
            try {
                ConvertJSONToExcel.convert(jsonResponse, filename, dataArrayKey, sheetName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
