package com.arch.inventory.repository;

import java.util.Optional;

import com.arch.inventory.model.Car;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

  public Optional<Car> findByLicensePlateNumberOptional(String licensePlateNumber) {
    return find("licensePlateNumber", licensePlateNumber).firstResultOptional();
  }

}
