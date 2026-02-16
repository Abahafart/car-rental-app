package com.arch.reservation.billing;

import com.arch.reservation.entity.Reservation;

public class Invoice {

  public Reservation reservation;
  public double price;

  public Invoice(Reservation reservation, double price) {
    this.reservation = reservation;
    this.price = price;
  }

  @Override
  public String toString() {
    return "Invoice{" +
        "reservation=" + reservation +
        ", price=" + price +
        '}';
  }
}
