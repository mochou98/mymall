package org.mochou.mymall.admin.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.mochou.mymall.admin.annotation.RequiresPermissionsDesc;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.core.validator.Order;
import org.mochou.mymall.core.validator.Sort;
import org.mochou.mymall.db.domain.MymallGoods;
import org.mochou.mymall.db.domain.MymallGroupon;
import org.mochou.mymall.db.domain.MymallGrouponRules;
import org.mochou.mymall.db.service.MymallGoodsService;
import org.mochou.mymall.db.service.MymallGrouponRulesService;
import org.mochou.mymall.db.service.MymallGrouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/groupon")
@Validated
public class AdminGrouponController {
    private final Log logger = LogFactory.getLog(AdminGrouponController.class);

    @Autowired
    private MymallGrouponRulesService rulesService;
    @Autowired
    private MymallGoodsService goodsService;
    @Autowired
    private MymallGrouponService grouponService;

    @RequiresPermissions("admin:groupon:read")
    @RequiresPermissionsDesc(menu={"推广管理" , "团购管理"}, button="详情")
    @GetMapping("/listRecord")
    public Object listRecord(String grouponId,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @Sort @RequestParam(defaultValue = "add_time") String sort,
                             @Order @RequestParam(defaultValue = "desc") String order) {
        List<MymallGroupon> grouponList = grouponService.querySelective(grouponId, page, limit, sort, order);
        long total = PageInfo.of(grouponList).getTotal();

        List<Map<String, Object>> records = new ArrayList<>();
        for (MymallGroupon groupon : grouponList) {
            try {
                Map<String, Object> RecordData = new HashMap<>();
                List<MymallGroupon> subGrouponList = grouponService.queryJoinRecord(groupon.getId());
                MymallGrouponRules rules = rulesService.queryById(groupon.getRulesId());
                MymallGoods goods = goodsService.findById(rules.getGoodsId());

                RecordData.put("groupon", groupon);
                RecordData.put("subGroupons", subGrouponList);
                RecordData.put("rules", rules);
                RecordData.put("goods", goods);

                records.add(RecordData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", records);

        return ResponseUtil.ok(data);
    }

    @RequiresPermissions("admin:groupon:list")
    @RequiresPermissionsDesc(menu={"推广管理" , "团购管理"}, button="查询")
    @GetMapping("/list")
    public Object list(String goodsId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<MymallGrouponRules> rulesList = rulesService.querySelective(goodsId, page, limit, sort, order);
        long total = PageInfo.of(rulesList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", rulesList);

        return ResponseUtil.ok(data);
    }

    private Object validate(MymallGrouponRules grouponRules) {
        Integer goodsId = grouponRules.getGoodsId();
        if (goodsId == null) {
            return ResponseUtil.badArgument();
        }
        BigDecimal discount = grouponRules.getDiscount();
        if (discount == null) {
            return ResponseUtil.badArgument();
        }
        Integer discountMember = grouponRules.getDiscountMember();
        if (discountMember == null) {
            return ResponseUtil.badArgument();
        }
        LocalDateTime expireTime = grouponRules.getExpireTime();
        if (expireTime == null) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:groupon:update")
    @RequiresPermissionsDesc(menu={"推广管理" , "团购管理"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody MymallGrouponRules grouponRules) {
        Object error = validate(grouponRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = grouponRules.getGoodsId();
        MymallGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        grouponRules.setGoodsName(goods.getName());
        grouponRules.setPicUrl(goods.getPicUrl());

        if (rulesService.updateById(grouponRules) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:groupon:create")
    @RequiresPermissionsDesc(menu={"推广管理" , "团购管理"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody MymallGrouponRules grouponRules) {
        Object error = validate(grouponRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = grouponRules.getGoodsId();
        MymallGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        grouponRules.setGoodsName(goods.getName());
        grouponRules.setPicUrl(goods.getPicUrl());

        rulesService.createRules(grouponRules);

        return ResponseUtil.ok(grouponRules);
    }

    @RequiresPermissions("admin:groupon:delete")
    @RequiresPermissionsDesc(menu={"推广管理" , "团购管理"}, button="删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody MymallGrouponRules grouponRules) {
        Integer id = grouponRules.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        rulesService.delete(id);
        return ResponseUtil.ok();
    }
}
