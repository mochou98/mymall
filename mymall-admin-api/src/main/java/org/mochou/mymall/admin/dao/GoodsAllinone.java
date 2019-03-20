package org.mochou.mymall.admin.dao;

import org.mochou.mymall.db.domain.MymallGoods;
import org.mochou.mymall.db.domain.MymallGoodsAttribute;
import org.mochou.mymall.db.domain.MymallGoodsProduct;
import org.mochou.mymall.db.domain.MymallGoodsSpecification;

public class GoodsAllinone {
    MymallGoods goods;
    MymallGoodsSpecification[] specifications;
    MymallGoodsAttribute[] attributes;
    // 这里采用 Product 再转换到 MymallGoodsProduct
    MymallGoodsProduct[] products;

    public MymallGoods getGoods() {
        return goods;
    }

    public void setGoods(MymallGoods goods) {
        this.goods = goods;
    }

    public MymallGoodsProduct[] getProducts() {
        return products;
    }

    public void setProducts(MymallGoodsProduct[] products) {
        this.products = products;
    }

    public MymallGoodsSpecification[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(MymallGoodsSpecification[] specifications) {
        this.specifications = specifications;
    }

    public MymallGoodsAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(MymallGoodsAttribute[] attributes) {
        this.attributes = attributes;
    }

}
