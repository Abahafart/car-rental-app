package com.arch.rest;

import com.arch.entity.Reservation;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/admin/reservations")
public interface ReservationCrudResource extends PanacheEntityResource<Reservation, Long> {

}
