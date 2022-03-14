# Steps to reproduce issue-23807

## Run Quarkus application in standard JVM mode
1. Build application command:
`mvn clean package`

2. Run java jar with profile NONE-CI activated
`java -Dquarkus.profile=NONE-CI -jar target\nativeconfig-1.0.0-SNAPSHOT-runner.jar`
 
2. Call http://localhost:8480/hello/sample 
3. Verify application console output - it should be following (this is expected and valid behaviour):
```    
2022-03-14 16:50:37,388 INFO  [io.quarkus] (main) nativeconfig 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.5.0.Final) started in 4.791s. Listening on: http://0.0.0.0:8480
2022-03-14 16:50:37,403 INFO  [io.quarkus] (main) Profile NONE-CI activated.
2022-03-14 16:50:37,407 INFO  [io.quarkus] (main) Installed features: [cdi, config-yaml, resteasy, servlet, smallrye-context-propagation, vertx]
Read value [                  Optional[CI]] for key [   stage.environment] using [microprofile.Config directly from javax.Filter]
Read value [               Optional[GREEN]] for key [        stage.colour] using [microprofile.Config directly from javax.Filter]
Read value [                  Optional[CI]] for key [   stage.environment] using [microprofile.Config indirectly via Helper class]
Read value [               Optional[GREEN]] for key [        stage.colour] using [microprofile.Config indirectly via Helper class]
```
The `CI` value comes from application.yaml configuration:

```
%NONE-CI:
  stage:
    environment: CI
```

**Please note, that using standard JVM approach everything works fine.**

## Run Quarkus application in native mode
1. Build application command:
`mvn clean package -Dquarkus.package.type=native -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:21.3-java11`

2. Build the image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/nativeconfig .`

3. Run the container with profile NONE-CI activated:
`docker run -i --rm -p 8480:8480 -e PROFILE=NONE-CI quarkus/nativeconfig`

4. Call http://localhost:8480/hello/sample 

5. Verify application console output - it should be following (this is reproducible and expected but **invalid behavior**):
  
```    
2022-03-14 15:53:46,655 INFO  [io.quarkus] (main) nativeconfig 1.0.0-SNAPSHOT native (powered by Quarkus 2.5.0.Final) started in 0.013s. Listening on: http://0.0.0.0:8480
2022-03-14 15:53:46,656 INFO  [io.quarkus] (main) Profile NONE-CI activated.
2022-03-14 15:53:46,656 INFO  [io.quarkus] (main) Installed features: [cdi, config-yaml, resteasy, servlet, smallrye-context-propagation, vertx]
Read value [       Optional[DEFAULT_VALUE]] for key [   stage.environment] using [microprofile.Config directly from javax.Filter]
Read value [                Optional.empty] for key [        stage.colour] using [microprofile.Config directly from javax.Filter]
Read value [                Optional.empty] for key [   stage.environment] using [microprofile.Config indirectly via Helper class]
Read value [               Optional[GREEN]] for key [        stage.colour] using [microprofile.Config indirectly via Helper class]
```

**Please note, that**
- for key (stage.environment) having default value in config both results are invalid - directly from Filter and indirectly via Helper class
- for key (stage.colour) which doesn't have default value result read directly from Config in Filter is invalid, but read via  Helper class is correct 
