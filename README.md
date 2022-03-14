To reproduce issue-23807:

Run Quarkus application in standard JVM mode
1. Activate profile NONE-CI 
2. Call http://localhost:8480/hello/sample 
3. Verify application console output - it should be following:
    
    `Optional[CI] from filter with no helper class` <br />
    `Optional[CI] from filter with helper class` <br />

The `CI` value comes from application.yaml configuration:

`'%NONE-CI': <br />
  stage: <br />
    environment: CI` 


Run Quarkus applicationin native mode
1. Run command:
`mvn clean package -Pnative -Dquarkus.package.type=native -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:21.3-java11`

2. Build the image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/nativeconfig .`

3. Run the container:
`docker run -i --rm -p 8480:8480 quarkus/nativeconfig`

4. Call http://localhost:8480/hello/sample 

5. Verify application console output - it should be following:
    
    **`Optional.empty from filter with no helper class`** <br />
    `Optional[CI] from filter with helper class`

**Here - note, that when Filter with direct Microprofile Config injection is used, the property configured under NONE-CI profile is not recognized and the Optional is empty.**

