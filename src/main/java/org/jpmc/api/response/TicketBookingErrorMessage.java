package org.jpmc.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TicketBookingErrorMessage {


    public TicketBookingErrorMessage() {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public TicketBookingErrorMessage(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }


}
