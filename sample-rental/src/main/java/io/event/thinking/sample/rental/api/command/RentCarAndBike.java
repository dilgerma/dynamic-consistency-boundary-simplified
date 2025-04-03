package io.event.thinking.sample.rental.api.command;

import java.util.UUID;

public record RentCarAndBike(UUID carId, UUID bikeId) {}