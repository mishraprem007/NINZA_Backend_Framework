package com.ninza.hrm.api.genericutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Driver;

public class DataBaseUtility {
	ResultSet result;
	Connection con;
	FileUtility fLib = new FileUtility();

	public void getDbconnection(String url, String username, String password) {

		try {
			Driver driver = new Driver();
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {

		}
	}

	public void getDbconnection() throws Throwable {

		try {
			Driver driver = new Driver();
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection(fLib.getDataFromPropertiesFile("dbURL"),
					fLib.getDataFromPropertiesFile("db_Username"), fLib.getDataFromPropertiesFile("db_Password"));
		} catch (Exception e) {

		}
	}

	public void closeDbconnection() throws SQLException {
		try {
			con.close();
		} catch (Exception e) {

		}
	}

	public ResultSet executeSelectQuery(String query) throws Throwable {
		ResultSet result = null;
		try {
			Statement stat = con.createStatement();
			result = stat.executeQuery(query);
		} catch (Exception e) {
		}
		return result;
	}

	public int executeNonSelectQuery(String query) {
		int result = 0;
		try {
			Statement stat = con.createStatement();
			result = stat.executeUpdate(query);
		} catch (Exception e) {
		}
		return result;
	}

	public boolean executeQueryVerifyAndGetData(String query, int columnIndex, String expectedData) throws SQLException {
		boolean flag = false;
		result = con.createStatement().executeQuery(query);
		while (result.next()) {
			if (result.getString(columnIndex).equals(expectedData)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			System.out.println(expectedData + " ====> data verified in data base table");
			return true;
		} else {
			System.out.println(expectedData + " ====> data is not verified in data base table");
			return false;
		}

	}
}
