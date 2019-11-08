package MainClasses;

import Files.PayLoad;
import Files.Resources;
import TestRunner.MainClass;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.testng.Assert.assertEquals;

public class WeatherAPI extends MainClass {


    private Response response;
    private ValidatableResponse response_statusCode;
    RequestSpecification httpRequest = RestAssured.given();
    String responseBody;
    private String ID_value;
    private JsonPath jp = null;


    @Given("^WeatherAPI URI$")
    public void weatherapiURI() {
        // RestAssured.baseURI = props.getProperty("BASE");
        RestAssured.baseURI = "http://api.openweathermap.org";
    }

    @When("^I post the rest call creating a new station$")
    public void iPostTheRestCallCreatingANewStation() throws Throwable {
        // getData();
        response = given().header("Content-Type", "application/json")
                .queryParam("appid", "a474909b9bd0b5205ac147da58080c0f")
                .body(PayLoad.getWeatherData("Murthy100", "Tuni Urban Station")).when().post(Resources.getResource());

    }

    @Then("^the status code is \"([^\"]*)\"$")
    public void theStatusCodeIs(int status_Code) throws Throwable {
        response_statusCode = response.then().statusCode((status_Code));
    }

    @And("^I validate response$")
    public void iValidateResponse() {

       System.out.println("response of post request: " + response.prettyPrint());
       assertEquals(201, response.getStatusCode());
      //  assertThat(jp.get("name"), containsString("Tuni"));
    }

    @When("^I execute get call for weatherAPI to get single station details$")
    public void iExecuteGetCallForWeatherAPIToGetSingleStationDetails() throws Throwable {

        iPostTheRestCallCreatingANewStation();
        iExtractIDValueFromTheResponse();

        response = given().header("Content_Type", "application/json").
                queryParam("appid", "a474909b9bd0b5205ac147da58080c0f").when().get(Resources.getResource() + "/" + ID_value);

        System.out.println("response of get is " + response.prettyPrint());
    }


    @When("^I execute get call for weatherAPI to get all station details$")
    public void iExecuteGetCallForWeatherAPIToGetAllStationDetails() {
        response = given().header("Content_Type", "application/json").
                queryParam("appid", "a474909b9bd0b5205ac147da58080c0f").when().get(Resources.getResource());

        System.out.println("response of get is " + response.prettyPrint());
    }

    @When("^I execute put call for weatherAPI to update single station details$")
    public void iExecutePutCallForWeatherAPIToUpdateSingleStationDetails() throws Throwable {
        iPostTheRestCallCreatingANewStation();
        iExtractIDValueFromTheResponse();

        response = given().header("Content-Type", "application/json")
                .queryParam("appid", "a474909b9bd0b5205ac147da58080c0f")
                .body(PayLoad.getWeatherData("Murthy100", "Tuni Urban Station")).when().put(Resources.getResource() + "/" + ID_value);

        System.out.println("response of get is " + response.prettyPrint());
    }

    @When("^I execute delete call for weatherAPI to delete single station details$")
    public void iExecuteDeleteCallForWeatherAPIToDeleteSingleStationDetails() throws Throwable {

        iPostTheRestCallCreatingANewStation();
        iExtractIDValueFromTheResponse();

        response = given().header("Content-Type", "application/json")
                .queryParam("appid", "a474909b9bd0b5205ac147da58080c0f")
                .body(PayLoad.getWeatherData("Murthy100", "Tuni Urban Station")).when().delete(Resources.getResource() + "/" + ID_value);

    }

    @Then("^I extract ID value from the response$")
    public void iExtractIDValueFromTheResponse() {
        responseBody = response.asString();
       jp = new JsonPath(responseBody);
        ID_value = jp.getString("ID");

        System.out.println("ID value from response is : " + ID_value);
    }

    @And("^I get all ID details of all Stations$")
    public void iGetAllIDDetailsOfAllStations() {

        String ids = response.jsonPath().getString("id");
        System.out.println("All IDs of this appid are"  + ids);
    }
}

