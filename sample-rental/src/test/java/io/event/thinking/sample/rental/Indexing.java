package io.event.thinking.sample.rental;

import io.event.thinking.eventstore.api.Index;
import io.event.thinking.micro.es.test.MultiEventIndexer;
import io.event.thinking.sample.rental.api.event.BikeRented;
import io.event.thinking.sample.rental.api.event.CarRented;

import java.util.Set;

import static io.event.thinking.micro.es.Indices.typeIndex;
import static io.event.thinking.sample.rental.commandhandler.Indices.bikeIndex;
import static io.event.thinking.sample.rental.commandhandler.Indices.carIndex;

public class Indexing {

    public static MultiEventIndexer multiEventIndexer() {
        return new MultiEventIndexer().register(BikeRented.class, Indexing::index)
                                      .register(CarRented.class, Indexing::index);
    }


    public static Set<Index> index(CarRented payload) {
        return Set.of(typeIndex(CarRented.NAME),
                      carIndex(payload.id()));
    }

    public static Set<Index> index(BikeRented payload) {
        return Set.of(typeIndex(BikeRented.NAME),
                      bikeIndex(payload.id()));
    }

}
