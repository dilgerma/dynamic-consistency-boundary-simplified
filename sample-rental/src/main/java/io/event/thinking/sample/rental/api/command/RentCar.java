package io.event.thinking.sample.rental.api.command;

import java.util.UUID;

public record RentCar(UUID carId, UUID bikeId) {}