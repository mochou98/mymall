package org.mochou.mymall.admin.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.mochou.mymall.admin.annotation.RequiresPermissionsDesc;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.core.validator.Order;
import org.mochou.mymall.core.validator.Sort;
import org.mochou.mymall.db.domain.MymallIssue;
import org.mochou.mymall.db.service.MymallIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/issue")
@Validated
public class AdminIssueController {
    private final Log logger = LogFactory.getLog(AdminIssueController.class);

    @Autowired
    private MymallIssueService issueService;

    @RequiresPermissions("admin:issue:list")
    @RequiresPermissionsDesc(menu={"商场管理" , "通用问题"}, button="查询")
    @GetMapping("/list")
    public Object list(String question,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<MymallIssue> issueList = issueService.querySelective(question, page, limit, sort, order);
        long total = PageInfo.of(issueList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", issueList);

        return ResponseUtil.ok(data);
    }

    private Object validate(MymallIssue issue) {
        String question = issue.getQuestion();
        if (StringUtils.isEmpty(question)) {
            return ResponseUtil.badArgument();
        }
        String answer = issue.getAnswer();
        if (StringUtils.isEmpty(answer)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:issue:create")
    @RequiresPermissionsDesc(menu={"商场管理" , "通用问题"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody MymallIssue issue) {
        Object error = validate(issue);
        if (error != null) {
            return error;
        }
        issueService.add(issue);
        return ResponseUtil.ok(issue);
    }

    @RequiresPermissions("admin:issue:read")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        MymallIssue issue = issueService.findById(id);
        return ResponseUtil.ok(issue);
    }

    @RequiresPermissions("admin:issue:update")
    @RequiresPermissionsDesc(menu={"商场管理" , "通用问题"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody MymallIssue issue) {
        Object error = validate(issue);
        if (error != null) {
            return error;
        }
        if (issueService.updateById(issue) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(issue);
    }

    @RequiresPermissions("admin:issue:delete")
    @RequiresPermissionsDesc(menu={"商场管理" , "通用问题"}, button="删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody MymallIssue issue) {
        Integer id = issue.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        issueService.deleteById(id);
        return ResponseUtil.ok();
    }

}
