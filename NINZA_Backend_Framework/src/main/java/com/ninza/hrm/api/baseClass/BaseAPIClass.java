package com.ninza.hrm.api.baseClass;
import java.sql.SQLException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.ninza.hrm.api.genericutility.DataBaseUtility;
import com.ninza.hrm.api.genericutility.FileUtility;
import com.ninza.hrm.api.genericutility.JavaUtility;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseAPIClass {

	public static JavaUtility jLib = new JavaUtility();
	public static DataBaseUtility dbLib = new DataBaseUtility();
	public static FileUtility fLib = new FileUtility();
	public static RequestSpecification specReqObj;
	public static ResponseSpecification specRespObj;

	@BeforeSuite
	public void configBS() throws Throwable {
		dbLib.getDbconnection();
		System.out.println("===========Connect to DB=============");
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setContentType(ContentType.JSON);
		// import static io.restassured.RestAssured.*;
		// builder.setAuth(RestAssured.basic("username", "password"));

		// builder.addHeader("", "");
		// builder.setAuth(basic("username", "password"));
		builder.setBaseUri(fLib.getDataFromPropertiesFile("baseURI"));
		specReqObj = builder.build();

		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectContentType(ContentType.JSON);
		specRespObj = resBuilder.build();
	}

	@AfterSuite
	public void configAS() throws SQLException {
		dbLib.closeDbconnection();
		System.out.println("==============Disconnect to DB============");
	}
}
