#!/bin/bash

# 本脚本的作用是停止当前Spring Boot应用，然后再次部署
sudo service mymall stop
sudo ln -f -s /home/ubuntu/deploy/mymall/mymall.jar /etc/init.d/mymall
sudo service mymall start