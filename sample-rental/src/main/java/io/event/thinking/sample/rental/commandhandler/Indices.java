package io.event.thinking.sample.rental.commandhandler;

import io.event.thinking.eventstore.api.Index;

import java.util.UUID;

import static io.event.thinking.eventstore.api.Index.index;

public class Indices {

    public static final String CAR_ID = "carId";
    public static final String BIKE_ID = "bikeId";

    private Indices() {

    }

    public static Index bikeIndex(UUID bikeId) {
        return index(BIKE_ID, bikeId.toString());
    }

    public static Index carIndex(UUID carId) {
        return index(CAR_ID, carId.toString());
    }
}
