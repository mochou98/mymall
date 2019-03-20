package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallCategoryMapper;
import org.mochou.mymall.db.domain.MymallCategory;
import org.mochou.mymall.db.domain.MymallCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallCategoryService {
    @Resource
    private MymallCategoryMapper categoryMapper;
    private MymallCategory.Column[] CHANNEL = {MymallCategory.Column.id, MymallCategory.Column.name, MymallCategory.Column.iconUrl};

    public List<MymallCategory> queryL1WithoutRecommend(int offset, int limit) {
        MymallCategoryExample example = new MymallCategoryExample();
        example.or().andLevelEqualTo("L1").andNameNotEqualTo("推荐").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<MymallCategory> queryL1(int offset, int limit) {
        MymallCategoryExample example = new MymallCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<MymallCategory> queryL1() {
        MymallCategoryExample example = new MymallCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<MymallCategory> queryByPid(Integer pid) {
        MymallCategoryExample example = new MymallCategoryExample();
        example.or().andPidEqualTo(pid).andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<MymallCategory> queryL2ByIds(List<Integer> ids) {
        MymallCategoryExample example = new MymallCategoryExample();
        example.or().andIdIn(ids).andLevelEqualTo("L2").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public MymallCategory findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<MymallCategory> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        MymallCategoryExample example = new MymallCategoryExample();
        MymallCategoryExample.Criteria criteria = example.createCriteria();

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
        return categoryMapper.selectByExample(example);
    }

    public int updateById(MymallCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void deleteById(Integer id) {
        categoryMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(MymallCategory category) {
        category.setAddTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertSelective(category);
    }

    public List<MymallCategory> queryChannel() {
        MymallCategoryExample example = new MymallCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example, CHANNEL);
    }
}
