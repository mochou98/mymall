package org.mochou.mymall.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallGoodsMapper;
import org.mochou.mymall.db.dao.MymallGrouponRulesMapper;
import org.mochou.mymall.db.domain.MymallGoods;
import org.mochou.mymall.db.domain.MymallGrouponRules;
import org.mochou.mymall.db.domain.MymallGrouponRulesExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MymallGrouponRulesService {
    @Resource
    private MymallGrouponRulesMapper mapper;
    @Resource
    private MymallGoodsMapper goodsMapper;
    private MymallGoods.Column[] goodsColumns = new MymallGoods.Column[]{MymallGoods.Column.id, MymallGoods.Column.name, MymallGoods.Column.brief, MymallGoods.Column.picUrl, MymallGoods.Column.counterPrice, MymallGoods.Column.retailPrice};

    public int createRules(MymallGrouponRules rules) {
        rules.setAddTime(LocalDateTime.now());
        rules.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(rules);
    }

    /**
     * 根据ID查找对应团购项
     *
     * @param id
     * @return
     */
    public MymallGrouponRules queryById(Integer id) {
        MymallGrouponRulesExample example = new MymallGrouponRulesExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return mapper.selectOneByExample(example);
    }

    /**
     * 查询某个商品关联的团购规则
     *
     * @param goodsId
     * @return
     */
    public List<MymallGrouponRules> queryByGoodsId(Integer goodsId) {
        MymallGrouponRulesExample example = new MymallGrouponRulesExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }

    /**
     * 获取首页团购活动列表
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String, Object>> queryList(int offset, int limit) {
        return queryList(offset, limit, "add_time", "desc");
    }

    public List<Map<String, Object>> queryList(int offset, int limit, String sort, String order) {
        MymallGrouponRulesExample example = new MymallGrouponRulesExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(offset, limit);
        List<MymallGrouponRules> grouponRules = mapper.selectByExample(example);

        List<Map<String, Object>> grouponList = new ArrayList<>(grouponRules.size());
        for (MymallGrouponRules rule : grouponRules) {
            Integer goodsId = rule.getGoodsId();
            MymallGoods goods = goodsMapper.selectByPrimaryKeySelective(goodsId, goodsColumns);
            if (goods == null)
                continue;

            Map<String, Object> item = new HashMap<>();
            item.put("goods", goods);
            item.put("groupon_price", goods.getRetailPrice().subtract(rule.getDiscount()));
            item.put("groupon_member", rule.getDiscountMember());
            grouponList.add(item);
        }

        return grouponList;
    }

    /**
     * 判断某个团购活动是否已经过期
     *
     * @return
     */
    public boolean isExpired(MymallGrouponRules rules) {
        return (rules == null || rules.getExpireTime().isBefore(LocalDateTime.now()));
    }

    /**
     * 获取团购活动列表
     *
     * @param goodsId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<MymallGrouponRules> querySelective(String goodsId, Integer page, Integer size, String sort, String order) {
        MymallGrouponRulesExample example = new MymallGrouponRulesExample();
        example.setOrderByClause(sort + " " + order);

        MymallGrouponRulesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.parseInt(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, size);
        return mapper.selectByExample(example);
    }

    public void delete(Integer id) {
        mapper.logicalDeleteByPrimaryKey(id);
    }

    public int updateById(MymallGrouponRules grouponRules) {
        grouponRules.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(grouponRules);
    }
}