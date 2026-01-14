package com.arch.users;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import com.arch.users.model.Car;
import com.arch.users.model.Reservation;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/")
public class ReservationsResource {

  @CheckedTemplate
  public static class Templates {
    public static native TemplateInstance index(LocalDate startDate, LocalDate endDate, String name);
    public static native TemplateInstance listofreservations(Collection<Reservation> reservations);
    public static native TemplateInstance availablecars(Collection<Car> cars, LocalDate startDate, LocalDate endDate);
  }

  @Inject
  SecurityContext securityContext;

  @RestClient
  ReservationsClient reservationsClient;

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance index(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
    if (Objects.isNull(startDate)) {
      startDate = LocalDate.now().plusDays(1);
    }
    if (Objects.isNull(endDate)) {
      endDate = LocalDate.now().plusDays(7);
    }
    return Templates.index(startDate, endDate, securityContext.getUserPrincipal().getName());
  }

  @GET
  @Path("/get")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getReservations() {
    Collection<Reservation> reservationCollection = reservationsClient.allReservations();
    return Templates.listofreservations(reservationCollection);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/available")
  public TemplateInstance getAvailableCars(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
    Collection<Car> availableCars = reservationsClient.availability(startDate, endDate);
    return Templates.availablecars(availableCars, startDate, endDate);
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  @Path("/reserve")
  public RestResponse<TemplateInstance> create(@RestForm LocalDate startDate,
      @RestForm LocalDate endDate, @RestForm Long carId) {
    Reservation reservation = new Reservation();
    reservation.startDay = startDate;
    reservation.endDay = endDate;
    reservation.carId = carId;
    reservationsClient.make(reservation);
    return RestResponse.ResponseBuilder.ok(getReservations())
        .header("HX-Trigger-After-Swap", "update-available-cars-list").build();
  }
}
