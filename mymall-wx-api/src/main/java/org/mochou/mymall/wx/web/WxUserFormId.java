package org.mochou.mymall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.db.domain.MymallUser;
import org.mochou.mymall.db.domain.MymallUserFormid;
import org.mochou.mymall.db.service.MymallUserFormIdService;
import org.mochou.mymall.db.service.MymallUserService;
import org.mochou.mymall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/wx/formid")
@Validated
public class WxUserFormId {
    private final Log logger = LogFactory.getLog(WxUserFormId.class);

    @Autowired
    private MymallUserService userService;

    @Autowired
    private MymallUserFormIdService formIdService;

    @GetMapping("create")
    public Object create(@LoginUser Integer userId, @NotNull String formId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        MymallUser user = userService.findById(userId);
        MymallUserFormid userFormid = new MymallUserFormid();
        userFormid.setOpenid(user.getWeixinOpenid());
        userFormid.setFormid(formId);
        userFormid.setIsprepay(false);
        userFormid.setUseamount(1);
        userFormid.setExpireTime(LocalDateTime.now().plusDays(7));
        formIdService.addUserFormid(userFormid);

        return ResponseUtil.ok();
    }
}
