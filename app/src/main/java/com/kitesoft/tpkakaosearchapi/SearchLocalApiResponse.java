package com.kitesoft.tpkakaosearchapi;

import java.util.List;

public class SearchLocalApiResponse {
    PlaceMeta meta;
    List<Place> documents;
}

class PlaceMeta{
    int total_count;
    int pageable_count;
    boolean is_end;
}

class Place{
    String id;
    String place_name;
    String category_name;
    String phone;
    String address_name;
    String road_address_name;
    String x;
    String y;
    String place_url;
    String distance; //중심좌표까지의 거리. 단, x,y 파라미터를 준 경우에만 존재. 단위는 meter
}


