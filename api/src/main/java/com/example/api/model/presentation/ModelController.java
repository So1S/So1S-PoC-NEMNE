package com.example.api.model.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.model.application.ModelService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/model")
@AllArgsConstructor
public class ModelController {
	private final Logger logger = LoggerFactory.getLogger(ModelService.class);
	private ModelService modelService;

	@PostMapping("/upload")
	public ResponseEntity<String> upload(){
		logger.debug("CONTROLLER!!");
		String result = modelService.upload();

		return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<String> hello(){
		return ResponseEntity.ok("Hello World!");
	}

}
