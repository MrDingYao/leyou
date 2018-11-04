package com.leyou.user.mapper;

import com.leyou.item.pojo.Address;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 30 16:57
 **/
public interface AddressMapper extends Mapper<Address> {

    /**
     * 将该用户的所有地址设置为非默认
     * @param userId
     */
    @Update("update tb_address set is_default = 0 where user_id = ${userId}")
    void setNotDefault(@Param("userId") Long userId);
}
