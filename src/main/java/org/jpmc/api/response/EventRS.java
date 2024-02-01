package org.jpmc.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EventRS implements Serializable {
    private Long eventNumber;

    public Long getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(Long eventNumber) {
        this.eventNumber = eventNumber;
    }
}
