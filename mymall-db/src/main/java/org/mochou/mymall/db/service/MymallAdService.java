package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallAdMapper;
import org.mochou.mymall.db.domain.MymallAd;
import org.mochou.mymall.db.domain.MymallAdExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallAdService {
    @Resource
    private MymallAdMapper adMapper;

    public List<MymallAd> queryIndex() {
        MymallAdExample example = new MymallAdExample();
        example.or().andPositionEqualTo((byte) 1).andDeletedEqualTo(false).andEnabledEqualTo(true);
        return adMapper.selectByExample(example);
    }

    public List<MymallAd> querySelective(String name, String content, Integer page, Integer limit, String sort, String order) {
        MymallAdExample example = new MymallAdExample();
        MymallAdExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(content)) {
            criteria.andContentLike("%" + content + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return adMapper.selectByExample(example);
    }

    public int updateById(MymallAd ad) {
        ad.setUpdateTime(LocalDateTime.now());
        return adMapper.updateByPrimaryKeySelective(ad);
    }

    public void deleteById(Integer id) {
        adMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(MymallAd ad) {
        ad.setAddTime(LocalDateTime.now());
        ad.setUpdateTime(LocalDateTime.now());
        adMapper.insertSelective(ad);
    }

    public MymallAd findById(Integer id) {
        return adMapper.selectByPrimaryKey(id);
    }
}
