package org.mochou.mymall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.mochou.mymall.db.domain.MymallOrder;

import java.time.LocalDateTime;

public interface OrderMapper {
    int updateWithOptimisticLocker(@Param("lastUpdateTime") LocalDateTime lastUpdateTime, @Param("order") MymallOrder order);
}