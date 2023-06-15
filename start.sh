#!/bin/bash

# 启动 Go 服务端
./go/go_app &

# 启动 Java 服务端
java -jar ./java/java_app.jar &

# 启动 Node 服务
node ./node/index.js &

# 等待所有服务端进程结束
wait

