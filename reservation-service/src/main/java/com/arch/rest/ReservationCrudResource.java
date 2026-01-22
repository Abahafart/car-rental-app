package com.arch.rest;

import com.arch.reservation.entity.Reservation;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/admin/reservations")
public interface ReservationCrudResource extends PanacheEntityResource<Reservation, Long> {

}
