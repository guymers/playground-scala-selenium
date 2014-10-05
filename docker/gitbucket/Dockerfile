FROM dockerfile/java

RUN mkdir /gitbucket
ADD https://github.com/takezoe/gitbucket/releases/download/2.3/gitbucket.war /gitbucket/

WORKDIR /gitbucket
ENTRYPOINT ["java", "-jar", "gitbucket.war"]
CMD ["--port=8789", "--gitbucket.home=/gitbucket"]

EXPOSE 8789
