package com.arch.reservation.billing;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BillingService {

  @Incoming("invoices")
  public void processInvoice(Invoice invoice) {
    Log.info("Processing received invoice: " + invoice);
  }

}
