package org.mochou.mymall.admin.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mochou.mymall.db.domain.MymallCoupon;
import org.mochou.mymall.db.domain.MymallCouponUser;
import org.mochou.mymall.db.service.MymallCouponService;
import org.mochou.mymall.db.service.MymallCouponUserService;
import org.mochou.mymall.db.util.CouponConstant;
import org.mochou.mymall.db.util.CouponUserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 检测优惠券过期情况
 */
@Component
public class CouponJob {
    private final Log logger = LogFactory.getLog(CouponJob.class);

    @Autowired
    private MymallCouponService couponService;
    @Autowired
    private MymallCouponUserService couponUserService;

    /**
     * 每隔一个小时检查
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void checkCouponExpired() {
        logger.info("系统开启任务检查优惠券是否已经过期");

        List<MymallCoupon> couponList = couponService.queryExpired();
        for(MymallCoupon coupon : couponList){
            coupon.setStatus(CouponConstant.STATUS_EXPIRED);
            couponService.updateById(coupon);
        }

        List<MymallCouponUser> couponUserList = couponUserService.queryExpired();
        for(MymallCouponUser couponUser : couponUserList){
            couponUser.setStatus(CouponUserConstant.STATUS_EXPIRED);
            couponUserService.update(couponUser);
        }
    }

}
