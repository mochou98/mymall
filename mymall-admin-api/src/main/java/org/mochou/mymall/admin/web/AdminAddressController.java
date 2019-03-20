package org.mochou.mymall.admin.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.mochou.mymall.admin.annotation.RequiresPermissionsDesc;
import org.mochou.mymall.core.util.ResponseUtil;
import org.mochou.mymall.core.validator.Order;
import org.mochou.mymall.core.validator.Sort;
import org.mochou.mymall.db.domain.MymallAddress;
import org.mochou.mymall.db.service.MymallAddressService;
import org.mochou.mymall.db.service.MymallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/address")
@Validated
public class AdminAddressController {
    private final Log logger = LogFactory.getLog(AdminAddressController.class);

    @Autowired
    private MymallAddressService addressService;
    @Autowired
    private MymallRegionService regionService;

    private Map<String, Object> toVo(MymallAddress address) {
        Map<String, Object> addressVo = new HashMap<>();
        addressVo.put("id", address.getId());
        addressVo.put("userId", address.getUserId());
        addressVo.put("name", address.getName());
        addressVo.put("mobile", address.getMobile());
        addressVo.put("isDefault", address.getIsDefault());
        addressVo.put("provinceId", address.getProvinceId());
        addressVo.put("cityId", address.getCityId());
        addressVo.put("areaId", address.getAreaId());
        addressVo.put("address", address.getAddress());
        String province = regionService.findById(address.getProvinceId()).getName();
        String city = regionService.findById(address.getCityId()).getName();
        String area = regionService.findById(address.getAreaId()).getName();
        addressVo.put("province", province);
        addressVo.put("city", city);
        addressVo.put("area", area);
        return addressVo;
    }

    @RequiresPermissions("admin:address:list")
    @RequiresPermissionsDesc(menu={"用户管理" , "收货地址"}, button="查询")
    @GetMapping("/list")
    public Object list(Integer userId, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        List<MymallAddress> addressList = addressService.querySelective(userId, name, page, limit, sort, order);
        long total = PageInfo.of(addressList).getTotal();

        List<Map<String, Object>> addressVoList = new ArrayList<>(addressList.size());
        for (MymallAddress address : addressList) {
            Map<String, Object> addressVo = toVo(address);
            addressVoList.add(addressVo);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", addressVoList);

        return ResponseUtil.ok(data);
    }
}
