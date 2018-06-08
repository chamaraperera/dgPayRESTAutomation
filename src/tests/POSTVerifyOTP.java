package tests;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class POSTVerifyOTP {

	// Variables for individual tests
	// ---------------------------------------------------------------------------------------------
	protected String phonePrefix = "";
	protected String phoneNumber = "";
	protected String phoneOTP = "";
	protected String codeOTP = "605434";
	protected static int statusCode = 0;
	protected static String statusMessage = "";
	protected static String statusError = "";

	// Variables for the verify/generate OTP methods
	// ------------------------------------------------------------------------------
	protected String baseVerifyOTPURL = "https://staging-api.dgpayglobal.com/v1/register/verify/otp";
	protected static String baseSendOTPURL = "https://staging-api.dgpayglobal.com/v1/register/send/otp";

	// Script to verify OTP
	// -------------------------------------------------------------------------------------------------------
	public void verifyOTP(String prefix, String number, String codeOTP) {
		RestAssured.baseURI = baseVerifyOTPURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", prefix);
		requestParameters.put("mobile_number", number);
		requestParameters.put("code", codeOTP);

		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		statusCode = httpResponse.getStatusCode();
		// String responseBody = httpResponse.getBody().asString();
		// System.out.println("Response Body is: " + responseBody);
		statusMessage = httpResponse.jsonPath().get("message");
		statusError = httpResponse.jsonPath().get("error");
	}
	
	// Script to generate new OTP
	// ------------------------------------------------------------------------------------------------
	public static void generateNewOTP() {
		RestAssured.baseURI = baseSendOTPURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParameters.put("mobile_prefix", "65");
		requestParameters.put("mobile_number", "82065852");
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();
	}

	@Test
	@Ignore
	public void PostTestBed1() {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully verified otp code");
	}

	// double send otp - done
	// generate two and use the first one - done
	// generate one verify then generate one again and use the old one - done
	// generate one verify then generate one again and use the new one - done
	// generate one use at 1.59 min
	// generate one use at 2 min
	// generate one use at 2.01 min

	@Test
	// @Ignore
	// POST request verify for valid OTP number
	public void PostVerifyValidOTPNumber() {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully verified otp code");
	}

	@Test
	@Ignore
	// POST request verify for same valid OTP number for the 2nd time
	public void PostVerifyTwiceValidOTPNumber() {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		verifyOTP("65", "82065852", phoneOTP);
		// verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage,
				"the OTP you have entered is incorrect, please try again");
	}

	@Test
	@Ignore
	// POST request verify for old, non-expired previously generated OTP number
	public void PostVerifyOldOTPNumber() {

		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate 1st OTP Code
		generateNewOTP();
		phoneOTP = "546789";

		// Verify using current generated OTP
		verifyOTP("65", "82065852", phoneOTP);

		// Generate 2nd OTP Code
		generateNewOTP();

		// Verify using previous generated OTP
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage,
				"the OTP you have entered is incorrect, please try again");
	}

	// generate one verify then generate one again and use the old one - done
	// generate one verify then generate one again and use the new one - done

	@Test
	@Ignore
	// POST request verify for current, non-expired newly generated OTP number
	public void PostVerifyNew2ndOTPNumber() {

		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate 1st OTP Code
		generateNewOTP();
		phoneOTP = "546789";

		// Verify using current generated OTP
		verifyOTP("65", "82065852", phoneOTP);

		// Generate 2nd OTP Code
		generateNewOTP();
		phoneOTP = "987654";

		// Verify using newly generated OTP
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Veirfy Message Returned", statusMessage, "successfully verified otp code");
	}

	// generate one use at 1.59 min
	// generate one use at 2 min
	// generate one use at 2.01 min

	@Test
	// @Ignore
	// POST request verify for valid OTP number 1.59mins after generation
	public void PostVerifyValidOTPNumberWith159Delay() throws InterruptedException {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate new OTP Code
		generateNewOTP();
		phoneOTP = "987654";
				
		TimeUnit.MINUTES.sleep(1);
		TimeUnit.SECONDS.sleep(58);
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully verified otp code");
	}
	
	@Test
	// @Ignore
	// POST request verify for valid OTP number 2mins after generation
	public void PostVerifyValidOTPNumberWith2Delay() throws InterruptedException {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate new OTP Code
		generateNewOTP();
		phoneOTP = "987654";
				
		TimeUnit.MINUTES.sleep(2);
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage, "the OTP you have entered is incorrect, please try again");
	}
	
	@Test
	 @Ignore
	// POST request verify for valid OTP number 2.2mins after generation
	public void PostVerifyValidOTPNumberWith220Delay() throws InterruptedException {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate new OTP Code
		generateNewOTP();
		phoneOTP = "987654";
				
		TimeUnit.MINUTES.sleep(2);
		TimeUnit.SECONDS.sleep(20);
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage, "the OTP you have entered is incorrect, please try again");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	@Ignore
	// POST request response for invalid prefix
	public void PostSendOTPWithInvalidPrefix() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
	@Ignore
	// POST request response for invalid number
	public void PostSendOTPWithInvalidNumber() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
	@Ignore
	// POST request response for empty prefix
	public void PostSendOTPWithEmptyPrefix() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
	@Ignore
	// POST request response for empty number
	public void PostSendOTPWithEmptyNumber() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
	@Ignore
	// POST request response for long prefix
	public void PostSendOTPWithLongPrefix() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
	@Ignore
	// POST request response for long number
	public void PostSendOTPWithLongNumber() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
		Assert.assertEquals("Correct Error Message Returned", errorCode,
				"Error for 651234567890123456789: to address is too long");
	}

	@Test
	@Ignore
	// POST request response for long number
	public void PostSendOTPWithInvalidDataTypeNumber() {
		RestAssured.baseURI = baseVerifyOTPURL;
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
		Assert.assertEquals("Correct Error Message Returned", errorCode,
				"Error for 658206.5852: to address is not numeric");
	}

}
