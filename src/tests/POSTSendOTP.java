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

	// Variables for individual tests
	// ---------------------------------------------------------------------------------------------------------------------
	protected String phonePrefix = "";
	protected String phoneNumber = "";
	protected String phoneOTP = "";
	protected static int statusCode = 0;
	protected static String statusMessage = "";
	protected static String statusError = "";
	
	protected String baseSendOTPURL = "https://staging-api.dgpayglobal.com/v1/register/send/otp";
	protected String headerContentType = "Content-Type\", \"application/json";
	
	public void sendOTP(String prefix, String number, String jsonMsgPath, String jsonErrPath) {
		RestAssured.baseURI = baseSendOTPURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", prefix);
		requestParameters.put("mobile_number", number);

		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		statusCode = httpResponse.getStatusCode();
		// String responseBody = httpResponse.getBody().asString();
		// System.out.println("Response Body is: " + responseBody);
		//statusMessage = httpResponse.jsonPath().get("message");
		statusMessage = httpResponse.jsonPath().get(jsonMsgPath);
		//statusError = httpResponse.jsonPath().get("error");
		statusError = httpResponse.jsonPath().get(jsonErrPath);
	}
	
	
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
	//@Ignore
	// POST request response for valid SG phone number
	public void PostSendOTPToValidSGNumber() {
		phonePrefix = "65";
		phoneNumber = "82065852";
		
		sendOTP(phonePrefix, phoneNumber, "message", "error");
		
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully sent otp code");
	}

	@Test
	@Ignore
	// POST request response for valid Phillipines phone number
	public void PostSendOTPToValidPLPNumber() {
		phonePrefix = "65";
		phoneNumber = "82065852";
		
		sendOTP(phonePrefix, phoneNumber, "message", "error");
		
		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully sent otp code");
	}

	@Test
	//@Ignore
	// POST request response for invalid prefix
	public void PostSendOTPWithInvalidPrefix() {
		phonePrefix = "!@65";
		phoneNumber = "82065852";
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors.mobile_prefix");
		
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", statusError, "The mobile prefix must be between 1 and 5 digits.");
	}

	@Test
	//@Ignore
	// POST request response for invalid number
	public void PostSendOTPWithInvalidNumber() {
		phonePrefix = "!@65";
		phoneNumber = "%82065852A";
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors.mobile_number");
		
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", statusError, "The mobile number must be between 1 and 15 digits.");
	}
	

	@Test
	//@Ignore
	// POST request response for empty prefix
	public void PostSendOTPWithEmptyPrefix() {
		phonePrefix = "";
		phoneNumber = "82065852";
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors.mobile_prefix");
		
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", statusError, "The mobile prefix field is required.");
	}
	
	
	@Test
	//@Ignore
	// POST request response for empty number
	public void PostSendOTPWithEmptyNumber() {
		phonePrefix = "65";
		phoneNumber = "";
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors.mobile_number");

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", statusError, "The mobile number field is required.");
	}
	
	
	@Test
	//@Ignore
	// POST request response for long prefix
	public void PostSendOTPWithLongPrefix() {
		phonePrefix = "98765432";
		phoneNumber = "82065852";
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors.mobile_prefix");

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Code Returned", statusError, "The mobile prefix must be between 1 and 5 digits.");
	}
	
	
	@Test
	//@Ignore
	// POST request response for long number
	public void PostSendOTPWithLongNumber() {
		phonePrefix = "65";
		phoneNumber = "1234567890123456789";
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors.mobile_number");
		
		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusError, "The mobile number must be between 1 and 15 digits.");
	}
	
	
	@Test
	@Ignore
	// POST request response for number field with invalid data type
	public void PostSendOTPWithInvalidDataTypeNumber() {
		phonePrefix = "65";
		phoneNumber = String.valueOf(8206.5852);
		
		sendOTP(phonePrefix, phoneNumber, "message", "errors");

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage, "The mobile number must be between 1 and 15 digits.");
	}
	
	
	
	
	
	
	
	
	
}
