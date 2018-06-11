package tests;

import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class POSTRegister {

	// Variables for individual tests
	// ---------------------------------------------------------------------------------------------
	protected String phonePrefix = "";
	protected String phoneNumber = "";
	protected String newPassword = "qwer1234A";
	protected String confirmPassword = "qwer1234A";
	protected int languageID = 1;
	protected int remittanceLimit = 0;
	protected int acceptTerms = 1;

	protected static int statusCode = 0;
	protected static String statusMessage = "";
	protected static String statusError = "";

	// Variables for the Register methods
	// ------------------------------------------------------------------------------
	protected String registerURL = "https://staging-api.dgpayglobal.com/v1/register";

	// Script to verify OTP
	// -------------------------------------------------------------------------------------------------------
	// @Test
	// @Ignore
	HashMap map = new HashMap();
	
	public void docBody() {
		map.put("documents.[0].document_request_type_id", 1);
		map.put("documents.[0].document_request_subtype_id", null);
		map.put("documents.[0].attachments", "");
		map.put("documents.[0].attachments.[0].filename", "");
		map.put("documents.[0].attachments.[0].original_filename", "");
		map.put("documents.[0].attachments.[0].portion", "front");
	}
	
	
	
	
	
	public void customerRegister(String prefix, String number, String password, String confirmedPassword, int langID,
			int remLimit, int isAccept) {

		RestAssured.baseURI = registerURL;
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParameters = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		// main body
		requestParameters.put("mobile_prefix", prefix);
		requestParameters.put("mobile_number", number);
		requestParameters.put("password", password);
		requestParameters.put("password_confirmation", confirmedPassword);
		requestParameters.put("language_id", langID);
		requestParameters.put("has_remittance_limit", remLimit);
		requestParameters.put("is_accept", isAccept);
		//requestParameters.put("documents", "");
		// documents data objects
		// doc1
		
		
		
		requestParameters.put("documents.[0].document_request_type_id", 1);
		requestParameters.put("documents.[0].document_request_subtype_id", null);
		requestParameters.put("documents.[0].attachments", "");
		requestParameters.put("documents.[0].attachments.[0].filename", "");
		requestParameters.put("documents.[0].attachments.[0].original_filename", "");
		requestParameters.put("documents.[0].attachments.[0].portion", "front");
		// doc2
		requestParameters.put("documents.[1].document_request_type_id", 2);
		requestParameters.put("documents.[1].document_request_subtype_id", null);
		requestParameters.put("documents.[1].attachments", "");
		requestParameters.put("documents.[1].attachments.[1].filename", "");
		requestParameters.put("documents.[1].attachments.[1].original_filename", "");
		requestParameters.put("documents.[1].attachments.[1].portion", "front");
		// doc3
		requestParameters.put("documents.[2].document_request_type_id", 3);
		requestParameters.put("documents.[2].document_request_subtype_id", null);
		requestParameters.put("documents.[2].attachments", "");
		requestParameters.put("documents.[2].attachments.[2].filename", "");
		requestParameters.put("documents.[2].attachments.[2].original_filename", "");
		requestParameters.put("documents.[2].attachments.[2].portion", "back");		
		// doc4
		requestParameters.put("documents.[3].document_request_type_id", 4);
		requestParameters.put("documents.[3].document_request_subtype_id", null);
		requestParameters.put("documents.[3].attachments", "");
		requestParameters.put("documents.[3].attachments.[3].filename", "");
		requestParameters.put("documents.[3].attachments.[3].original_filename", "");
		requestParameters.put("documents.[3].attachments.[3].portion", "front");
		
		
		
		
		
		
		
		
		
		
		
		httpRequest.body(requestParameters.toJSONString());
		Response httpResponse = httpRequest.post();

		statusCode = httpResponse.getStatusCode();
		String responseBody = httpResponse.getBody().asString();
		System.out.println("Response Body is: " + responseBody);
		statusMessage = httpResponse.jsonPath().get("message");
		statusError = httpResponse.jsonPath().get("error");

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("Correct Success Message Returned", statusMessage, "successfully verified otp code");
	}

	@Test
	public void PostTestComplex() {
		phonePrefix = "65";
		phoneNumber = "82065852";
		newPassword = "qwer1234A";
		confirmPassword = "qwer1234A";
		languageID = 1;
		remittanceLimit = 0;
		acceptTerms = 1;

		customerRegister(phonePrefix, phoneNumber, newPassword, confirmPassword, languageID, remittanceLimit,
				acceptTerms);

	}

	
}
