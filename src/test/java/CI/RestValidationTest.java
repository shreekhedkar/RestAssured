package CI;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import Domain.Data;
import Domain.User;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestValidationTest {

	@Test
	public void restValidation() {

		given().log().all().when().get("https://reqres.in/api/users/2").then().log().all().assertThat().statusCode(200)
				.and().body("data.id", equalTo(2));

	}

	@Test
	public void restValidationMultiUsers() {

		List<Integer> list = given().param("page", 2).when().get("https://reqres.in/api/users").then().statusCode(200)
				.extract().jsonPath().getList("data.id");

		List<Integer> expectedList = new ArrayList<>();
		expectedList.add(7);
		expectedList.add(8);
		expectedList.add(9);
		expectedList.add(10);
		expectedList.add(11);
		expectedList.add(12);
		Assert.assertEquals(list, expectedList);
	}

	@Test
	public void restValidationMultiUsersUsingDomain() {

		User user = given().param("page", 2).when().get("https://reqres.in/api/users").then().statusCode(200).extract()
				.as(User.class);
		List<Integer> expected = user.getData().stream().map(Data::getId).collect(Collectors.toList());
		System.out.println("list::" + expected);
		Assert.assertTrue(expected.contains(7));

	}

	@Test
	public void testUserCreation() {

		RestAssured.baseURI = "https://reqres.in/";

		RequestSpecification req = RestAssured.given();

		req.body("{\r\n" + "\"name\": \"test\",\r\n" + "    \"job\": \"leader\"\r\n" + "}");
		Response response = req.log().all().post("api/users").then().log().all().statusCode(HttpStatus.SC_CREATED).and()
				.extract().response();

		String name = response.path("id");
		System.out.println(name);
	}

	@Test
	public void should_create_new_user() {

		String userPayload = "{\"name\": \"sachin\",\r\n" + "    \"job\": \"leader\"\r\n" + "}";
		Response response = given().body(userPayload).post("https://reqres.in/api/users").andReturn();

		String id = response.then().log().all().assertThat().statusCode(HttpStatus.SC_CREATED).extract().path("id");

		get("https://reqres.in/api/users/" + id);
			//.assertThat().statusCode(404).and().body("data.id", equalTo(id));
	}

	@Test
	public void testListUsers() {

//		given().when().get("https://reqres.in/api/users?page=2").then().log().all().assertThat().statusCode(200).and()
//				.body("data.id", hasItems(7, 8, 9, 10, 11, 12));

		List<Object> actualList = given().when().get("https://reqres.in/api/users?page=2").then().log().all()
				.assertThat().statusCode(200).extract().jsonPath().getList("data.id");
		List<Integer> expectedList = new ArrayList<Integer>();
		expectedList.add(7);
		expectedList.add(8);
		expectedList.add(9);
		expectedList.add(10);
		expectedList.add(11);
		expectedList.add(12);

		Assert.assertEquals(actualList.size(), 6);
		Assert.assertEquals(actualList, expectedList);
	}

}
