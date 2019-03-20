package org.mochou.mymall.wx.service;

import org.mochou.mymall.db.domain.MymallUser;
import org.mochou.mymall.db.service.MymallUserService;
import org.mochou.mymall.wx.dao.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserInfoService {
    @Autowired
    private MymallUserService userService;


    public UserInfo getInfo(Integer userId) {
        MymallUser user = userService.findById(userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
