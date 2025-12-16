package com.arch.reservation;

import java.util.List;

import com.arch.inventory.Car;
import com.arch.inventory.GraphQLInventoryClient;

import io.quarkus.test.Mock;
//Commented because it has conflict with mockito
//@Mock
//public class MockInventoryClient implements GraphQLInventoryClient {
//
//  @Override
//  public List<Car> allCars() {
//    Car car = new Car(1L, "ABC123", "MAZDA", "406");
//    return List.of(car);
//  }
//}
