package org.mochou.mymall.db.service;

import org.mochou.mymall.db.dao.MymallSystemMapper;
import org.mochou.mymall.db.domain.MymallSystem;
import org.mochou.mymall.db.domain.MymallSystemExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MymallSystemConfigService {
    @Resource
    private MymallSystemMapper systemMapper;

    public List<MymallSystem> queryAll() {
        MymallSystemExample example = new MymallSystemExample();
        example.or();
        return systemMapper.selectByExample(example);
    }
}
