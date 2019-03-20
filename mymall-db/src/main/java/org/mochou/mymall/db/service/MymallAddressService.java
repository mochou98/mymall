package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallAddressMapper;
import org.mochou.mymall.db.domain.MymallAddress;
import org.mochou.mymall.db.domain.MymallAddressExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallAddressService {
    @Resource
    private MymallAddressMapper addressMapper;

    public List<MymallAddress> queryByUid(Integer uid) {
        MymallAddressExample example = new MymallAddressExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        return addressMapper.selectByExample(example);
    }

    public MymallAddress findById(Integer id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    public int add(MymallAddress address) {
        address.setAddTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.insertSelective(address);
    }

    public int update(MymallAddress address) {
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.updateByPrimaryKeySelective(address);
    }

    public void delete(Integer id) {
        addressMapper.logicalDeleteByPrimaryKey(id);
    }

    public MymallAddress findDefault(Integer userId) {
        MymallAddressExample example = new MymallAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public void resetDefault(Integer userId) {
        MymallAddress address = new MymallAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        MymallAddressExample example = new MymallAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    public List<MymallAddress> querySelective(Integer userId, String name, Integer page, Integer limit, String sort, String order) {
        MymallAddressExample example = new MymallAddressExample();
        MymallAddressExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return addressMapper.selectByExample(example);
    }
}
