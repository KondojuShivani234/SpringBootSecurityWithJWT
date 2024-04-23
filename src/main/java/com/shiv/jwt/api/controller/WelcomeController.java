package com.shiv.jwt.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shiv.jwt.api.entity.AuthRequest;
import com.shiv.jwt.api.entity.Question;
import com.shiv.jwt.api.repository.QuestionRepo;
import com.shiv.jwt.api.repository.UserRepository;
import com.shiv.jwt.api.util.JwtUtil;

@RestController
public class WelcomeController {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private QuestionRepo questionRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String welcome() {
		return "Shiv.......... !!";
	}

	@GetMapping("/all")
	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
	}

	@PostMapping("/validate")
	public ResponseEntity<String> validateAnswer(@RequestBody Map<String, String> answer) {
		Long questionId = Long.parseLong(answer.get("questionId"));
		String chosenOption = answer.get("chosenOption");

		Optional<Question> optionalQuestion = questionRepository.findById(questionId);

		if (optionalQuestion.isPresent()) {
			Question question = optionalQuestion.get();
			if (question.getCorrectOption().equalsIgnoreCase(chosenOption)) {
				return ResponseEntity.ok("Correct!");
			} else {
				return ResponseEntity.ok("Wrong! The correct option is: " + question.getCorrectOption());
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (Exception ex) {
			throw new Exception("inavalid username/password");
		}
		return jwtUtil.generateToken(authRequest.getUserName());
	}
}
