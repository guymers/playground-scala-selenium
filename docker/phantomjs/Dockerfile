FROM debian:wheezy

RUN apt-get update
RUN apt-get install -y \
  build-essential git g++ flex bison gperf ruby perl \
  libsqlite3-dev libfontconfig1-dev libicu-dev libfreetype6 \
  libssl-dev libpng-dev libjpeg-dev
RUN mkdir /phantomjs
RUN git clone https://github.com/ariya/phantomjs.git /phantomjs/git
RUN cd /phantomjs/git && git checkout remotes/origin/1.x
RUN cd /phantomjs/git && ./build.sh --confirm
RUN mv /phantomjs/git/bin/phantomjs /phantomjs/phantomjs
RUN rm -r /phantomjs/git

WORKDIR /phantomjs
ENTRYPOINT ["/phantomjs/phantomjs"]
CMD ["--webdriver=4444"]

EXPOSE 4444
