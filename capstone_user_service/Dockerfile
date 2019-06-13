FROM java:8-jre
WORKDIR /usr/lib
ENV MONGO_DB_NAME=music
ENV MONGO_HOST=mongo
ENV MONGO_PORT=27017
ENV CONFIG_HOST=http://configserver:8090
ADD ./target/usertrackservice-0.0.1-SNAPSHOT.jar /usr/lib/usertrackservice-0.0.1-SNAPSHOT.jar
ADD ./script/wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh","music-eureka:9003","mongo:27017","--strict","--","java","-jar","usertrackservice-0.0.1-SNAPSHOT.jar"]
