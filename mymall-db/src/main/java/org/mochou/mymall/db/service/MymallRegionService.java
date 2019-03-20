package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallRegionMapper;
import org.mochou.mymall.db.domain.MymallRegion;
import org.mochou.mymall.db.domain.MymallRegionExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MymallRegionService {

    @Resource
    private MymallRegionMapper regionMapper;

    public List<MymallRegion> getAll(){
        MymallRegionExample example = new MymallRegionExample();
        byte b = 4;
        example.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(example);
    }

    public List<MymallRegion> queryByPid(Integer parentId) {
        MymallRegionExample example = new MymallRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public MymallRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    public List<MymallRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        MymallRegionExample example = new MymallRegionExample();
        MymallRegionExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return regionMapper.selectByExample(example);
    }
}
