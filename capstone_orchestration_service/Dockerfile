FROM java:8-jre
WORKDIR /usr/lib
ENV CONFIG_HOST=http://configserver:8090
ADD ./target/orchestrationservice-0.0.1-SNAPSHOT.jar /usr/lib/orchestrationservice-0.0.1-SNAPSHOT.jar
ADD ./script/wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh","music-eureka:9003","musicuserservice:8083","musicauthservice:8084","--strict","--","java","-jar","orchestrationservice-0.0.1-SNAPSHOT.jar"]
