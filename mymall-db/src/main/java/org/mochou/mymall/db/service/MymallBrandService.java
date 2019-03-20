package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallBrandMapper;
import org.mochou.mymall.db.domain.MymallBrand;
import org.mochou.mymall.db.domain.MymallBrand.Column;
import org.mochou.mymall.db.domain.MymallBrandExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallBrandService {
    @Resource
    private MymallBrandMapper brandMapper;
    private Column[] columns = new Column[]{Column.id, Column.name, Column.desc, Column.picUrl, Column.floorPrice};

    public List<MymallBrand> queryVO(int offset, int limit) {
        MymallBrandExample example = new MymallBrandExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);
        return brandMapper.selectByExampleSelective(example, columns);
    }

    public int queryTotalCount() {
        MymallBrandExample example = new MymallBrandExample();
        example.or().andDeletedEqualTo(false);
        return (int) brandMapper.countByExample(example);
    }

    public MymallBrand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    public List<MymallBrand> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        MymallBrandExample example = new MymallBrandExample();
        MymallBrandExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return brandMapper.selectByExample(example);
    }

    public int updateById(MymallBrand brand) {
        brand.setUpdateTime(LocalDateTime.now());
        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    public void deleteById(Integer id) {
        brandMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(MymallBrand brand) {
        brand.setAddTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        brandMapper.insertSelective(brand);
    }

    public List<MymallBrand> all() {
        MymallBrandExample example = new MymallBrandExample();
        example.or().andDeletedEqualTo(false);
        return brandMapper.selectByExample(example);
    }
}
