package org.mochou.mymall.db.service;

import org.apache.ibatis.annotations.Param;
import org.mochou.mymall.db.dao.GoodsProductMapper;
import org.mochou.mymall.db.dao.MymallGoodsProductMapper;
import org.mochou.mymall.db.domain.MymallGoodsProduct;
import org.mochou.mymall.db.domain.MymallGoodsProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MymallGoodsProductService {
    @Resource
    private MymallGoodsProductMapper mymallGoodsProductMapper;
    @Resource
    private GoodsProductMapper goodsProductMapper;

    public List<MymallGoodsProduct> queryByGid(Integer gid) {
        MymallGoodsProductExample example = new MymallGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid).andDeletedEqualTo(false);
        return mymallGoodsProductMapper.selectByExample(example);
    }

    public MymallGoodsProduct findById(Integer id) {
        return mymallGoodsProductMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        mymallGoodsProductMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(MymallGoodsProduct goodsProduct) {
        goodsProduct.setAddTime(LocalDateTime.now());
        goodsProduct.setUpdateTime(LocalDateTime.now());
        mymallGoodsProductMapper.insertSelective(goodsProduct);
    }

    public int count() {
        MymallGoodsProductExample example = new MymallGoodsProductExample();
        example.or().andDeletedEqualTo(false);
        return (int) mymallGoodsProductMapper.countByExample(example);
    }

    public void deleteByGid(Integer gid) {
        MymallGoodsProductExample example = new MymallGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid);
        mymallGoodsProductMapper.logicalDeleteByExample(example);
    }

    public int addStock(Integer id, Short num){
        return goodsProductMapper.addStock(id, num);
    }

    public int reduceStock(Integer id, Short num){
        return goodsProductMapper.reduceStock(id, num);
    }
}