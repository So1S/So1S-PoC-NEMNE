# K8s 기반 ML 모델 Real-Time Inference 서빙 Application 프로젝트 So1s 개인 PoC

## PoC 아키텍처

![architecture.jpg](images/architecture.jpg)

<br>
<br>
<br>

![API](images/api.png)

<br>
<br>
<br>

### 실행하기전 설정 사항

1. 로컬 클러스터 환경 내에 실행 하는 것이므로 ingress Host 설정은 별도로 해줘야합니다.
2. 추가로 ingress도 사전에 실행해줘야합니다. (test.ingress.yaml 참고)
3. 테스트 해볼 이미지가 없다면 **dlatqdlatq/tf_service:latest**로 실행해주면 됩니다.

<br>
<br>

## 실행 결과

![스크린샷 2022-06-24 오후 3.50.43.png](images/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-06-24_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_3.50.43.png)

![스크린샷 2022-06-24 오후 3.49.09.png](images/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-06-24_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_3.49.09.png)