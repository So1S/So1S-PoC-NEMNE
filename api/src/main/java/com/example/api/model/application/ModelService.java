package com.example.api.model.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

	public String upload(){
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
			.withImage("dlatqdlatq/tf_service:latest")
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

	private void buildModel() {
		/*
		mount volume(or ConfigMap)을 통해서 api-server에 저장된 모델을 연결하고
			model.pt 가지고 있다. -> 일단은 api 서버에 저장

		Job(도커 이미지를 만들어야 됨)에서는 컨테이너를 생성하고 연결을 해준다.
			-> docker + bentoML 환경이 있는 Job 컨테이너에서 bentoml bundle 생성 후 도커라이즈하고 레지스트리에 푸쉬까지 해준다.


		1차 -> bentoML(bentoml bundle까지 생성 + serve)
		2차 -> Docker가 있는 컨테이너를 통해 이미지 푸쉬

		Job만 만들되, Model에 대한 볼륨 설정 및 docker login을 위한 Secret 설정까지 해줄 것

		*/


	}
}
