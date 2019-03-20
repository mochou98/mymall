package org.mochou.mymall.db.service;

import org.mochou.mymall.db.dao.MymallPermissionMapper;
import org.mochou.mymall.db.dao.MymallRoleMapper;
import org.mochou.mymall.db.domain.MymallPermission;
import org.mochou.mymall.db.domain.MymallPermissionExample;
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
public class MymallPermissionService {
    @Resource
    private MymallPermissionMapper permissionMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        MymallPermissionExample example = new MymallPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<MymallPermission> permissionList = permissionMapper.selectByExample(example);

        for(MymallPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }


    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        MymallPermissionExample example = new MymallPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<MymallPermission> permissionList = permissionMapper.selectByExample(example);

        for(MymallPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        MymallPermissionExample example = new MymallPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        MymallPermissionExample example = new MymallPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    public void add(MymallPermission mymallPermission) {
        mymallPermission.setAddTime(LocalDateTime.now());
        mymallPermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(mymallPermission);
    }
}
