
import org.junit.jupiter.api.Test;
import org.testng.Assert;


import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;



public class GetAllCountries {
	
	@Test
	public void GetAllCountriesFullList() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/countries";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "");
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is: " + responseBody);
		
		int statusCode = response.getStatusCode();
		Headers allHeaders = response.headers();
		
		for (Header header : allHeaders) {
			System.out.println("Key: " + header.getName() + "Value: " + header.getValue());
		}
		Assert.assertEquals(statusCode, 200, "Correct status code returned.");
	}

}
