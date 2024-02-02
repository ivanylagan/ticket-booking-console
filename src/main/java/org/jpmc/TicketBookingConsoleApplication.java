package org.jpmc;

import org.jpmc.api.request.CancelTicketRQ;
import org.jpmc.api.request.EventRQ;
import org.jpmc.api.request.TicketRQ;
import org.jpmc.api.response.*;
import org.jpmc.client.TicketBookingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class TicketBookingConsoleApplication implements CommandLineRunner {

	private final List<String> mainMenuOptions = Arrays.asList("1", "2", "3");

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private TicketBookingClient ticketBookingClient;

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Connecting to the backend.");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		boolean continueFlag = true;
		try {
			ResponseEntity<String> response = ticketBookingClient.healthCheck();
		} catch (Exception e) {
			System.out.println("An error occurred connecting to the backend. Please restart.");
			System.exit(SpringApplication.exit(context));
		}

		System.out.println("Welcome to Ticketing Console!");
		while(continueFlag) {
			String option = displayMainMenu();
			if (option.equalsIgnoreCase("1")) {
				displayAdminMenu();
			} else if (option.equalsIgnoreCase("2")) {
				displayBuyerMenu();
			} else if (option.equalsIgnoreCase("3")) {
				continueFlag = false;
				break;
			}
		}
	}

	String displayMainMenu() {
		System.out.println("---------------------------------------");
		System.out.println("Type '1' to login as ADMIN");
		System.out.println("Type '2' to login as BUYER");
		System.out.println("Type '3' to EXIT");
		System.out.print("Your choice: ");
		Scanner scanner = new Scanner(System.in);
		String option = scanner.nextLine();
		if (!mainMenuOptions.contains(option.toLowerCase().strip().trim())) {
			System.out.println("Invalid option! Please choose");
		}
		return option;
	}

	String displayAdminMenu() {
		boolean continueFlag = true;
		while (continueFlag) {
			displayAdminMenuText();
			Scanner scanner = new Scanner(System.in);
			String option = scanner.nextLine();
			String[] parameters = option.split(" ");
			if (parameters[0].equalsIgnoreCase("setup") && parameters.length == 5) {
				try {
					EventRQ request = new EventRQ(Long.parseLong(parameters[1]),
							Integer.parseInt(parameters[2]),
							Integer.parseInt(parameters[3]),
							Long.parseLong(parameters[4]));
					ResponseEntity<EventRS> response = ticketBookingClient.setupEvent(request);
					System.out.println(String.format("Event with event number %d has been created.", response.getBody().getEventNumber()));
				} catch (HttpStatusCodeException ex) {
					ErrorMessage error = ex.getResponseBodyAs(ErrorMessage.class);
					System.out.println(error.getMessage());
				}
			} else if (parameters[0].equalsIgnoreCase("view") && parameters.length == 2) {
				try {
					ResponseEntity<List<EventDetailRS>> response = ticketBookingClient.viewEventDetails(Long.parseLong(parameters[1]));
					List<EventDetailRS> details = response.getBody();
					System.out.println(String.format("Event Details of event number %d", Long.parseLong(parameters[1])));
					details.stream().forEach(detail -> {
						System.out.println(String.format("Ticket #: %s, Buyer Phone #: %s, Seat #: %s",
								detail.getTicketNumber(), detail.getPhoneNumber(), detail.getSeatNumber()));
					});
				} catch (HttpStatusCodeException ex) {
					ErrorMessage error = ex.getResponseBodyAs(ErrorMessage.class);
					System.out.println(error.getMessage());
				}
			} else if (parameters[0].equalsIgnoreCase("back") && parameters.length == 1) {
				continueFlag = false;
			} else {
				System.out.println("Invalid command. Please choose again");
			}
		}
		return null;
	}

	String displayBuyerMenu() {
		boolean continueFlag = true;
		while (continueFlag) {
			displayBuyerMenuText();
			Scanner scanner = new Scanner(System.in);
			String option = scanner.nextLine();
			String[] parameters = option.split(" ");
			if (parameters[0].equalsIgnoreCase("availability") && parameters.length == 2) {
				try {
					ResponseEntity<List<String>> response = ticketBookingClient.viewAvailableSeats(Long.parseLong(parameters[1]));
					System.out.println(String.format("Available seats: %s", String.join(",", response.getBody())));
				} catch (HttpStatusCodeException ex) {
					ErrorMessage error = ex.getResponseBodyAs(ErrorMessage.class);
					System.out.println(error.getMessage());
				}
			} else if (parameters[0].equalsIgnoreCase("book") && parameters.length == 4) {
				try {
					TicketRQ ticketRequest = new TicketRQ(Arrays.stream(parameters[3].split(",")).toList(), parameters[2]);
					ResponseEntity<List<TicketBookingRS>> response = ticketBookingClient.bookSeats(ticketRequest, Long.parseLong(parameters[1]));
					List<TicketBookingRS> details = response.getBody();
					details.stream().forEach(detail -> {
						if (detail.getTicketDetails() != null) {
							TicketDetailsRS ticketDetailsRS = detail.getTicketDetails();
							System.out.println(String.format("Ticket #: %s, Seat #: %s, Booking Date: %s",
									ticketDetailsRS.getTicketNumber(), ticketDetailsRS.getSeatNumber(), ticketDetailsRS.getBookingTimestamp()));
						} else if (detail.getError() != null) {
							TicketBookingErrorMessage errorMessage = detail.getError();
							System.out.println(String.format("%s", errorMessage.getMessage()));
						}
					});
				} catch (HttpStatusCodeException ex) {
					ErrorMessage error = ex.getResponseBodyAs(ErrorMessage.class);
					System.out.println(error.getMessage());
				}
			} else if (parameters[0].equalsIgnoreCase("cancel") && parameters.length == 3) {
				try {
					CancelTicketRQ ticketRequest = new CancelTicketRQ(parameters[2], parameters[1]);
					ResponseEntity<EventRS> response = ticketBookingClient.cancelTicket(ticketRequest);
					System.out.println("Ticket has been cancelled.");
				} catch (HttpStatusCodeException ex) {
					ErrorMessage error = ex.getResponseBodyAs(ErrorMessage.class);
					System.out.println(error.getMessage());
				}
			} else if (parameters[0].equalsIgnoreCase("back") && parameters.length == 1) {
				continueFlag = false;
			} else {
				System.out.println("Invalid command. Please choose again");
			}
		}
		return null;
	}

	private void displayAdminMenuText() {
		System.out.println("---------------------------------------");
		System.out.println("Commands executable by admin");
		System.out.println("Setup [Show Number] [Number of Rows] [Number of seats per row] [Cancellation window in minutes]");
		System.out.println("View [Show Number]");
		System.out.println("Back");
		System.out.print("Your choice: ");
	}

	private void displayBuyerMenuText() {
		System.out.println("---------------------------------------");
		System.out.println("Commands executable by buyer");
		System.out.println("Availability [Show Number]");
		System.out.println("Book [Show Number] [Phone Number] [Comma Separated List of Seats]");
		System.out.println("Cancel [Ticket Number] [Phone Number]");
		System.out.println("Back");
		System.out.print("Your choice: ");
	}

}


