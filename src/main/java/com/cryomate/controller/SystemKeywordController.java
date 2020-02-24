package com.cryomate.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cryomate.exception.ResourceNotFoundException;
import com.cryomate.model.SystemKeyword;
import com.cryomate.model.UserExample;
import com.cryomate.repository.SystemKeywordRepository;
import com.cryomate.repository.UserRepository;



@RestController
@RequestMapping("/api/v1")
public class SystemKeywordController {
	
	@Autowired
	private SystemKeywordRepository systemKeywordRepository;

	
	@GetMapping("/keyword")
	public List<SystemKeyword> getAllKeywords() {
		return systemKeywordRepository.findAll();
	}

	@GetMapping("/keyword/{id}")
	public ResponseEntity<SystemKeyword> getKeywordById(
			@PathVariable(value = "id") String keywordID) throws ResourceNotFoundException {
		SystemKeyword keyword = systemKeywordRepository.findById(keywordID)
        .orElseThrow(() -> new ResourceNotFoundException("Keyword not found :: " + keywordID));
		return ResponseEntity.ok().body(keyword);
	}

	@PostMapping("/keyword")
	public SystemKeyword createKeyword(@Valid @RequestBody SystemKeyword keyword) {
		return systemKeywordRepository.save(keyword);
	}

	@PutMapping("/keyword/{id}")
	public ResponseEntity<SystemKeyword> updateKeyword(
			@PathVariable(value = "id") String keywordID,
			@Valid @RequestBody SystemKeyword keywordDetails) throws ResourceNotFoundException {
		SystemKeyword keyword = systemKeywordRepository.findById(keywordID)
		        .orElseThrow(() -> new ResourceNotFoundException("keywordID not found :: " + keywordID));
		
		keyword.setDataType(keywordDetails.getDataType());
		keyword.setDefaultValue(keywordDetails.getDefaultValue());
		keyword.setDescription(keywordDetails.getDescription());
		keyword.setID(keywordDetails.getID());
		keyword.setKeyword(keywordDetails.getKeyword());
		
		
		final SystemKeyword updatedKeyword = systemKeywordRepository.save(keyword);
		return ResponseEntity.ok(updatedKeyword);
	}

	@DeleteMapping("/keyword/{id}")
	public Map<String, Boolean> deleteKeyword(
			@PathVariable(value = "id") String keywordID) throws ResourceNotFoundException {
		SystemKeyword keyword = systemKeywordRepository.findById(keywordID)
		        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + keywordID));

		systemKeywordRepository.delete(keyword);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
