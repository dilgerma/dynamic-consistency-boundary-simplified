package io.event.thinking.sample.rental.commandhandler;

import io.event.thinking.eventstore.api.Criteria;
import io.event.thinking.eventstore.api.Index;
import io.event.thinking.micro.es.DcbCommandHandler;
import io.event.thinking.micro.es.Event;
import io.event.thinking.sample.rental.api.command.RentCarAndBike;
import io.event.thinking.sample.rental.api.event.BikeRented;
import io.event.thinking.sample.rental.api.event.CarRented;

import java.util.*;

import static io.event.thinking.eventstore.api.Criterion.criterion;
import static io.event.thinking.micro.es.Event.event;
import static io.event.thinking.micro.es.Indices.typeIndex;
import static io.event.thinking.sample.rental.commandhandler.Indices.bikeIndex;
import static io.event.thinking.sample.rental.commandhandler.Indices.carIndex;

public class RentCarAndBikeCommandHandler
        implements DcbCommandHandler<RentCarAndBike, RentCarAndBikeCommandHandler.State> {

    @Override
    public Criteria criteria(RentCarAndBike cmd) {
        var criterion = List.of(
                criterion(typeIndex(CarRented.NAME), carIndex(cmd.carId())),
                criterion(typeIndex(BikeRented.NAME), bikeIndex(cmd.bikeId()))
                );
        return Criteria.criteria(
                new HashSet<>(criterion));
    }

    @Override
    public State initialState() {
        return State.initial();
    }

    @Override
    public List<Event> handle(RentCarAndBike cmd, State state) {
         if(state.rentedCars.contains(cmd.carId())) throw new IllegalStateException("car rented");
        if(state.bikesRented.contains(cmd.bikeId())) throw new IllegalStateException("bike rented");

        var carIndex = List.of(typeIndex(CarRented.NAME),
                carIndex(cmd.carId()));
        var bikeIndex = List.of(typeIndex(BikeRented.NAME),
                bikeIndex(cmd.bikeId()));

        return List.of(
                event(new CarRented(cmd.carId()),carIndex.toArray(Index[]::new)),
                event(new BikeRented(cmd.bikeId()),bikeIndex.toArray(Index[]::new)));
    }

    @Override
    public State source(Object event, State state) {
        return switch (event) {
            case CarRented e -> state.withCarRented(e.id());
            case BikeRented e -> state.withBikeRented(e.id());
            default -> throw new RuntimeException("No handler for this event");
        };
    }

    public record State(List<UUID> rentedCars, List<UUID> bikesRented) {

        public State withCarRented(UUID id) {
            this.rentedCars.add(id);
            return this;
        }

        public State withBikeRented(UUID id) {
            this.bikesRented.add(id);
            return this;
        }

        public static State initial() {
            return new State(new ArrayList<>(), new ArrayList<>());
        }
    }
}