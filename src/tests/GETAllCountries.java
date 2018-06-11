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

public class GETAllCountries {

	// Variables for individual tests
	// ---------------------------------------------------------------------------------------------------------------------
	protected String currencyCodeOne = "";
	protected int iDOne = 0;
	protected String currencyCodeLast = "";
	protected int iDLast = 0;
	protected static int statusCode = 0;
	protected static String statusMessage = "";
	protected static String statusError = "";

	protected String baseGetCountriesURL = "http://staging-api.dgpayglobal.com/v1/countries";
	protected String headerContentType = "Content-Type\", \"application/json";

	public void getAllCountries(String getPath, String jsonMsgPath, String jsonErrPath, String firstID, String lastID,
			String firstCurrencyCode, String lastCurrencyCode) {
		RestAssured.baseURI = baseGetCountriesURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, getPath);

		// use jsonPathEvaluator to get first and last data object values
		JsonPath jsonPathEvaluator = response.jsonPath();
		currencyCodeOne = jsonPathEvaluator.get(firstCurrencyCode);
		iDOne = jsonPathEvaluator.get(firstID);
		currencyCodeLast = jsonPathEvaluator.get(lastCurrencyCode);
		iDLast = jsonPathEvaluator.get(lastID);
		statusCode = response.getStatusCode();

		String statusMessage = jsonPathEvaluator.get("message");
		String statusError = jsonPathEvaluator.get("error");

	}

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
	public void GetAllCountriesFullList1() {
		getAllCountries("", "message", "error", "data[0].id", "data[-1].id", "data[0].currency_code",
				"data[-1].currency_code");

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("First Country ID.", 2, iDOne);
		Assert.assertEquals("First Currency Code.", "ALL", currencyCodeOne);
		Assert.assertEquals("Last Country ID.", 159, iDLast);
		Assert.assertEquals("Last Currency Code.", "ZWL", currencyCodeLast);
	}

	@Test
	// @Ignore
	// GET response for page 2 of the list of countries
	public void GetAllCountriesByPage() {
		getAllCountries("/?page=2", "message", "error", "data[0].id", "data[-1].id", "data[0].currency_code",
				"data[-1].currency_code");

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("First Country ID.", 13, iDOne);
		Assert.assertEquals("First Currency Code.", "BGN", currencyCodeOne);
		Assert.assertEquals("Last Country ID.", 62, iDLast);
		Assert.assertEquals("Last Currency Code.", "FJD", currencyCodeLast);
	}

	@Test
	@Ignore
	// GET response for page 2 with a limit of 10 for the list of countries -
	// OPTIONAL PARAMETER PAGE IS NOT WORKING!!
	public void GetLimitedCountriesByPage() {
		getAllCountries("/?page=2&limit=10", "message", "error", "data[0].id", "data[-1].id", "data[0].currency_code",
				"data[-1].currency_code");

		Assert.assertEquals("Correct status code returned.", 200, statusCode);
		Assert.assertEquals("First Country ID.", 13, iDOne);
		Assert.assertEquals("First Currency Code.", "BGN", currencyCodeOne);
		Assert.assertEquals("Last Country ID.", 42, iDLast);
		Assert.assertEquals("Last Currency Code.", "EUR", currencyCodeLast);
	}

	@Test
	//@Ignore
	// GET invalid request response for full list of countries with no optional
	// parameters passed.
	public void GetAllCountriesInvalidRequest() {
		getAllCountries("", "message", "error", "data[0].id", "data[-1].id", "data[0].currency_code",
				"data[-1].currency_code");

		Assert.assertEquals("Correct status code returned.", 404, statusCode);
		Assert.assertEquals("Error Message.", "API endpoint does not exist", statusMessage);
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
		Assert.assertEquals("Maximum Pagination Limit Message.",
				"https://staging-api.dgpayglobal.com/v1/countries?page=4", errorMessage);
	}

	// @Test
	// GET bad request response for full list of countries with correct page
	// parameter
	// UNABLE to get 400 Bad Request for GET call. This would occur when a db
	// issue/connection issue occurs. Cannot be initiated from the UI side.

}
