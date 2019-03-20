#!/bin/bash

# 请注意
# 本脚本的作用是把本项目编译的结果保存到deploy文件夹中
# 1. 把项目数据库文件拷贝到deploy/db
# 2. 编译mymall-admin
# 3. 编译mymall-all模块，然后拷贝到deploy/mymall

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
cd $DIR/../..
MYMALL_HOME=$PWD
echo "MYMALL_HOME $MYMALL_HOME"

# 复制数据库
cat $MYMALL_HOME/mymall-db/sql/mymall_schema.sql > $MYMALL_HOME/deploy/db/mymall.sql
cat $MYMALL_HOME/mymall-db/sql/mymall_table.sql >> $MYMALL_HOME/deploy/db/mymall.sql
cat $MYMALL_HOME/mymall-db/sql/mymall_data.sql >> $MYMALL_HOME/deploy/db/mymall.sql

cd $MYMALL_HOME/mymall-admin
# 安装阿里node镜像工具
npm install -g cnpm --registry=https://registry.npm.taobao.org
# 安装node项目依赖环境
cnpm install
cnpm run build:dep

cd $MYMALL_HOME
mvn clean package
cp -f $MYMALL_HOME/mymall-all/target/mymall-all-*-exec.jar $MYMALL_HOME/deploy/mymall/mymall.jar