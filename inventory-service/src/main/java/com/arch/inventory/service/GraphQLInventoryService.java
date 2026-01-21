package com.arch.inventory.service;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import com.arch.inventory.model.Car;
import com.arch.inventory.repository.CarRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@GraphQLApi
public class GraphQLInventoryService {

    @Inject
    CarRepository carRepository;

    @Query
    public List<Car> cars() {
        return carRepository.listAll();
    }

    @Mutation
    @Transactional
    public Car register(Car car) {
        carRepository.persist(car);
        return car;
    }

    @Mutation
    @Transactional
    public boolean remove(String licensePlateNumber) {
        List<Car> cars = carRepository.listAll();
        Optional<Car> toBeRemoved = cars.stream()
                .filter(car -> car.getLicensePlateNumber()
                        .equals(licensePlateNumber))
                .findAny();
        if(toBeRemoved.isPresent()) {
            return cars.remove(toBeRemoved.get());
        } else {
            return false;
        }
    }
}
