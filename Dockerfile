
#FROM centos:7.6.1810

FROM public.ecr.aws/amazoncorretto/amazoncorretto:latest

USER root

RUN mkdir -p /app
COPY . /app/
RUN chmod 755 /app/mvnw
RUN chmod 755 /app/npmw

EXPOSE 8080



COPY run-java.sh /app/

RUN chmod 755 /app/run-java.sh

CMD [ "/app/run-java.sh" ]