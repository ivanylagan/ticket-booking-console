package org.jpmc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketBookingConsoleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
