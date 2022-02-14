package com.unosquare.carmigo.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JourneyService {

    public static String getJourneyParameters(final Map<String, String> paramMap)
    {
        if (!paramMap.isEmpty()) {
            final Map.Entry<String, String> entry = paramMap.entrySet().iterator().next();
            final String key = entry.getKey();
            final String value = entry.getValue();

            switch (key) {
                case "passenger_id":
                    return "Here are all journeys for passenger " + value;
                case "driver_id":
                    return "Here are all journeys for driver " + value;
                default:
                    return "Error retrieving journeys";
            }
        } else {
            return "Here are all your journeys";
        }
    }
}
