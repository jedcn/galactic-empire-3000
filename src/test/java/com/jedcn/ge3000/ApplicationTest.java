package com.jedcn.ge3000;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationTest {

	public void testMainMethodOutput() {
		// Redirect System.out to capture output
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outContent));

		try {
			// Call the main method
			Application.main(new String[] {});

			// Get the output and verify it contains the expected message
			String output = outContent.toString();
			assertTrue(output.contains("Welcome to Galactic Empire 3000"), "Output should contain welcome message");
		}
		finally {
			// Reset System.out
			System.setOut(originalOut);
		}
	}

}