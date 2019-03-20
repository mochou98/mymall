package org.mochou.mymall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mochou.mymall.db.domain.MymallGoodsSpecification;
import org.mochou.mymall.db.domain.MymallGoodsSpecificationExample;

public interface MymallGoodsSpecificationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    long countByExample(MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int deleteByExample(MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int insert(MymallGoodsSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int insertSelective(MymallGoodsSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    MymallGoodsSpecification selectOneByExample(MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    MymallGoodsSpecification selectOneByExampleSelective(@Param("example") MymallGoodsSpecificationExample example, @Param("selective") MymallGoodsSpecification.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    List<MymallGoodsSpecification> selectByExampleSelective(@Param("example") MymallGoodsSpecificationExample example, @Param("selective") MymallGoodsSpecification.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    List<MymallGoodsSpecification> selectByExample(MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    MymallGoodsSpecification selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") MymallGoodsSpecification.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    MymallGoodsSpecification selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    MymallGoodsSpecification selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MymallGoodsSpecification record, @Param("example") MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MymallGoodsSpecification record, @Param("example") MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MymallGoodsSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MymallGoodsSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByExample(@Param("example") MymallGoodsSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mymall_goods_specification
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByPrimaryKey(Integer id);
}