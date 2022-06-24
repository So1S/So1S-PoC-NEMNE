package com.example.api.model.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeployRequest {
	private String account;
	private String title;
	private String version;
}
