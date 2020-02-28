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
//import com.cryomate.model.UserExample;
//import com.cryomate.repository.UserExampleRepository;
//
//
//
//
//@RestController
//@RequestMapping("/api/v1")
//public class UserExampleController {
//	
//	@Autowired
//	private UserExampleRepository userRepository;
//
//	
//	@GetMapping("/users")
//	public List<UserExample> getAllUsers() {
//		return userRepository.findAll();
//	}
//
//	@GetMapping("/users/{id}")
//	public ResponseEntity<UserExample> getUserById(
//			@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
//		UserExample user = userRepository.findById(userId)
//        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + userId));
//		return ResponseEntity.ok().body(user);
//	}
//
//	@PostMapping("/users")
//	public UserExample createUser(@Valid @RequestBody UserExample user) {
//		return userRepository.save(user);
//	}
//
//	@PutMapping("/users/{id}")
//	public ResponseEntity<UserExample> updateUser(
//			@PathVariable(value = "id") Long userId,
//			@Valid @RequestBody UserExample userDetails) throws ResourceNotFoundException {
//		UserExample user = userRepository.findById(userId)
//		        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + userId));
//		
//		user.setEmailId(userDetails.getEmailId());
//		user.setLastName(userDetails.getLastName());
//		user.setFirstName(userDetails.getFirstName());
//		user.setUpdatedAt(new Date());
//		final UserExample updatedUser = userRepository.save(user);
//		return ResponseEntity.ok(updatedUser);
//	}
//
//	@DeleteMapping("/users/{id}")
//	public Map<String, Boolean> deleteUser(
//			@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
//		UserExample user = userRepository.findById(userId)
//		        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + userId));
//
//		userRepository.delete(user);
//		Map<String, Boolean> response = new HashMap<>();
//		response.put("deleted", Boolean.TRUE);
//		return response;
//	}
//}
