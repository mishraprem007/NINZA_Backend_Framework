package com.ninza.hrm.api.projecttest;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ninza.hrm.api.genericutility.DataBaseUtility;
import com.ninza.hrm.api.genericutility.FileUtility;
import com.ninza.hrm.api.genericutility.JavaUtility;
import com.ninza.hrm.api.pojoclass.ProjectPojo;
import com.ninza.hrm.constant.endpoint.IendPoint;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ProjectTest {
	ProjectPojo pObj;

	JavaUtility jLib = new JavaUtility();
	FileUtility fLib = new FileUtility();
	DataBaseUtility dbLib = new DataBaseUtility();

	@Test
	public void addSingleProjectWithCreatedTest() throws Throwable {

		String baseURI = fLib.getDataFromPropertiesFile("baseURI");
		String expSucMsg = "Successfully Added";
		String projectName = "JERSEY_" + jLib.getRandomNumber();

		pObj = new ProjectPojo(projectName, "Created", "Jersey", 0);

		/*
		 * Verify the projectName in API layer
		 */
		Response resp = given().contentType(ContentType.JSON).body(pObj).when().post(baseURI +IendPoint.add_proj);

		resp.then().assertThat().statusCode(201).assertThat().time(Matchers.lessThan(3000L)).assertThat()
				.contentType(ContentType.JSON).log().all();

		String actMsg = resp.jsonPath().get("msg");
		Assert.assertEquals(actMsg, expSucMsg);

		/*
		 * Verify the project in DB layer
		 */
		dbLib.getDbconnection();
		boolean flag = dbLib.executeQueryVerifyAndGetData("select * from project", 4, projectName);
		Assert.assertTrue(flag, "Project in DB is not verified");
		dbLib.closeDbconnection();
	}

	@Test(dependsOnMethods = "addSingleProjectWithCreatedTest")
	public void createDuplicateProject() throws Throwable {
		String baseURI = fLib.getDataFromPropertiesFile("baseURI");
		given().contentType(ContentType.JSON).body(pObj).when().post(baseURI +IendPoint.add_proj).then().assertThat()
				.statusCode(409).log().all();
	}

}
