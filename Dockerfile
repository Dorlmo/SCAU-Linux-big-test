# 设置基础镜像为 CentOS
FROM centos:latest

COPY go_app/ go/
COPY java_app/ java/
COPY node_app/ node/

# 安装nodejs，java17
RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-* &&\
    sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-* &&\
    yum install -y wget &&\
    wget -O /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-vault-8.5.2111.repo &&\
    yum clean all &&\
    yum install -y nodejs &&\
    yum localinstall -y java/jdk-17_linux-x64_bin.rpm

# 设置工作目录
WORKDIR /

# 复制启动脚本到镜像中
COPY start.sh start.sh

# 添加执行权限
RUN chmod +x /start.sh

# 运行启动脚本
CMD ["/start.sh"]

