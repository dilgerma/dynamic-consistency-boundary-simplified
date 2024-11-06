package io.event.thinking.sample.rental;

import io.event.thinking.micro.es.test.CommandHandlerFixture;
import io.event.thinking.sample.rental.api.command.RentCar;
import io.event.thinking.sample.rental.api.event.BikeRented;
import io.event.thinking.sample.rental.api.event.CarRented;
import io.event.thinking.sample.rental.commandhandler.RentCarCommandHandler;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static io.event.thinking.sample.rental.Indexing.multiEventIndexer;

class SubscribeStudentTest {

    private CommandHandlerFixture<RentCar> fixture;

    @BeforeEach
    void setUp() {
        fixture = new CommandHandlerFixture<>(RentCar.class,
                new RentCarCommandHandler(),
                multiEventIndexer());
    }

    @Test
    void successfulCarRental() {
        var carId = UUID.randomUUID();
        var bikeId = UUID.randomUUID();

        fixture.givenNoEvents()
                .when(new RentCar(carId, bikeId))
                .expectEvents(new CarRented(carId), new BikeRented(bikeId));
    }

    @Test
    void bikeRented() {
        var carId = UUID.randomUUID();
        var bikeId = UUID.randomUUID();

        fixture.given(new BikeRented(bikeId))
                .when(new RentCar(carId, bikeId))
                .expectException(IllegalStateException.class, "bike rented");
    }


    @Test
    void carRented() {
        var carId = UUID.randomUUID();
        var bikeId = UUID.randomUUID();

        fixture.given(new CarRented(carId))
                .when(new RentCar(carId, bikeId))
                .expectException(IllegalStateException.class, "car rented");
    }


    @Test
    void differentCarRented() {
        var carId = UUID.randomUUID();
        var bikeId = UUID.randomUUID();

        fixture.given(new CarRented(UUID.randomUUID()))
                .when(new RentCar(carId, bikeId))
                .expectEvents(new CarRented(carId), new BikeRented(bikeId));
    }


}