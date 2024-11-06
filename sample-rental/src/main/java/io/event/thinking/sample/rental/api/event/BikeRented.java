package io.event.thinking.sample.rental.api.event;

import java.io.Serializable;
import java.util.UUID;

public record BikeRented(UUID id) implements Serializable {

    public static final String NAME = "BikeRented";
}
