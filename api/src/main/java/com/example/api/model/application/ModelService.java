package com.example.api.model.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModelService {
	private final Logger logger = LoggerFactory.getLogger(ModelService.class);
	private final KubernetesClient client = new DefaultKubernetesClient();

	public String upload(){

		String result = "";
		try {
			io.fabric8.kubernetes.api.model.Service aService = client.services().load(new FileInputStream("/Users/imseongbin/Desktop/workspace/so1s/so1s-poc-nemne/inferences-server/kubernetes-manifests/inference.service.yaml")).get();
			logger.info("service spec: {}", aService.getSpec());
			io.fabric8.kubernetes.api.model.Service createdSvc = client.services().inNamespace("default").createOrReplace(aService);
			Deployment aDeployment = client.apps()
				.deployments()
				.load(new FileInputStream(
					"/Users/imseongbin/Desktop/workspace/so1s/so1s-poc-nemne/inferences-server/kubernetes-manifests/inference.deployment.yaml"))
				.get();
			result = aDeployment.toString();
			logger.info("deployment spec: {}", aDeployment.getSpec());
			Deployment createdDeployment = client.apps().deployments().inNamespace("default").createOrReplace(aDeployment);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
