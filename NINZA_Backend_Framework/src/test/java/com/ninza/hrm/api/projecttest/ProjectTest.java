package com.ninza.hrm.api.projecttest;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ninza.hrm.api.baseClass.BaseAPIClass;
import com.ninza.hrm.api.pojoclass.ProjectPojo;
import com.ninza.hrm.constant.endpoint.IendPoint;
import io.restassured.response.Response;

public class ProjectTest extends BaseAPIClass {
	ProjectPojo pObj;

	@Test
	public void addSingleProjectWithCreatedTest() throws Throwable {
		String expSucMsg = "Successfully Added";
		String projectName = "TestXY" + jLib.getRandomNumber();

		pObj = new ProjectPojo(projectName, "Created", "Jersey", 0);

		/*
		 * Verify the projectName in API layer
		 */
		Response resp = given().spec(specReqObj).body(pObj).when().post(IendPoint.add_proj);

		resp.then().assertThat().statusCode(201).assertThat().time(Matchers.lessThan(3000L)).assertThat()
				.spec(specRespObj).log().all();

		String actMsg = resp.jsonPath().get("msg");
		Assert.assertEquals(actMsg, expSucMsg);

		/*
		 * Verify the project in DB layer
		 */

		boolean flag = dbLib.executeQueryVerifyAndGetData("select * from project", 4, projectName);
		Assert.assertTrue(flag, "Project in DB is not verified");
	}

	@Test(dependsOnMethods = "addSingleProjectWithCreatedTest")
	public void createDuplicateProject() throws Throwable {
		given().spec(specReqObj).body(pObj).when().post(IendPoint.add_proj).then().assertThat()
				.statusCode(409).spec(specRespObj).log().all();
	}
}
