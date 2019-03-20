package org.mochou.mymall.db.service;

import com.github.pagehelper.PageHelper;
import org.mochou.mymall.db.dao.MymallOrderMapper;
import org.mochou.mymall.db.dao.OrderMapper;
import org.mochou.mymall.db.domain.MymallOrder;
import org.mochou.mymall.db.domain.MymallOrderExample;
import org.mochou.mymall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MymallOrderService {
    @Resource
    private MymallOrderMapper mymallOrderMapper;
    @Resource
    private OrderMapper orderMapper;

    public int add(MymallOrder order) {
        order.setAddTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return mymallOrderMapper.insertSelective(order);
    }

    public int count(Integer userId) {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return (int) mymallOrderMapper.countByExample(example);
    }

    public MymallOrder findById(Integer orderId) {
        return mymallOrderMapper.selectByPrimaryKey(orderId);
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public int countByOrderSn(Integer userId, String orderSn) {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int) mymallOrderMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateOrderSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(userId, orderSn) != 0) {
            orderSn = getRandomNum(6);
        }
        return orderSn;
    }

    public List<MymallOrder> queryByOrderStatus(Integer userId, List<Short> orderStatus, Integer page, Integer size) {
        MymallOrderExample example = new MymallOrderExample();
        example.setOrderByClause(MymallOrder.Column.addTime.desc());
        MymallOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andOrderStatusIn(orderStatus);
        }
        criteria.andDeletedEqualTo(false);
        PageHelper.startPage(page, size);
        return mymallOrderMapper.selectByExample(example);
    }

    public List<MymallOrder> querySelective(Integer userId, String orderSn, List<Short> orderStatusArray, Integer page, Integer size, String sort, String order) {
        MymallOrderExample example = new MymallOrderExample();
        MymallOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andOrderStatusIn(orderStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return mymallOrderMapper.selectByExample(example);
    }

    public int updateWithOptimisticLocker(MymallOrder order) {
        LocalDateTime preUpdateTime = order.getUpdateTime();
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.updateWithOptimisticLocker(preUpdateTime, order);
    }

    public void deleteById(Integer id) {
        mymallOrderMapper.logicalDeleteByPrimaryKey(id);
    }

    public int count() {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andDeletedEqualTo(false);
        return (int) mymallOrderMapper.countByExample(example);
    }

    public List<MymallOrder> queryUnpaid() {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_CREATE).andDeletedEqualTo(false);
        return mymallOrderMapper.selectByExample(example);
    }

    public List<MymallOrder> queryUnconfirm() {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_SHIP).andShipTimeIsNotNull().andDeletedEqualTo(false);
        return mymallOrderMapper.selectByExample(example);
    }

    public MymallOrder findBySn(String orderSn) {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return mymallOrderMapper.selectOneByExample(example);
    }

    public Map<Object, Object> orderInfo(Integer userId) {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        List<MymallOrder> orders = mymallOrderMapper.selectByExampleSelective(example, MymallOrder.Column.orderStatus, MymallOrder.Column.comments);

        int unpaid = 0;
        int unship = 0;
        int unrecv = 0;
        int uncomment = 0;
        for (MymallOrder order : orders) {
            if (OrderUtil.isCreateStatus(order)) {
                unpaid++;
            } else if (OrderUtil.isPayStatus(order)) {
                unship++;
            } else if (OrderUtil.isShipStatus(order)) {
                unrecv++;
            } else if (OrderUtil.isConfirmStatus(order) || OrderUtil.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            } else {
                // do nothing
            }
        }

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        orderInfo.put("unpaid", unpaid);
        orderInfo.put("unship", unship);
        orderInfo.put("unrecv", unrecv);
        orderInfo.put("uncomment", uncomment);
        return orderInfo;

    }

    public List<MymallOrder> queryComment() {
        MymallOrderExample example = new MymallOrderExample();
        example.or().andCommentsGreaterThan((short) 0).andDeletedEqualTo(false);
        return mymallOrderMapper.selectByExample(example);
    }
}
