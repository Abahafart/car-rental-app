package com.arch.users;

import java.time.LocalDate;
import java.util.Collection;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import com.arch.users.model.Car;
import com.arch.users.model.Reservation;

import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@AccessToken
@Path("/reservations")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface ReservationsClient {

    @GET
    @Path("/all")
    public Collection<Reservation> allReservations();

    @POST
    @Path("/make")
    public Reservation make(Reservation reservation);

    @GET
    @Path("/availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate);

}
