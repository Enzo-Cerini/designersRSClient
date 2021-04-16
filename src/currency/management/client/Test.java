package currency.management.client;
import java.util.ArrayList;


import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.apache.cxf.jaxrs.client.*;
import currency.management.web.data.*;

/**
 * @file Test.java
 * @brief Classe représentant le client RS
 * @author OBEYESEKARA Avishka, CERINI Enzo
 * @version 1.0
 * @date 15/04/2021
 *
 * Classe permettant de tester à partir du client les fonctions associées aux Currency.
 */

public class Test {
	private static String webServiceUrl =
	"http://localhost:8088/designersRS/api/currency";
	public static void main(String[] args) {
		
		
		add("Enzo",25);
		getConversion(1, "Euro", "Enzo");
		
		add("Avishka",32);
		getConversion(1, "Euro", "Avishka");
		
		update("A",12);
		
		update("Enzo",20);
		getConversion(1, "Euro", "Enzo");
		
		
		getConversion(22.3,"Dollar","Yen");
		getConversion(50, "Euro", "Roupie");
		getConversion(15, "Livre", "Yen");
		
		getInfos("Dollar");
		getInfos("Roupie");
	}
	
	
	private static Integer add(String name, double value) {
		System.out.print("Adding " + name + "... ");
		String newURL = webServiceUrl;
		newURL += "/adding/" + name + "/" + value;
		
		WebClient webClient = WebClient.create(newURL);
		Currency currency = new Currency(name, value);
		Response r = webClient.post(currency);
		if(r.getStatus() == 400) {
			System.out.println("Adding for "+ name +" is impossible! \n");
			return null;
		}
		String uri = r.getHeaderString("Content-Location");
		System.out.println("The currency "+ name + " has been added with the value "+ value + "\n");
		return 0;
		
	}
	
	
	private static Boolean update(String name, double newRate) {
		String newURL = webServiceUrl;
		newURL += "/update/" + name + "/" + newRate;
		
		System.out.print("Update " + name + " to " + newRate + " ... ");
		
		WebClient webClient1 = WebClient.create(newURL);
		int status1 = webClient1.put(name).getStatus();
		int status2 = webClient1.put(newRate).getStatus();
		
		if(status1 <300 && status2 < 300 ) {
			System.out.println("The currency named "+ name + " has been modified with the value " + newRate + "\n");
			return true;
		}
		System.out.println("The update of the currency named "+ name + " with the value " + newRate + " did not achieved !\n" );
		return false;
	}
	
	private static int getConversion(double monnaieEntree, String typeEntree, String typeSortie) {
		System.out.print("Getting conversion from " + monnaieEntree + " " + typeEntree +  " into " + typeSortie + " ... \n");
		String newURL = webServiceUrl;
		newURL += "/conversion/" + monnaieEntree + "/" + typeEntree + "/" + typeSortie;
		
		WebClient webClient = WebClient.create(newURL);
		int status= webClient.get().getStatus();
		double response = webClient.get(Double.class);
		
		if(status <300) {
			System.out.println(monnaieEntree + " " + typeEntree + " => " + response + " " + typeSortie + "\n");
		}
		else {
			System.out.println("The conversion was not successful ! \n");
		}
		
		return status;
	}
	
	private static String getInfos(String currency) {
		System.out.println("Getting information from " + currency);
		String newURL = webServiceUrl;
		newURL += "/info/" + currency;
		
		WebClient webClient = WebClient.create(newURL);
		int status= webClient.get().getStatus();
		String response = webClient.get(String.class);
		
		if(status <300) {
			System.out.println(response + "\n");
		}
		else {
			System.out.println("Cannot find information about " + currency + " ! \n");
		}
		
		return response;
	}
	
	
}
