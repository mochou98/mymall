package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallCommentMapper;
import org.mochou.mymall.db.domain.MymallComment;
import org.mochou.mymall.db.domain.MymallCommentExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallCommentService {
    @Resource
    private MymallCommentMapper commentMapper;

    public List<MymallComment> queryGoodsByGid(Integer id, int offset, int limit) {
        MymallCommentExample example = new MymallCommentExample();
        example.setOrderByClause(MymallComment.Column.addTime.desc());
        example.or().andValueIdEqualTo(id).andTypeEqualTo((byte) 0).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    public List<MymallComment> query(Byte type, Integer valueId, Integer showType, Integer offset, Integer limit) {
        MymallCommentExample example = new MymallCommentExample();
        example.setOrderByClause(MymallComment.Column.addTime.desc());
        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        } else if (showType == 1) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        } else {
            throw new RuntimeException("showType不支持");
        }
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    public int count(Byte type, Integer valueId, Integer showType) {
        MymallCommentExample example = new MymallCommentExample();
        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        } else if (showType == 1) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        } else {
            throw new RuntimeException("showType不支持");
        }
        return (int) commentMapper.countByExample(example);
    }

    public int save(MymallComment comment) {
        comment.setAddTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        return commentMapper.insertSelective(comment);
    }

    public List<MymallComment> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        MymallCommentExample example = new MymallCommentExample();
        MymallCommentExample.Criteria criteria = example.createCriteria();

        // type=2 是订单商品回复，这里过滤
        criteria.andTypeNotEqualTo((byte) 2);

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.valueOf(valueId)).andTypeEqualTo((byte) 0);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return commentMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        commentMapper.logicalDeleteByPrimaryKey(id);
    }

    public String queryReply(Integer id) {
        MymallCommentExample example = new MymallCommentExample();
        example.or().andTypeEqualTo((byte) 2).andValueIdEqualTo(id);
        List<MymallComment> commentReply = commentMapper.selectByExampleSelective(example, MymallComment.Column.content);
        // 目前业务只支持回复一次
        if (commentReply.size() == 1) {
            return commentReply.get(0).getContent();
        }
        return null;
    }

    public MymallComment findById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }
}
