package com.example.api.model.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.model.application.ModelService;
import com.example.api.model.presentation.dto.DeployRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/model")
@AllArgsConstructor
public class ModelController {
	private final Logger logger = LoggerFactory.getLogger(ModelService.class);
	private ModelService modelService;

	@PostMapping("/deploy")
	public ResponseEntity<String> deploy(@RequestBody DeployRequest deployRequest){
		logger.info("Receive deploy Request");

		String imageName = (deployRequest.getAccount().equals(""))
			? "dlatqdlatq/tf_service:latest"
			: deployRequest.getAccount() + "/" + deployRequest.getTitle() + ":" + deployRequest.getVersion();
		String result = modelService.deploy(imageName);

		return ResponseEntity.ok(result);
	}

	@PostMapping("/upload")
	public ResponseEntity<String> upload() {
		logger.info("Receive upload Request");

		String result = modelService.upload();

		return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<String> hello(){
		logger.info("Receive hello Request");
		return ResponseEntity.ok("Hello World!");
	}

}
