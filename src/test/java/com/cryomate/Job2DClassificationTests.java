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

import com.cryomate.model.Job2DClassification;
import com.cryomate.model.SystemKeyword;
import com.cryomate.repository.Job2DClassificationRepository;
import com.cryomate.repository.SystemKeywordRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Job2DClassificationTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private Job2DClassificationRepository job2DRepository;
	
	private String getRootUrl() {
		return "http://localhost:" + port + "/api/v1";
	}
	
	
	@Test
	public void testInsertNew2DJob() {
		
		Job2DClassification job2D = new Job2DClassification();
		job2D.setID("ID001");
		job2DRepository.save(job2D);
		
	}

}
