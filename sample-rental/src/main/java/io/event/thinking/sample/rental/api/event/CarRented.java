package io.event.thinking.sample.rental.api.event;

import java.io.Serializable;
import java.util.UUID;

public record CarRented(UUID id) implements Serializable {

    public static final String NAME = "CarRented";
}
