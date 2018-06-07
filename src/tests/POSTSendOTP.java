package tests;




import org.junit.Test;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class POSTSendOTP {
	
	public static void restSetter() {
		RestAssured.baseURI = "https://staging-api.dgpayglobal.com/v1/register/send";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
	}
	
	protected String baseURL = "https://staging-api.dgpayglobal.com/v1/register/send/otp";
	
	@Test 
	@Ignore
	// Test Bed
	public void PostTestBed() {
		RestAssured.baseURI = "https://staging-api.dgpayglobal.com/v1/register/send";
		RequestSpecification httpRequest = RestAssured.given();
		
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "82065852");
		
		httpRequest.body(requestParameters.toJSONString());
		//Response httpResponse = httpRequest.post();
		Response httpResponse = httpRequest.post("/otp");
		
		int statusCode = httpResponse.getStatusCode();
		String responseBody = httpResponse.getBody().asString();
		System.out.println("Response Body is: " + responseBody);
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		String successCode = httpResponse.jsonPath().get("message");
		Assert.assertEquals("Correct Success Code Returned", successCode, "successfully sent otp code");
	}	
	
	
	@Test
	// POST request response for valid SG phone number
	public void PostSendOTPToValidSGNumber() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "82065852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();
		
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		String successCode = httpResponse.jsonPath().get("message");
		Assert.assertEquals("Correct Success Code Returned", successCode, "successfully sent otp code");
	}
		
	
	@Test
	@Ignore
	// POST request response for valid Phillipines phone number
	public void PostSendOTPToValidPLPNumber() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "63");
		requestParameters.put("mobile_number", "468526345");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();
		
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		String successCode = httpResponse.jsonPath().get("message");
		Assert.assertEquals("Correct Success Code Returned", successCode, "successfully sent otp code");
	}	
	
	
	@Test
	public void PostSendOTPWithInvalidPrefix() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "82065852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();
		
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		String successCode = httpResponse.jsonPath().get("message");
		Assert.assertEquals("Correct Success Code Returned", successCode, "successfully sent otp code");
	}
	
	
	

		
		
		/*
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
		*/
	
	
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
		Response response = httpRequest.request(Method.GET, "/?page=2&limit=10");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is: " + responseBody);

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
	@Ignore
	// GET invalid request response for full list of countries with no optional parameters passed.
	public void GetAllCountriesInvalidRequest() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "");

		JsonPath jsonPathEvaluator = response.jsonPath();
		String errorMessage = jsonPathEvaluator.get("message");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 404, statusCode);
		Assert.assertEquals("Error Message.", "API endpoint does not exist", errorMessage);
	}
	
	
	@Test 
	@Ignore
	// GET request response for full list of countries with incorrect page parameter
	public void GetAllCountriesIncorrectPageValue() {
		RestAssured.baseURI = "http://staging-api.dgpayglobal.com/v1/countries";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "?page=20");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is: " + responseBody);

		JsonPath jsonPathEvaluator = response.jsonPath();
		String errorMessage = jsonPathEvaluator.get("links.last");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Maximum Pagination Limit Message.", "https://staging-api.dgpayglobal.com/v1/countries?page=4", errorMessage);
	}
	
	
	//@Test 
	// GET bad request response for full list of countries with correct page parameter
	// UNABLE to get 400 Bad Request for GET call. This would occur when a db issue/connection issue occurs. Cannot be initiated from the UI side.
	

}
