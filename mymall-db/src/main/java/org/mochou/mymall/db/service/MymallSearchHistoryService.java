package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallSearchHistoryMapper;
import org.mochou.mymall.db.domain.MymallSearchHistory;
import org.mochou.mymall.db.domain.MymallSearchHistoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallSearchHistoryService {
    @Resource
    private MymallSearchHistoryMapper searchHistoryMapper;

    public void save(MymallSearchHistory searchHistory) {
        searchHistory.setAddTime(LocalDateTime.now());
        searchHistory.setUpdateTime(LocalDateTime.now());
        searchHistoryMapper.insertSelective(searchHistory);
    }

    public List<MymallSearchHistory> queryByUid(int uid) {
        MymallSearchHistoryExample example = new MymallSearchHistoryExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        example.setDistinct(true);
        return searchHistoryMapper.selectByExampleSelective(example, MymallSearchHistory.Column.keyword);
    }

    public void deleteByUid(int uid) {
        MymallSearchHistoryExample example = new MymallSearchHistoryExample();
        example.or().andUserIdEqualTo(uid);
        searchHistoryMapper.logicalDeleteByExample(example);
    }

    public List<MymallSearchHistory> querySelective(String userId, String keyword, Integer page, Integer size, String sort, String order) {
        MymallSearchHistoryExample example = new MymallSearchHistoryExample();
        MymallSearchHistoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andKeywordLike("%" + keyword + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return searchHistoryMapper.selectByExample(example);
    }
}
