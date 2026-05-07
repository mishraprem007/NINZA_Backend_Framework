package com.ninza.hrm.api.employeetest;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ninza.hrm.api.genericutility.DataBaseUtility;
import com.ninza.hrm.api.genericutility.FileUtility;
import com.ninza.hrm.api.genericutility.JavaUtility;
import com.ninza.hrm.api.pojoclass.EmployeePOJO;
import com.ninza.hrm.api.pojoclass.ProjectPojo;
import com.ninza.hrm.constant.endpoint.IendPoint;

import io.restassured.http.ContentType;

public class EmployeeTest {
	JavaUtility jLib = new JavaUtility();
	DataBaseUtility dbLib = new DataBaseUtility();
	FileUtility fLib = new FileUtility();

	@Test
	public void addEmployeeTest() throws Throwable {
		/*
		 * create an object to Pojo class
		 */
		String baseURI = fLib.getDataFromPropertiesFile("baseURI");
		String dbURL = fLib.getDataFromPropertiesFile("dbURL");
		String db_Username = fLib.getDataFromPropertiesFile("db_Username");
		String db_Password = fLib.getDataFromPropertiesFile("db_Password");

		String projectName = "JERSEY_" + jLib.getRandomNumber();
		String userName = "user" + jLib.getRandomNumber();

		/*
		 * Api-1 - Add a project inside a server
		 */
		ProjectPojo pObj = new ProjectPojo(projectName, "Created", "Jersey", 0);

		given().contentType(ContentType.JSON).body(pObj).when().post(baseURI +IendPoint.add_proj).then().log().all();

		/*
		 * Api-2 Add employee to same Project
		 */

		EmployeePOJO empObj = new EmployeePOJO("Architect", "01/01/1997", "jersey@gmail.com", userName, 10,
				"8435902135", projectName, "ROLE_ADMIN", userName);

		given().contentType(ContentType.JSON).body(empObj).when().post(baseURI +IendPoint.add_emp).then().assertThat()
				.statusCode(201).assertThat().contentType(ContentType.JSON).and().time(Matchers.lessThan(3000L)).log()
				.all();

		/*
		 * Verify emp name in DB Note: DB (SQL) index starts from 1
		 */
		dbLib.getDbconnection();
		boolean flag = dbLib.executeQueryVerifyAndGetData("select * from employee", 5, userName);

		Assert.assertTrue(flag, "Employee in DB is not verified");
		dbLib.closeDbconnection();
	}

	@Test
	public void addEmployeeWithoutEmailTest() throws Throwable {
		/*
		 * create an object to Pojo class
		 */

		String projectName = "JERSEY_" + jLib.getRandomNumber();
		String userName = "user" + jLib.getRandomNumber();
		String baseURI = fLib.getDataFromPropertiesFile("baseURI");

		/*
		 * Api-1 - Add a project inside a server
		 */
		ProjectPojo pObj = new ProjectPojo(projectName, "Created", "Jersey", 0);

		given().contentType(ContentType.JSON).body(pObj).when().post(baseURI + IendPoint.add_proj).then().log().all();

		/*
		 * Api-2 Add employee to same Project
		 */

		EmployeePOJO empObj = new EmployeePOJO("Architect", "01/01/1997", "", userName, 10, "8435902135", projectName,
				"ROLE_ADMIN", userName);

		given().contentType(ContentType.JSON).body(empObj).when().post(baseURI +IendPoint.add_emp).then().assertThat()
				.statusCode(500).log().all();

	}
}
