package org.mochou.mymall.wx.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mochou.mymall.core.util.JacksonUtil;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.db.domain.MymallFootprint;
import org.mochou.mymall.db.domain.MymallGoods;
import org.mochou.mymall.db.service.MymallFootprintService;
import org.mochou.mymall.db.service.MymallGoodsService;
import org.mochou.mymall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户访问足迹服务
 */
@RestController
@RequestMapping("/wx/footprint")
@Validated
public class WxFootprintController {
    private final Log logger = LogFactory.getLog(WxFootprintController.class);

    @Autowired
    private MymallFootprintService footprintService;
    @Autowired
    private MymallGoodsService goodsService;

    /**
     * 删除用户足迹
     *
     * @param userId 用户ID
     * @param body   请求内容， { id: xxx }
     * @return 删除操作结果
     */
    @PostMapping("delete")
    public Object delete(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }

        Integer footprintId = JacksonUtil.parseInteger(body, "id");
        if (footprintId == null) {
            return ResponseUtil.badArgument();
        }
        MymallFootprint footprint = footprintService.findById(footprintId);

        if (footprint == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!footprint.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        footprintService.deleteById(footprintId);
        return ResponseUtil.ok();
    }

    /**
     * 用户足迹列表
     *
     * @param page 分页页数
     * @param size 分页大小
     * @return 用户足迹列表
     */
    @GetMapping("list")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<MymallFootprint> footprintList = footprintService.queryByAddTime(userId, page, size);
        long count = PageInfo.of(footprintList).getTotal();
        int totalPages = (int) Math.ceil((double) count / size);

        List<Object> footprintVoList = new ArrayList<>(footprintList.size());
        for (MymallFootprint footprint : footprintList) {
            Map<String, Object> c = new HashMap<String, Object>();
            c.put("id", footprint.getId());
            c.put("goodsId", footprint.getGoodsId());
            c.put("addTime", footprint.getAddTime());

            MymallGoods goods = goodsService.findById(footprint.getGoodsId());
            c.put("name", goods.getName());
            c.put("brief", goods.getBrief());
            c.put("picUrl", goods.getPicUrl());
            c.put("retailPrice", goods.getRetailPrice());

            footprintVoList.add(c);
        }


        Map<String, Object> result = new HashMap<>();
        result.put("footprintList", footprintVoList);
        result.put("totalPages", totalPages);
        return ResponseUtil.ok(result);
    }

}