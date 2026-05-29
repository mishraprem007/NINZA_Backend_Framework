package com.ninza.hrm.api.genericutility;
import static io.restassured.RestAssured.given;

/**
 * @author Premshankar Mishra
 */
import java.util.List;

import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;

public class JsonUtility {
	FileUtility fLib = new FileUtility();

	/**
	 * get the JSONdata from based on json complext xpath
	 * 
	 * @param resp
	 * @param jsonXpath
	 * @return
	 */

	public String getDataOnJsonPath(Response resp, String jsonXpath) {
		List<Object> list = JsonPath.read(resp.asString(), jsonXpath);
		return list.get(0).toString();
	}

	/**
	 * get the xmlData from based on xml complex xpath
	 * 
	 * @param resp
	 * @param xmlpath
	 * @return
	 */
	public String getDataOnXmlPath(Response resp, String xmlpath) {
		return resp.xmlPath().get(xmlpath);
	}

	public boolean verificationOnJsonPath(Response resp, String jsonXpath, String expectedData) {
		List<String> list = JsonPath.read(resp.asString(), jsonXpath);
		boolean flag = false;
		for (String str : list) {
			if (str.equals(expectedData)) {
				System.out.println(expectedData + "is available==>PASS");
				flag = true;
			}
		}
		if (flag == false) {
			System.out.println(expectedData + " is not available==>FAIL");
		}
		return flag;
	}

	public String getAcessToken() throws Throwable {
		Response resp = given().formParam("client_id", fLib.getDataFromPropertiesFile("client_id"))
				.formParam("client_secret", fLib.getDataFromPropertiesFile("client_secret"))
				.formParam("grant_type", "client_credentials").when()
				.post("http://49.249.29.4:8180/auth/realms/ninza/protocol/openid-connect/token");
		resp.then().log().all();
		/* capture data from the response */
		String token = resp.jsonPath().get("acess_token");
		return token;
	}
}
