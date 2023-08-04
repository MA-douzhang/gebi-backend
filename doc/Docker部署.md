# 服务器环境搭建
## nacos镜像

1. 使用宝塔Docker管理器直接拉起nacos环境并运行

![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1690988243495-9747837c-ed5d-4c46-9783-0b195a047cbc.png#averageHue=%23faf9f9&clientId=u4534810f-0ee5-4&from=paste&height=106&id=uff567e64&originHeight=133&originWidth=1225&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=21249&status=done&style=none&taskId=u68467bd9-5ee3-4abd-811e-0c0a16cc0de&title=&width=980)
**注意：在同一台服务器中，nacos只对内网才能注册，图中172.17.0.2是内网地址，在多台服务器中需要跨ip注册服务需要百度自行学习，本次部署使用同一台服务器部署。**
启动命令
```shell
//加内存限制启动
docker  run \
--name nacos -d \
-p 8848:8848 \
--privileged=true \
--restart=always \
-e JVM_XMS=32m \
-e JVM_XMX=128m \
-e JVM_XMN=32m \
-e JVM_MS=32m \
-e JVM_MMS=128m \
-e MODE=standalone \
-e PREFER_HOST_MODE=hostname \
nacos/nacos-server:latest

```
```shell
//不加内存限制
docker run -d --name nacos -p 8848:8848 -e PREFER_HOST_MODE=hostname -e MODE=standalone nacos/nacos-server
```
## RabbitMQ镜像

1. 拉起RabbitMQ镜像并运行
```dockerfile
//拉取镜像
docker pull rabbitmq
//部署容器
docker run -d --hostname my-rabbit --name rabbit -p 15672:15672 -p 5672:5672 rabbitmq

```

2. 开启可视化界面[https://blog.csdn.net/ruoshuiyx/article/details/128305746](https://blog.csdn.net/ruoshuiyx/article/details/128305746)

注意：项目部署最好也使用内部ip访问rabbitmq
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1690990479320-8ba75d5e-f8a1-4b15-b4a0-2256dd6071d5.png#averageHue=%23fafafa&clientId=u4534810f-0ee5-4&from=paste&height=47&id=ubc07ef9a&originHeight=59&originWidth=1203&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=7557&status=done&style=none&taskId=u30a08e93-aeb6-4a18-8aab-31258bfd7bb&title=&width=962.4)
## Redis

1. 可以使用宝塔一键集成Redis，也可以Docker搭建环境Redis，

步骤：
docker安装redis：[https://blog.csdn.net/weixin_45821811/article/details/116211724](https://blog.csdn.net/weixin_45821811/article/details/116211724)
redis外网连接：[https://blog.csdn.net/zhoumengshun/article/details/112862448](https://blog.csdn.net/zhoumengshun/article/details/112862448)
注意：外网连接需要修改redis连接密码，不然会被攻击
# 本地配置
## 利用idea插件连接服务器的docker
**注意连接服务器docter必须使用钥密验证，不能直连，服务器会被侵入。**
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1690988014546-95a95c2b-10f0-4c09-afc6-9b30810913cd.png#averageHue=%233d4043&clientId=u4534810f-0ee5-4&from=paste&height=682&id=u47c89202&originHeight=852&originWidth=1316&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=58216&status=done&style=none&taskId=ub9be01a7-4c4b-409d-be5b-7c0da916980&title=&width=1052.8)
idea连接ca证书步骤：[https://blog.csdn.net/qq_41946543/article/details/104159687](https://blog.csdn.net/qq_41946543/article/details/104159687)
证书自动脚本，全新可用
```shell
# !/bin/bash

# 一键生成TLS和CA证书

# Create : 2023-08-03
# Update : 2023-08-03
# @Autor : madou

# 服务器ip名
SERVER="服务器ip"
# 密码
PASSWORD="密码"
# 国家
COUNTRY="CN"
# 省份
STATE="sc"
# 城市
CITY="cd"
# 机构名称
ORGANIZATION="madou"
# 机构单位
ORGANIZATIONAL_UNIT="madou"
# 邮箱
EMAIL="841838856@qq.com"

echo "生成文件完成"
#切换到生产密钥的目录
cd /etc/docker 
# 生成CA密钥
#生成ca私钥(使用aes256加密)
openssl genrsa -aes256 -passout pass:$PASSWORD  -out ca-key.pem 4096
#生成ca证书，填写配置信息
openssl req -new -x509 -passin "pass:$PASSWORD" -days 365 -key ca-key.pem -sha256 -out ca.pem -subj "/C=$COUNTRY/ST=$STATE/L=$CITY/O=$ORGANIZATION/OU=$ORGANIZATIONAL_UNIT/CN=$SERVER/emailAddress=$EMAIL"

#生成server证书私钥文件
openssl genrsa -out server-key.pem 4096
#生成server证书请求文件
openssl req -subj "/CN=$SERVER" -sha256 -new -key server-key.pem -out server.csr

sh -c 'echo "subjectAltName = IP:139.155.130.108,IP:0.0.0.0" > extfile.cnf'
sh -c 'echo "extendedKeyUsage=serverAuth " >> extfile.cnf'

#使用CA证书及CA密钥以及上面的server证书请求文件进行签发，生成server自签证书
openssl x509 -req -days 365 -sha256 -in server.csr -CA ca.pem -CAkey ca-key.pem -passin "pass:$PASSWORD" -CAcreateserial  -out server-cert.pem  -extfile extfile.cnf

#生成client证书RSA私钥文件
openssl genrsa -out key.pem 4096
#生成client证书请求文件
openssl req -subj '/CN=client' -new -key key.pem -out client.csr

sh -c 'echo "extendedKeyUsage=clientAuth" > extfile-client.cnf'
#生成client自签证书（根据上面的client私钥文件、client证书请求文件生成）
openssl x509 -req -days 365 -sha256 -in client.csr -CA ca.pem -CAkey ca-key.pem  -passin "pass:$PASSWORD" -CAcreateserial -out cert.pem  -extfile extfile-client.cnf

#更改密钥权限
chmod 0400 ca-key.pem key.pem server-key.pem
#更改密钥权限
chmod 0444 ca.pem server-cert.pem cert.pem
#删除无用文件
rm client.csr server.csr
echo "生成文件完成"
```
连接完成后配置docker插件
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135229518-faa4182d-72fd-4bda-a931-e95ee1bd4772.png#averageHue=%233e464f&clientId=u6dd29d8c-3f08-4&from=paste&height=213&id=uead4fae8&originHeight=266&originWidth=401&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=19936&status=done&style=none&taskId=u36e91e36-2683-4e98-8318-2fad82c45c4&title=&width=320.8)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135262332-1c2c000c-508b-45b8-aad2-0c4ad36147e1.png#averageHue=%233d4144&clientId=u6dd29d8c-3f08-4&from=paste&height=643&id=u5e08b8d3&originHeight=804&originWidth=1290&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=55682&status=done&style=none&taskId=u44a8ecc0-2f10-408f-a39a-7a6cc13aa25&title=&width=1032)
## 配置文件部署环境
建三个配置文件，分别为dev本地环境，prod部署环境，和主环境
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1690988113026-29066cec-fe1f-4a5c-9966-af6c4eda57fc.png#averageHue=%233d4144&clientId=u4534810f-0ee5-4&from=paste&height=140&id=uf180c098&originHeight=175&originWidth=309&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=7356&status=done&style=none&taskId=u1c31b2f4-31c3-41f0-92eb-c47d78a26c0&title=&width=247.2)
## 开启配置文件环境

1. 当本地运行会选择dev，打包部署会选择prod
2. 将prod中nacos，redis，rabbitmq，mysql的信息配置正确
```yaml
//application.yml
spring:
  profiles:
    active: dev

```
```yaml
//application-dev.yml
spring:
  config:
    activate:
      on-profile: 
      - dev

```
```yaml
//application-prod.yml
spring:
  config:
    activate:
      on-profile: 
      - prod

```
### 支付功能配置
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135675817-edbf4914-8cd3-40e4-8441-3475d3b3dfc3.png#averageHue=%236b6746&clientId=u6dd29d8c-3f08-4&from=paste&height=393&id=u6c3f1b5d&originHeight=491&originWidth=1587&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=83860&status=done&style=none&taskId=u31539811-a60f-47b8-897b-a5d4b5877ff&title=&width=1269.6)

1. 需要自己去支付宝注册沙盒
2. 将ip地址改为项目网关ip
3. 本地使用支付需要内网穿透
## Dockerfile文件配置
Dockerfile-chart，其它Dockerfile类似
```dockerfile
FROM openjdk:8
VOLUME /tmp
COPY target/*.jar gebi-text.jar
ENV PORT=9096
ENTRYPOINT ["java","-jar","-Xms32m","-Xmx64m","gebi-text.jar","--spring.profiles.active=prod"]
EXPOSE $PORT

```

# 准备启动
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135561545-884f067c-a487-4119-a614-6b3a565004af.png#averageHue=%233e464f&clientId=u6dd29d8c-3f08-4&from=paste&height=198&id=ufb0aa383&originHeight=248&originWidth=448&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=19326&status=done&style=none&taskId=u87cca3e6-d4d5-4938-a6bd-07b7d8fae71&title=&width=358.4)
启动之后会自动打包部署置服务器上注意打开项目端口
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135541280-5e2416f9-d460-4529-a2fb-9d64737838b6.png#averageHue=%233c4043&clientId=u6dd29d8c-3f08-4&from=paste&height=425&id=u60cb8bc1&originHeight=531&originWidth=1857&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=41088&status=done&style=none&taskId=u2d6a0a3f-bfb7-4faf-a0f2-8dd5c3c4e6e&title=&width=1485.6)
服务器上可以看到
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135847292-c09effa7-f06f-420b-949a-04095296584b.png#averageHue=%23f8f8f8&clientId=u1cd29890-8342-4&from=paste&height=286&id=u512cb92e&originHeight=358&originWidth=1104&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=50887&status=done&style=none&taskId=u3c57c361-7939-40fa-a229-7c477bfecc5&title=&width=883.2)
部署成功
![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691135875563-e6f00ea7-c6dd-4d5f-ba6b-b421e1717ceb.png#averageHue=%23f7f7f7&clientId=u1cd29890-8342-4&from=paste&height=802&id=u44e08426&originHeight=1002&originWidth=1875&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=64971&status=done&style=none&taskId=u1c8a948c-6af8-4d0a-b729-919a19de504&title=&width=1500)

# 前端部署

1. 使用宝塔面板部署
2. 项目app.ts修改一下ip地址

![image.png](https://cdn.nlark.com/yuque/0/2023/png/34904406/1691136425272-0a1dd3b6-9cc8-4c88-a28f-b197e5004e5d.png#averageHue=%23756e44&clientId=u7e480584-0eb3-4&from=paste&height=373&id=ubc60a550&originHeight=466&originWidth=1730&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=96566&status=done&style=none&taskId=u15c74bac-7cb3-4f70-ae04-b70a11e26cb&title=&width=1384)

3. 打包直接部署即可
