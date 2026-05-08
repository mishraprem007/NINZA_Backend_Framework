package com.ninza.hrm.api.employeetest;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ninza.hrm.api.baseClass.BaseAPIClass;
import com.ninza.hrm.api.pojoclass.EmployeePOJO;
import com.ninza.hrm.api.pojoclass.ProjectPojo;
import com.ninza.hrm.constant.endpoint.IendPoint;

public class EmployeeTest extends BaseAPIClass {

	@Test
	public void addEmployeeTest() throws Throwable {
		/*
		 * create an object to Pojo class
		 */

		String projectName = "TestXY_" + jLib.getRandomNumber();
		String userName = "user" + jLib.getRandomNumber();

		/*
		 * Api-1 - Add a project inside a server
		 */
		ProjectPojo pObj = new ProjectPojo(projectName, "Created", "Prem", 0);

		given().spec(specReqObj).body(pObj).when().post(IendPoint.add_proj).then().spec(specRespObj).log().all();

		/*
		 * Api-2 Add employee to same Project
		 */

		EmployeePOJO empObj = new EmployeePOJO("Architect", "01/01/1997", "jersey@gmail.com", userName, 10,
				"8435902135", projectName, "ROLE_ADMIN", userName);

		given().spec(specReqObj).body(empObj).when().post(IendPoint.add_emp).then().assertThat().statusCode(201)
				.assertThat().and().time(Matchers.lessThan(3000L)).spec(specRespObj).log().all();

		/*
		 * Verify emp name in DB Note: DB (SQL) index starts from 1
		 */
		boolean flag = dbLib.executeQueryVerifyAndGetData("select * from employee", 5, userName);
		Assert.assertTrue(flag, "Employee in DB is not verified");
	}

	@Test
	public void addEmployeeWithoutEmailTest() throws Throwable {
		/*
		 * create an object to Pojo class
		 */

		String projectName = "TestXY_" + jLib.getRandomNumber();
		String userName = "user" + jLib.getRandomNumber();

		/*
		 * Api-1 - Add a project inside a server
		 */
		ProjectPojo pObj = new ProjectPojo(projectName, "Created", "Prem", 0);

		given().spec(specReqObj).body(pObj).when().post(IendPoint.add_proj).then().spec(specRespObj).log().all();

		/*
		 * Api-2 Add employee to same Project
		 */

		EmployeePOJO empObj = new EmployeePOJO("Architect", "01/01/1997", "", userName, 10, "8435902135", projectName,
				"ROLE_ADMIN", userName);

		given().spec(specReqObj).body(empObj).when().post(IendPoint.add_emp).then().assertThat()
				.statusCode(500).spec(specRespObj).log().all();

	}

}
