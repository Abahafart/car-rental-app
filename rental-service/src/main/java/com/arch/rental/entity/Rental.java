package com.arch.rental.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "rentals")
public class Rental extends PanacheMongoEntity {

    private String userId;
    private Long reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public Rental(String userId, Long reservationId, LocalDate startDate, LocalDate endDate,
        boolean active) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public Rental() {
    }

    public static Optional<Rental> findByUserAndReservationIdsOptional(String userId, Long reservationId) {
        return find("userId = ?1 and reservationId = ?2", userId, reservationId).firstResultOptional();
    }

    public static List<Rental> listActive() {
        return list("active", true);
    }

    public String getUserId() {
        return userId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Rental{" +
            "userId='" + userId + '\'' +
            ", reservationId=" + reservationId +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", active=" + active +
            ", id=" + id +
            '}';
    }
}
