package org.jpmc.client;


import org.jpmc.api.request.CancelTicketRQ;
import org.jpmc.api.request.EventRQ;
import org.jpmc.api.request.TicketRQ;
import org.jpmc.api.response.EventDetailRS;
import org.jpmc.api.response.EventRS;
import org.jpmc.api.response.TicketBookingRS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TicketBookingClient {

    @Value("${app.properties.backend-url}")
    private String BACKEND_URL;

    @Value("${app.properties.backend-root-context}")
    private String BACKEND_ROOT_CONTEXT;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<EventRS> setupEvent(EventRQ request) throws HttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EventRQ> event = new HttpEntity<>(request);
        ResponseEntity<EventRS> response = restTemplate
                .exchange(BACKEND_URL + BACKEND_ROOT_CONTEXT + "/admin/events",
                        HttpMethod.POST, event, EventRS.class);
        return response;
    }

    public ResponseEntity<List<EventDetailRS>> viewEventDetails(Long eventNumber) throws HttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<List<EventDetailRS>> response = restTemplate
                .exchange(BACKEND_URL + BACKEND_ROOT_CONTEXT + "/admin/events/" + Long.toString(eventNumber) + "/details",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<EventDetailRS>>() {});
        return response;
    }

    public ResponseEntity<List<String>> viewAvailableSeats(Long eventNumber) throws HttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<List<String>> response = restTemplate
                .exchange(BACKEND_URL + BACKEND_ROOT_CONTEXT + "/events/" + Long.toString(eventNumber) + "/available-seats",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
        return response;
    }

    public ResponseEntity<List<TicketBookingRS>> bookSeats(TicketRQ request, Long eventNumber) throws HttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TicketRQ> event = new HttpEntity<>(request);
        ResponseEntity<List<TicketBookingRS>> response = restTemplate
                .exchange(BACKEND_URL + BACKEND_ROOT_CONTEXT + "/events/" + Long.toString(eventNumber) + "/book",
                        HttpMethod.POST, event, new ParameterizedTypeReference<List<TicketBookingRS>>() {});
        return response;
    }

    public ResponseEntity<EventRS> cancelTicket(CancelTicketRQ request) throws HttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CancelTicketRQ> event = new HttpEntity<>(request);
        ResponseEntity<EventRS> response = restTemplate
                .exchange(BACKEND_URL + BACKEND_ROOT_CONTEXT + "/events/cancel",
                        HttpMethod.DELETE, event, EventRS.class);
        return response;
    }

    public ResponseEntity<String> healthCheck() throws HttpStatusCodeException {
        ResponseEntity<String> response = restTemplate.getForEntity(BACKEND_URL + "/actuator/health", String.class);
        return response;
    }

}
