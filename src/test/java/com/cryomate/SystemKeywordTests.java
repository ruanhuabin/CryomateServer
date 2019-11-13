package com.cryomate;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cryomate.model.SystemKeyword;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemKeywordTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	private String getRootUrl() {
		return "http://localhost:" + port + "/api/v1";
	}
	
	
	@Test
	public void testCreateKeyword() {
		SystemKeyword keyword = new SystemKeyword();
		keyword.setID("def");
		keyword.setKeyword("Cs");
		keyword.setDataType("float");
		keyword.setDefaultValue("2.7");
		keyword.setDescription("Spherical aberration coefficient of objective lens in mm");		

		ResponseEntity<SystemKeyword> postResponse = restTemplate.postForEntity(getRootUrl() + "/keyword", keyword, SystemKeyword.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
		System.out.println("==============>Body: " + postResponse.getBody());
	}

}
