package tests;




import org.junit.Test;
import org.junit.Assert;
import org.junit.Ignore;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import resources.Constants;
//import net.thucydides.core.annotations.Title;


public class GetAllCountries {
	
	@Test 
	@Ignore
	// Test Bed
	public void TestBed() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/countries";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "");
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is: " + responseBody);
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		String lastCurrencyCode = jsonPathEvaluator.get("data[-1].currency_code");
		int lastId = jsonPathEvaluator.get("data[-1].id");
		
		int statusCode = response.getStatusCode();
		Headers allHeaders = response.headers();
		
		for (Header header : allHeaders) {
			System.out.println("Key: " + header.getName() + "Value: " + header.getValue());
		}
		
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		
		System.out.println("currency code :" + lastCurrencyCode);
		System.out.println("id :" + lastId);
		
	}
	
	@Test 
	@Ignore
	// GET response for full list of countries with no optional parameters passed.
	public void GetAllCountriesFullList() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/countries";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "");

		// use jsonPathEvaluator to get first and last data object values
		JsonPath jsonPathEvaluator = response.jsonPath();
		String firstCurrencyCode = jsonPathEvaluator.get("data[0].currency_code");
		int firstId = jsonPathEvaluator.get("data[0].id");
		String lastCurrencyCode = jsonPathEvaluator.get("data[-1].currency_code");
		int lastId = jsonPathEvaluator.get("data[-1].id");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("First Country ID.", 1, firstId);
		Assert.assertEquals("First Country Code.", "AED", firstCurrencyCode);
		Assert.assertEquals("Last Country ID.", 159, lastId);
		Assert.assertEquals("Last Country Code.", "ZWL", lastCurrencyCode);
	}
	
	@Test 
	@Ignore
	// GET response for page 2 of the list of countries
	public void GetAllCountriesByPage() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/countries";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "/?page=2");

		// use jsonPathEvaluator to get first and last data object values
		JsonPath jsonPathEvaluator = response.jsonPath();
		String firstCountryName = jsonPathEvaluator.get("data[0].name");
		int firstId = jsonPathEvaluator.get("data[0].id");
		String lastCountryName = jsonPathEvaluator.get("data[-1].name");
		int lastId = jsonPathEvaluator.get("data[-1].id");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("First Country ID.", 51, firstId);
		Assert.assertEquals("First Country Code.", "Lithuania", firstCountryName);
		Assert.assertEquals("Last Country ID.", 100, lastId);
		Assert.assertEquals("Last Country Code.", "Malaysia", lastCountryName);
	}
	
	@Test 
	@Ignore
	// GET response for page 2  with a limit of 10 for the list of countries - OPTIONAL PARAMETER PAGE IS NOT WORKING!!
	public void GetLimitedCountriesByPage() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/countries";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "/?limit=10&page=2");

		// use jsonPathEvaluator to get first and last data object values
		JsonPath jsonPathEvaluator = response.jsonPath();
		String firstCountryName = jsonPathEvaluator.get("data[0].name");
		int firstId = jsonPathEvaluator.get("data[0].id");
		String lastCountryName = jsonPathEvaluator.get("data[-1].name");
		int lastId = jsonPathEvaluator.get("data[-1].id");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("First Country ID.", 51, firstId);
		Assert.assertEquals("First Country Code.", "Lithuania", firstCountryName);
		Assert.assertEquals("Last Country ID.", 61, lastId);
		Assert.assertEquals("Last Country Code.", "Latvia", lastCountryName);
	}
	
	
	@Test 
	// GET response for full list of countries with no optional parameters passed.
	public void GetAllCountriesBadRequest() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body is: " + responseBody);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		//Assert.assertEquals("First Country ID.", 1, firstId);
		//Assert.assertEquals("First Country Code.", "AED", firstCurrencyCode);
		//Assert.assertEquals("Last Country ID.", 159, lastId);
		//Assert.assertEquals("Last Country Code.", "ZWL", lastCurrencyCode);
	
	}
	
	

}
