package com.example.api.model.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.model.application.ModelService;

@RestController("/model")
public class ModelController {
	private ModelService modelService;

	@PostMapping("/upload")
	public ResponseEntity<Boolean> upload(){
		/*

		일단 모델 업로드를 요청한다. -> 모델을 매개변수 값으로 준다.

		 */

		return ResponseEntity.ok(true);
	}

}
