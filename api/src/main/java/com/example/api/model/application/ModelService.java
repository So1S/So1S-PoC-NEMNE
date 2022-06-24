package com.example.api.model.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.api.model.presentation.dto.DeployRequest;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientImpl;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModelService {
	private final Logger logger = LoggerFactory.getLogger(ModelService.class);
	private final KubernetesClient client = new DefaultKubernetesClient();

	public String upload() {
		// docker-java or AmazonECRClient를 이용하여 image를 push하고 pull할 예정 - 수도코드로 대체
		// DockerClient dockerClient = DockerClientImpl.getInstance();
		// dockerClient.pushImageCmd()

		return "";
	}

	public String deploy(String imageName){
		io.fabric8.kubernetes.api.model.Service service = new ServiceBuilder()
			.withNewMetadata()
			.withName("inference-service")
			.endMetadata()
			.withNewSpec()
			.withSelector(Collections.singletonMap("app", "myinference"))
			.addNewPort()
			.withPort(5000)
			.withTargetPort(new IntOrString(5000))
			.endPort()
			.endSpec().build();

		//create service
		client.services()
			.inNamespace("default")
			.createOrReplace(service);
		logger.info("service spec: {}", service.getSpec());

		Map<String, Quantity> reqMap = new HashMap<>();
		reqMap.put("cpu", new Quantity("1"));
		reqMap.put("memory", new Quantity("1024Mi"));

		ResourceRequirements resourceRequirements = new ResourceRequirementsBuilder()
			.withLimits(reqMap).build();

		Deployment deployment = new DeploymentBuilder()
			.withNewMetadata()
			.withName("inference-deployment")
			.endMetadata()
			.withNewSpec()
			.withReplicas(2)
			.withNewTemplate()
			.withNewMetadata()
			.addToLabels("app", "myinference")
			.endMetadata()
			.withNewSpec()
			.addNewContainer()
			.withName("bentoml-inference")
			.withImage(imageName)
			.withResources(resourceRequirements)
			.addNewPort()
			.withContainerPort(5000)
			.endPort()
			.endContainer()
			.endSpec()
			.endTemplate()
			.withNewSelector()
			.addToMatchLabels("app", "myinference")
			.endSelector()
			.endSpec()
			.build();

		//create deployment
		client.apps().deployments().inNamespace("default").createOrReplace(deployment);
		logger.info("deployment spec: {}", deployment.getSpec());

		return deployment.toString();
	}
}
