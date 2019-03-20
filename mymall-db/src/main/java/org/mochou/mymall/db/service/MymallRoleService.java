package org.mochou.mymall.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallRoleMapper;
import org.mochou.mymall.db.domain.MymallRole;
import org.mochou.mymall.db.domain.MymallRoleExample;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MymallRoleService {
    @Resource
    private MymallRoleMapper roleMapper;


    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        MymallRoleExample example = new MymallRoleExample();
        example.or().andIdIn(Arrays.asList(roleIds)).andEnabledEqualTo(true).andDeletedEqualTo(false);
        List<MymallRole> roleList = roleMapper.selectByExample(example);

        for(MymallRole role : roleList){
            roles.add(role.getName());
        }

        return roles;

    }

    public List<MymallRole> querySelective(String roleName, Integer page, Integer size, String sort, String order) {
        MymallRoleExample example = new MymallRoleExample();
        MymallRoleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(roleName)) {
            criteria.andNameEqualTo("%" + roleName + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return roleMapper.selectByExample(example);
    }

    public MymallRole findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public void add(MymallRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insertSelective(role);
    }

    public void deleteById(Integer id) {
        roleMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(MymallRole role) {
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    public boolean checkExist(String name) {
        MymallRoleExample example = new MymallRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return roleMapper.countByExample(example) != 0;
    }

    public List<MymallRole> queryAll() {
        MymallRoleExample example = new MymallRoleExample();
        example.or().andDeletedEqualTo(false);
        return roleMapper.selectByExample(example);
    }
}
