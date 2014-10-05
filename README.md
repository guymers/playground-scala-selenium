```
sudo docker build -t local:gitbucket docker/gitbucket
sudo docker run -d -p 8789:8789 --name gitbucket local:gitbucket

-- build phantomjs 1.10.x so deleteAllCookies() doesnt remove other sessions cookies when running in parallel
sudo docker build -t local:phantomjs docker/phantomjs
sudo docker run -d -p 4444:4444 --name phantomjs --link gitbucket:gitbucket local:phantomjs

sudo docker build -t local:phantomjs docker/phantomjs2
sudo docker run -d -p 4444:4444 --name phantomjs2 --link gitbucket:gitbucket local:phantomjs2

chromedriver --port=4444
```
