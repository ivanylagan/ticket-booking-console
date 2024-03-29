package org.jpmc.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CancelTicketRQ {

    private String phoneNumber;
    private String ticketNumber;

    public CancelTicketRQ(String phoneNumber, String ticketNumber) {
        this.phoneNumber = phoneNumber;
        this.ticketNumber = ticketNumber;
    }

    public CancelTicketRQ() {}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
