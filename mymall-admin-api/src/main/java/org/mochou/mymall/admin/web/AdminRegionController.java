package org.mochou.mymall.admin.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.core.validator.Order;
import org.mochou.mymall.core.validator.Sort;
import org.mochou.mymall.db.domain.MymallRegion;
import org.mochou.mymall.db.service.MymallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/region")
@Validated
public class AdminRegionController {
    private final Log logger = LogFactory.getLog(AdminRegionController.class);

    @Autowired
    private MymallRegionService regionService;

    @GetMapping("/clist")
    public Object clist(@NotNull Integer id) {
        List<MymallRegion> regionList = regionService.queryByPid(id);
        return ResponseUtil.ok(regionList);
    }

    @GetMapping("/list")
    public Object list(String name, Integer code,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort(accepts = {"id"}) @RequestParam(defaultValue = "id") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<MymallRegion> regionList = regionService.querySelective(name, code, page, limit, sort, order);
        long total = PageInfo.of(regionList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", regionList);

        return ResponseUtil.ok(data);
    }
}
