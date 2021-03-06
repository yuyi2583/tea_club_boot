package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.typehandler.ActivityRuleTypeHandler;
import com.yuyi.tea.typehandler.ClerkTypeHandler;
import com.yuyi.tea.typehandler.CustomerTypeHandler;
import com.yuyi.tea.typehandler.ProductTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface OrderMapper {

    //获取客户订单列表
    @Select("<script>" +
            "select * from orders where customerId=#{customerId} and " +
            "<if test='timeRange.startDate!=-1'> <![CDATA[orderTime>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[orderTime<=#{timeRange.endDate}]]> order by orderTime desc" +
            "</script>")
    @ResultMap(value = "order")
//    @Results(id = "order",
//            value = {
//                    @Result(id = true,column = "uid",property = "uid"),
//                    @Result(column = "customerId",property = "customer",
//                            one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
//                                    fetchType = FetchType.LAZY)),
//                    @Result(column = "clerkId",property = "clerk",
//                            one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getClerk",
//                                    fetchType = FetchType.LAZY)),
//                    @Result(column = "uid",property = "products",
//                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderProducts",
//                                    fetchType = FetchType.LAZY)),
//                    @Result(column = "activityRuleId",property = "activityRule",
//                            one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule",
//                                    fetchType = FetchType.LAZY)),
//                    @Result(column = "trackingId",property = "trackInfo",
//                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getTrackInfo",
//                                    fetchType = FetchType.LAZY)),
//                     @Result(column = "uid",property = "status",
//                             one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderCurrentStatus",
//                                    fetchType = FetchType.LAZY)),
//                    @Result(column = "uid",property = "orderStatusHistory",
//                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderStatusHistory",
//                                    fetchType = FetchType.LAZY)),
//    })
    List<Order> getOrdersByCustomer(int customerId, TimeRange timeRange);

    //根据订单id获取该订单所有产品信息
    @Select("select * from orderProduct where orderId=#{orderId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "productId",property = "product",typeHandler = ProductTypeHandler.class),
//                    one = @One(select="com.yuyi.tea.mapper.ProductMapper.getProduct",
//                            fetchType = FetchType.LAZY)),
            @Result(column = "activityRuleId",property = "activityRule",typeHandler = ActivityRuleTypeHandler.class)
//                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule",
//                            fetchType = FetchType.LAZY)),
    })
    List<OrderProduct> getOrderProducts(int orderId);


    /**
     * 获取未完成的订单列表
     * @return
     */
    @Select("select * from orders where uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status!='complete' and A.status!='refunded')")
    @Results(id="order",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "customerId",property = "customer",typeHandler = CustomerTypeHandler.class),
                    @Result(column = "clerkId",property = "clerk",typeHandler = ClerkTypeHandler.class),
                    @Result(column = "uid",property = "products",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderProducts",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "activityRuleId",property = "activityRule",typeHandler = ActivityRuleTypeHandler.class),
                    @Result(column = "trackingId",property = "trackInfo",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getTrackInfo",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "status",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderCurrentStatus",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "orderStatusHistory",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderStatusHistory",
                                    fetchType = FetchType.LAZY)),
            })
    List<Order> getUncompleteOrders();

    /**
     * 根据条件获取订单列表
     * @param status
     * @param timeRange
     * @return
     */
    @Select("<script>" +
            "select * from orders where " +
            "<if test='status!=\"all\"'> uid in (select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status=#{status}) and</if>" +
            "<if test='timeRange.startDate!=-1'> <![CDATA[orderTime>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[orderTime<=#{timeRange.endDate}]]> order by orderTime desc" +
            "</script>")
    @ResultMap(value = "order")
    List<Order> getOrders(String status, TimeRange timeRange);

    /**
     * 根据uid获取订单详细信息
     * @param uid
     * @return
     */
    @Select("select * from orders where uid=${uid}")
    @ResultMap(value = "order")
    Order getOrder(int uid);

    //获取订单信息
    @Select("select * from trackInfo where uid=#{uid}")
    TrackInfo getTrackInfo(int uid);

    //获取订单状态历史信息
    @Select("select * from orderStatus where orderId=#{orderId} order by time desc")
    List<OrderStatus> getOrderStatusHistory(int orderId);

    //获取订单当前状态
    @Select("select * from orderStatus where orderId=#{orderId} order by time desc limit 1")
    OrderStatus getOrderCurrentStatus(int orderId);

    //将订单状态更新为已发货
    @Insert("insert into orderStatus(orderId,status,time) values(#{orderId},#{status},#{time})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveOrderStatus(OrderStatus status);

    //保存物流信息
    @Insert("insert into trackInfo(companyName,trackingId,description,phone) values(#{companyName},#{trackingId},#{description},#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveTrackInfo(TrackInfo trackInfo);

    //更新orders表的trackingId
    @Update("update orders set trackingId=#{trackInfo.uid} where uid=#{uid}")
    void updateOrderTrackingId(Order order);

    //更新卖家留言
    @Update("update orders set sellerPs=#{sellerPs} where uid=#{uid}")
    void saveSellerPs(Order order);
}
