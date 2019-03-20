
## 快速启动

1. 配置最小开发环境：
    * [MySQL](https://dev.mysql.com/downloads/mysql/)
    * [JDK1.8或以上](http://www.oracle.com/technetwork/java/javase/overview/index.html)
    * [Maven](https://maven.apache.org/download.cgi)
    * [Nodejs](https://nodejs.org/en/download/)
    * [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
    
2. 数据库依次导入mymall-db/sql下的数据库文件
    * mymall_schema.sql
    * mymall_table.sql
    * mymall_data.sql

3. 启动小商场和管理后台的后端服务

    打开命令行，输入以下命令
    ```bash
    cd mymall
    mvn install
    mvn package
    cd ./mymall-all
    mvn spring-boot:run
    ```
    
4. 启动管理后台前端

    打开命令行，输入以下命令
    ```bash
    npm install -g cnpm --registry=https://registry.npm.taobao.org
    cd mymall/mymall-admin
    cnpm install
    cnpm run dev
    ```
    此时，浏览器打开，输入网址`http://localhost:9527`, 此时进入管理后台登录页面。
    
5. 启动小商城前端
   
   打开微信开发者工具，导入mymall-wx模块,点击`编译`即可，此时可以预览小商场效果。

   这里存在两套小商场前端mymall-wx和renard-wx，开发者可以分别导入和测试。
   
