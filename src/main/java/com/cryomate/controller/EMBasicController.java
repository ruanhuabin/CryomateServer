//package com.cryomate.controller;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cryomate.exception.ResourceNotFoundException;
//import com.cryomate.model.EMBasic;
//import com.cryomate.repository.EMBasicRepository;
//import com.cryomate.repository.UserRepository;
//
//
//
//@RestController
//@RequestMapping("/api/v1")
//public class EMBasicController {
//	
//	@Autowired
//	private EMBasicRepository systemKeywordRepository;
//
//	
//	@GetMapping("/embasic")
//	public List<EMBasic> getAllKeywords() {
//		return systemKeywordRepository.findAll();
//	}
//
//	@GetMapping("/embasic/{id}")
//	public ResponseEntity<EMBasic> getKeywordById(
//			@PathVariable(value = "id") String embasicID) throws ResourceNotFoundException {
//		EMBasic embasic = systemKeywordRepository.findById(embasicID)
//        .orElseThrow(() -> new ResourceNotFoundException("Keyword not found :: " + embasicID));
//		return ResponseEntity.ok().body(embasic);
//	}
//
//	@PostMapping("/embasic")
//	public EMBasic createKeyword(@Valid @RequestBody EMBasic embasic) {
//		return systemKeywordRepository.save(embasic);
//	}
//
//	@PutMapping("/embasic/{id}")
//	public ResponseEntity<EMBasic> updateKeyword(
//			@PathVariable(value = "id") String embasicID,
//			@Valid @RequestBody EMBasic embasicDetails) throws ResourceNotFoundException {
//		EMBasic embasic = systemKeywordRepository.findById(embasicID)
//		        .orElseThrow(() -> new ResourceNotFoundException("embasicID not found :: " + embasicID));
//		
//		embasic.setID(embasicDetails.getID());
//		embasic.setAberration(embasicDetails.getAberration());
//		embasic.setAccelerationVoltage(embasicDetails.getAccelerationVoltage());
//		embasic.setCameraName(embasicDetails.getCameraName());
//		embasic.setDescription(embasicDetails.getDescription());
//		embasic.setEMName(embasicDetails.getEMName());
//		embasic.setInsertTime(embasicDetails.getInsertTime());
//		embasic.setParameterName(embasicDetails.getParameterName());
//		embasic.setSphericalAberrationCoefficient(embasicDetails.getSphericalAberrationCoefficient());
//		embasic.setUsergroup(embasicDetails.getUsergroup());
//		embasic.setUsername(embasicDetails.getUsername());
//		
//		
//		final EMBasic updatedKeyword = systemKeywordRepository.save(embasic);
//		return ResponseEntity.ok(updatedKeyword);
//	}
//
//	@DeleteMapping("/embasic/{id}")
//	public Map<String, Boolean> deleteKeyword(
//			@PathVariable(value = "id") String embasicID) throws ResourceNotFoundException {
//		EMBasic embasic = systemKeywordRepository.findById(embasicID)
//		        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + embasicID));
//
//		systemKeywordRepository.delete(embasic);
//		Map<String, Boolean> response = new HashMap<>();
//		response.put("deleted", Boolean.TRUE);
//		return response;
//	}
//}
