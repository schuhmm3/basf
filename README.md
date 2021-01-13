![CI Pipeline](https://github.com/rogomdi/basf/workflows/ci-pipeline.yml/badge.svg)
![CD Pipeline](https://github.com/rogomdi/basf/workflows/cd-pipeline.yml/badge.svg)

# CODING CHALLENGE

Tech stack
---
The project used the followig technologies:
- Java 11
- Spring Boot 2.4.1
- Spring Cloud
- Spring Cloud Stream
- Apache Kafka & Apache Zookeeper
- MongoDB
- Github Actions
- Github Packages

Continuous integration and continuous deployment (CI/CD)
---
The steps defined in CI workflow are the following:
- Build: This step compiles all the microservices. This step runs on every pipeline
- Test: The mission of this step is to run all tests in the project
  - CI on every push: This pipeline only runs unit tests on this step.
  - CI on PRs and main branch: It runs unit and integration tests.
- Publish artifacts: This step publishes the artifacts on github packages
- Publish docker: This step builds the docker images and publishes them to dockerhub repositories
- Publish chart: This step builds a helm chart and publishes it into gh-pages

Also it is integrated on github to pass this checks on every pull request

Security
---
Despite these coding test have not any security implemented, in a production environment these APIs should have a security layer using JWT tokens or the OAuth2 standard. 
Also, I would suggest having a gateway on top of these services, so you only expose these APIs through it.  

API documentation
---
Both APIs are documented with Swagger following the OpenAPI 3 Specification.

- NLP-Processor Swagger: <ur>http://localhost:8081/swagger</url>
- Patent-Manager Swagger: <ur>http://localhost:8082/swagger</url>

Heathchecks
---
Both APIs have the basic actuator endpoints to check if the services are running.

- NLP-Processor health endpoint: <ur>http://localhost:8081/actuator/health</url>
- Patent-Manager health endpoint: <ur>http://localhost:8082/actuator/health</url>

Architecture
---
All **asynchronous communications** between microservices are handled by Apache kafka and Zookeeper,
instead of using HTTP protocol, since it is more reliable and allows us to have fault-tolerance.

All **synchronous communications** between microservices are handled calling the REST API by using HTTP protocol.

Build
---
In order to build all components, from root folder you have to do one of the following steps:

- Build helm chart locally: <br>
  **Note: You need to have installed kompose and helm CLI** <br>
  Run the script `build-all.sh` on the root folder with the following command
  <code>PACKAGE=true BUILD_IMAGES=true ./build-all.sh</code>

- Compile all microservices and build docker images: <br>
  Go to nlp-processor and patent-manager folders and run the following command on both: <code>cd docker && ./build-image.sh</code>

- Compile all microservices: <br>
  Go to nlp-processor and patent-manager folders and run the following command on both: <code>./gradlew build</code>
  
- Compile all microservices skipping tests: <br>
  Go to nlp-processor and patent-manager folders and run the following command on both: <code>./gradlew build -PskipTests</code>

Run in docker
---
From root folder you have to run the following command to start docker containers:
- <code>docker-compose up -d</code>

When you run docker compose, these are the ports exposed to your computer:
- 2181 for Zookeeper
- 2717 for MongoDB
- 9000 for Kafdrop (Kafka UI)
- 9092 for Kafka
- 8081 for NLP-Processor
- 8082 for Patent-Manager

Run in kubernetes
---
**Note: You need to have installed kompose and helm CLI** <br>

From root folder you have to run the following command to start docker containers:
- <code>PACKAGE=true BUILD_IMAGES=true ./build-all.sh</code>

Install the chart by running: `helm install basf-coding-challenge basf-test-1.0.0-local.tgz`

Since we have not configured any Ingress controller, to access the APIs and Kafdrop you will need to expose the ports from services.
To do that we will need to run the following commands:
<br>
1. Exposing kafdrop:
   - <code>POD_NAME=$(kubectl get pods -n dev | grep kafdrop | awk '{print $1}')</code>
   - <code>kubectl port-forward 9000:9000 $POD_NAME</code>

2. Exposing patent-manager:
    - <code>POD_NAME=$(kubectl get pods -n dev | grep patent-manager | awk '{print $1}')</code>
    - <code>kubectl port-forward 8082:8082 $POD_NAME</code>

3. Exposing nlp-processor:
    - <code>POD_NAME=$(kubectl get pods -n dev | grep nlp-processor | awk '{print $1}')</code>
    - <code>kubectl port-forward 8082:8082 $POD_NAME</code>

FAQ
---
1. Why use Kafka instead of RabbitMQ?
   <br> Since Kafka is designed for deliver thousand of messages at a lower latency than RabbitMQ, this is the appropiate technology.

2. Why use a NoSQL database such as MongoDB?
   <br> As mentioned in the statement, we need to store lot of data and the schema for the patent can be different in the future. In this case, a NoSQL database is a good option.

3. Why use Github Actions?
   <br> I have selected it because it is easy to configure and it is fully integrated with Github

4. Why having the synchronous and asynchronous process?
   <br> In my opinion, to debug and to process a few patents. Of course, if you want to process a huge amount of data, the asynchronous way is the best option.

5. If I process a ZIP in an asynchronous way, how do I know if the NLP Process has finished?
   <br> Well, if you want to look for a patent, you can use the API to request it by its UUID or application. Note that other best approachs would be to notify through a websocket or publish on a kafka topic once the process is finished and read messages from it.

6. Why do you have two microservices in the same repository?
   <br> Since it is a coding challenge I think it is simpler to look into a single repository instead of cloning three different repositories. In production, each microservice should have its own repository and CI/CD pipelines.