FROM errordeveloper/oracle-jre

ENV HOME /root/
WORKDIR /app/

# newrelic
RUN curl -L --silent --retry 3 https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip -o newrelic-java.zip
RUN unzip newrelic-java.zip && rm newrelic/*.xml newrelic/*.xsd newrelic/*.yml newrelic/newrelic-api*.jar
ADD newrelic/newrelic.yml /app/newrelic/newrelic.yml

# dropwizard
ADD work-deal.yml /app/
ADD build/work-deal.jar /app/

# run
EXPOSE 8080
ENTRYPOINT [ "java" ]
CMD ["-javaagent:newrelic/newrelic.jar", "-jar", "work-deal.jar", "server", "work-deal.yml"]
