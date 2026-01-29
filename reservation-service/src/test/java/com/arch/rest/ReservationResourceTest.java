package com.arch.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.arch.inventory.Car;
import com.arch.inventory.GraphQLInventoryClient;
import com.arch.reservation.entity.Reservation;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class ReservationResourceTest {

  @TestHTTPResource
  @TestHTTPEndpoint(ReservationResource.class)
  URL reservationResource;

  @TestHTTPEndpoint(ReservationResource.class)
  @TestHTTPResource("availability")
  URL availability;

  @Test
  public void testReservationIds() {
    Reservation reservation = new Reservation();
    reservation.carId = 1L;
    reservation.startDay = LocalDate.now().plusDays(5L);
    reservation.endDay = LocalDate.now().plusDays(11L);

    RestAssured.given().contentType(MediaType.APPLICATION_JSON).body(reservation).when().post(reservationResource)
        .then().statusCode(200).body("id", notNullValue());

  }

  @Test
  void testMakingAReservationAndCheckingAvailability() {
    GraphQLInventoryClient mock = Mockito.mock(GraphQLInventoryClient.class);
    Car car = new Car(1L, "ABC123", "MAZDA", "406");
    Mockito.when(mock.allCars()).thenReturn(Uni.createFrom().item(Collections.singletonList(car)));
    QuarkusMock.installMockForType(mock, GraphQLInventoryClient.class);

    String startDate = "2023-01-01";
    String endDate = "2023-01-11";
    Car[] cars = RestAssured.given().queryParam("startDate", startDate)
        .queryParam("endDate", endDate).when().get(availability)
        .then().statusCode(200).extract().body().as(Car[].class);
    Car car1 = cars[0];

    Reservation reservation = new Reservation();
    reservation.carId = car1.id;
    reservation.startDay = LocalDate.parse(startDate);
    reservation.endDay = LocalDate.parse(endDate);

    RestAssured.given().contentType(ContentType.JSON).body(reservation).when().post(reservationResource)
    .then().statusCode(200).body("carId", is(car1.id.intValue()));

    RestAssured.given().queryParam("startDate", startDate)
    .queryParam("endDate", endDate).when().get(availability)
    .then().statusCode(200).body("findAll {car -> car.id == " + car1.id + "}", hasSize(0));
  }
}