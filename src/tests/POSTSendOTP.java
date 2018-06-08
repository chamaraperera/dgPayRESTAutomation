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

	public static void getResponseBody(Response httpResponse) {
		String responseBody = httpResponse.getBody().asString();
		System.out.println("Response Body is: " + responseBody);
	}

	protected String baseURL = "https://staging-api.dgpayglobal.com/v1/register/send/otp";
	protected String headerContentType = "Content-Type\", \"application/json";

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
		// Response httpResponse = httpRequest.post();
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
	// POST request response for invalid prefix
	public void PostSendOTPWithInvalidPrefix() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "!@65");
		requestParameters.put("mobile_number", "82065852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		String errorCode = httpResponse.jsonPath().get("errors.mobile_prefix").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", errorCode, "The mobile prefix must be a number.");
	}

	@Test
	// POST request response for invalid number
	public void PostSendOTPWithInvalidNumber() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "%82065852A");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		String errorCode = httpResponse.jsonPath().get("errors.mobile_number").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", errorCode, "The mobile number must be a number.");
	}
	

	@Test
	// POST request response for empty prefix
	public void PostSendOTPWithEmptyPrefix() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "");
		requestParameters.put("mobile_number", "82065852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();
		
		String errorCode = httpResponse.jsonPath().get("errors.mobile_prefix").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", errorCode, "The mobile prefix field is required.");
	}
	
	
	@Test
	// POST request response for empty number
	public void PostSendOTPWithEmptyNumber() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		String errorCode = httpResponse.jsonPath().get("errors.mobile_number").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", errorCode, "The mobile number field is required.");
	}
	
	
	@Test
	// POST request response for long prefix
	public void PostSendOTPWithLongPrefix() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "98765432");
		requestParameters.put("mobile_number", "82065852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();
		
		String errorCode = httpResponse.jsonPath().get("errors.mobile_prefix").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", errorCode, "The selected mobile prefix is invalid.");
	}
	
	
	@Test
	// POST request response for long number
	public void PostSendOTPWithLongNumber() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "1234567890123456789");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		String errorCode = httpResponse.jsonPath().get("message").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", errorCode, "Error for 651234567890123456789: to address is too long");
	}
	
	
	@Test
	// POST request response for long number
	public void PostSendOTPWithInvalidDataTypeNumber() {
		RestAssured.baseURI = baseURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "8206.5852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		String errorCode = httpResponse.jsonPath().get("message").toString();
		int statusCode = httpResponse.getStatusCode();
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", errorCode, "Error for 658206.5852: to address is not numeric");
	}
	
	
	
	
	
	
	
	
	
}
