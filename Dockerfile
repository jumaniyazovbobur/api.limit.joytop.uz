FROM openjdk:17
EXPOSE 8081
ADD target/dachatop.jar dachatop.jar
ENTRYPOINT ["java","-jar","/dachatop"]
