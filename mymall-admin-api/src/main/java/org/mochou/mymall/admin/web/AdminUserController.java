package org.mochou.mymall.admin.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.mochou.mymall.admin.annotation.RequiresPermissionsDesc;
import org.mochou.mymall.core.util.RegexUtil;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.core.util.bcrypt.BCryptPasswordEncoder;
import org.mochou.mymall.core.validator.Order;
import org.mochou.mymall.core.validator.Sort;
import org.mochou.mymall.db.domain.MymallUser;
import org.mochou.mymall.db.service.MymallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mochou.mymall.admin.util.AdminResponseCode.*;

@RestController
@RequestMapping("/admin/user")
@Validated
public class AdminUserController {
    private final Log logger = LogFactory.getLog(AdminUserController.class);

    @Autowired
    private MymallUserService userService;

    @RequiresPermissions("admin:user:list")
    @RequiresPermissionsDesc(menu={"用户管理" , "会员管理"}, button="查询")
    @GetMapping("/list")
    public Object list(String username, String mobile,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<MymallUser> userList = userService.querySelective(username, mobile, page, limit, sort, order);
        long total = PageInfo.of(userList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", userList);

        return ResponseUtil.ok(data);
    }
}
