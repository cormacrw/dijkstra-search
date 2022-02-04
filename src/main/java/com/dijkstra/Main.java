package com.dijkstra;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Handler handler = new Handler();
        Map<String, Object> req = new HashMap<>();
        req.put("body", "{\"nodes\":[{\"id\":1,\"connections\":[2],\"x\":116,\"y\":615},{\"id\":2,\"connections\":[],\"x\":198,\"y\":405},{\"id\":3,\"connections\":[4],\"x\":370,\"y\":560},{\"id\":4,\"connections\":[],\"x\":593,\"y\":648}],\"target\":4}");
        handler.handleRequest(req, null);
    }
}
