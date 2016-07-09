package net.gunther.app.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.gunther.testing.TJWSEmbeddedJaxrsCdiServer;

public class GreetingsResourceTest {

	private static TJWSEmbeddedJaxrsCdiServer server = new TJWSEmbeddedJaxrsCdiServer();

	@BeforeClass
	public static void startEmbeddedServer() {
		server.start();
		server.getDeployment().getRegistry().addPerRequestResource(GreetingsResource.class, "/");
	}

	@AfterClass
	public static void shutdownServer() {
		server.stop();
	}

	@Test
	public void greet() throws IOException {
		URL url = new URL("http://localhost:" + server.PORT + "/greetings");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
	}
}
