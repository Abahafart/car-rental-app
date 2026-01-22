package com.arch.reservation.entity;

import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Reservation extends PanacheEntity {

    public Long carId;
    public LocalDate startDay;
    public LocalDate endDay;
    public String userId;
    /**
     * Check if the given duration overlaps with this reservation
     * @return true if the dates overlap with the reservation, false
     * otherwise
     */
    public boolean isReserved(LocalDate startDay, LocalDate endDay) {
        return (!(this.endDay.isBefore(startDay) ||
                this.startDay.isAfter(endDay)));
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", endDay=" + endDay +
            ", startDay=" + startDay +
            ", carId=" + carId +
            '}';
    }
}
