# Steps to reproduce issue-23807

## Run Quarkus application in standard JVM mode
1. Build application command:
`mvn clean install`

2. Run java jar with profile NONE-CI activated
`java -Dquarkus.profile=NONE-CI -jar target\nativeconfig-1.0.0-SNAPSHOT-runner.jar`
 
2. Call http://localhost:8480/hello/sample 
3. Verify application console output - it should be following (this is expected and valid behaviour):
```    
    Optional[CI] from filter with no helper class
    Optional[CI] from filter with helper class
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
`mvn clean package -Pnative -Dquarkus.package.type=native -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:21.3-java11`

2. Build the image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/nativeconfig .`

3. Run the container with profile NONE-CI activated:
`docker run -i --rm -p 8480:8480 -e STAGE_ENVIRONMENT=NONE-CI quarkus/nativeconfig`

4. Call http://localhost:8480/hello/sample 

5. Verify application console output - it should be following (this is reproducible and expected but **invalid behavior**):
  
```    
    Optional[DEFAULT_VALUE] from filter with no helper class
    Optional[NONE-CI] from filter with helper class
```

**Please note, that when Filter with direct Microprofile Config injection is used, 
the property configured under NONE-CI profile is not recognized and the default value of stage.environment property is used.**

