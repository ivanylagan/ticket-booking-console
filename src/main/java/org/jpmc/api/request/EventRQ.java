package org.jpmc.api.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EventRQ implements Serializable {

    private Long eventNumber;
    private Integer rowCount;
    private Integer seatsPerRow;
    private Long cancellationWindow;

    public EventRQ(Long eventNumber, Integer rowCount, Integer seatsPerRow, Long cancellationWindow) {
        this.eventNumber = eventNumber;
        this.rowCount = rowCount;
        this.seatsPerRow = seatsPerRow;
        this.cancellationWindow = cancellationWindow;
    }

    public Long getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(Long eventNumber) {
        this.eventNumber = eventNumber;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(Integer seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public Long getCancellationWindow() {
        return cancellationWindow;
    }

    public void setCancellationWindow(Long cancellationWindow) {
        this.cancellationWindow = cancellationWindow;
    }
}
