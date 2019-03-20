package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallCollectMapper;
import org.mochou.mymall.db.domain.MymallCollect;
import org.mochou.mymall.db.domain.MymallCollectExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallCollectService {
    @Resource
    private MymallCollectMapper collectMapper;

    public int count(int uid, Integer gid) {
        MymallCollectExample example = new MymallCollectExample();
        example.or().andUserIdEqualTo(uid).andValueIdEqualTo(gid).andDeletedEqualTo(false);
        return (int) collectMapper.countByExample(example);
    }

    public List<MymallCollect> queryByType(Integer userId, Byte type, Integer page, Integer size) {
        MymallCollectExample example = new MymallCollectExample();
        example.or().andUserIdEqualTo(userId).andTypeEqualTo(type).andDeletedEqualTo(false);
        example.setOrderByClause(MymallCollect.Column.addTime.desc());
        PageHelper.startPage(page, size);
        return collectMapper.selectByExample(example);
    }

    public int countByType(Integer userId, Byte type) {
        MymallCollectExample example = new MymallCollectExample();
        example.or().andUserIdEqualTo(userId).andTypeEqualTo(type).andDeletedEqualTo(false);
        return (int) collectMapper.countByExample(example);
    }

    public MymallCollect queryByTypeAndValue(Integer userId, Byte type, Integer valueId) {
        MymallCollectExample example = new MymallCollectExample();
        example.or().andUserIdEqualTo(userId).andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        return collectMapper.selectOneByExample(example);
    }

    public void deleteById(Integer id) {
        collectMapper.logicalDeleteByPrimaryKey(id);
    }

    public int add(MymallCollect collect) {
        collect.setAddTime(LocalDateTime.now());
        collect.setUpdateTime(LocalDateTime.now());
        return collectMapper.insertSelective(collect);
    }

    public List<MymallCollect> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        MymallCollectExample example = new MymallCollectExample();
        MymallCollectExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.valueOf(valueId));
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return collectMapper.selectByExample(example);
    }
}
