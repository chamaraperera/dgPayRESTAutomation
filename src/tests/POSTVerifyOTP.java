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
	protected String codeOTP = "892840";
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
	// generate one use at 1.59 min - done
	// generate one use at 2 min - done
	// generate one use at 2.01 min - done

	@Test
	@Ignore
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

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage, "the OTP you have entered is incorrect, please try again");
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
	@Ignore
	// POST request verify for valid OTP number 1.59mins after generation
	public void PostVerifyValidOTPNumberWith159Delay() throws InterruptedException {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate new OTP Code
		//generateNewOTP();
		//phoneOTP = "987654";
				
		TimeUnit.MINUTES.sleep(1);
		TimeUnit.SECONDS.sleep(58);
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully verified otp code");
	}
	
	@Test
	@Ignore
	// POST request verify for valid OTP number 2mins after generation
	public void PostVerifyValidOTPNumberWith2Delay() throws InterruptedException {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = codeOTP;

		// Generate new OTP Code
		//generateNewOTP();
		//phoneOTP = "987654";
				
		TimeUnit.MINUTES.sleep(2);
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage, "successfully verified otp code");
	}
	
	@Test
	//@Ignore
	// POST request verify for valid OTP number 2.2mins after generation
	public void PostVerifyValidOTPNumberWith220Delay() throws InterruptedException {
		phonePrefix = "65";
		phoneNumber = "82065852";
		phoneOTP = "208794";

		// Generate new OTP Code
		//generateNewOTP();
		//phoneOTP = "987654";
				
		TimeUnit.MINUTES.sleep(2);
		TimeUnit.SECONDS.sleep(30);
		verifyOTP("65", "82065852", phoneOTP);

		Assert.assertEquals("Correct status code returned.", 400, statusCode);
		Assert.assertEquals("Correct Error Message Returned", statusMessage, "expired otp code");
	}

}
