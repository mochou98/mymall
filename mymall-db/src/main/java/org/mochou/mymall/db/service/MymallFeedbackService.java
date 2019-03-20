package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallFeedbackMapper;
import org.mochou.mymall.db.domain.MymallFeedback;
import org.mochou.mymall.db.domain.MymallFeedbackExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yogeek
 * @date 2018/8/27 11:39
 */
@Service
public class MymallFeedbackService {
    @Autowired
    private MymallFeedbackMapper feedbackMapper;

    public Integer add(MymallFeedback feedback) {
        feedback.setAddTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.insertSelective(feedback);
    }

    public List<MymallFeedback> querySelective(Integer userId, String username, Integer page, Integer limit, String sort, String order) {
        MymallFeedbackExample example = new MymallFeedbackExample();
        MymallFeedbackExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return feedbackMapper.selectByExample(example);
    }
}
