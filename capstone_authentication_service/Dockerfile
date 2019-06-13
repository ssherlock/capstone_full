FROM java:8-jre
WORKDIR /usr/lib
ENV MYSQL_URI=jdbc:mysql://mysql:3306/capstonedb
ENV MYSQL_USER=root
ENV MYSQL_PASS=root
ENV AUTH_SHARED_KEY=secretkey
ENV CONFIG_HOST=http://configserver:8090
ADD ./target/authenticationservice-0.0.1-SNAPSHOT.jar /usr/lib/authenticationservice-0.0.1-SNAPSHOT.jar
ADD ./script/wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh","music-eureka:9003","mysql:3306","--strict","--","java","-jar","authenticationservice-0.0.1-SNAPSHOT.jar"]
