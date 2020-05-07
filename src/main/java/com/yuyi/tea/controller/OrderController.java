package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Order;
import com.yuyi.tea.bean.Reservation;
import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.common.Amount;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.service.CustomerService;
import com.yuyi.tea.service.OrderService;
import com.yuyi.tea.service.ShopBoxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopBoxMapper shopBoxMapper;

    @Autowired
    private ShopBoxService shopBoxService;

    @Autowired
    private CustomerService customerService;


    /**
     * 获取客户的订单列表
     * @param customerId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/admin/ordersByCustomer/{customerId}/{startDate}/{endDate}")
    public List<Order> getOrdersByCustomer(@PathVariable int customerId,@PathVariable long startDate,@PathVariable long endDate){
       TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Order> ordersByCustomer = orderService.getOrdersByCustomer(customerId, timeRange);
        return ordersByCustomer;
    }

    /**
     * 获取未完成的订单列表
     * @return
     */
    @GetMapping("/admin/uncompleteOrders")
    public List<Order> getUncompleteOrders(){
        List<Order> uncompleteOrders = orderService.getUncompleteOrders();
        return uncompleteOrders;
    }

    /**
     * 根据条件获取订单列表
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/admin/orders/{status}/{startDate}/{endDate}")
    public List<Order> getOrders(@PathVariable String status,@PathVariable long startDate,@PathVariable long endDate){
        TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Order> orders = orderService.getOrders(status, timeRange);
        return orders;
    }

    /**
     * 根据uid获取订单详细信息
     * @param uid
     * @return
     */
    @GetMapping("/admin/order/{uid}")
    public Order getOrder(@PathVariable int uid){
        Order order = orderService.getOrder(uid);
        return order;
    }

    /**
     * 将订单状态更新为已发货
     * @param order
     * @return
     */
    @PutMapping("/admin/ordershipped")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderShipped(@RequestBody Order order){
        Order updatedOrder = orderService.updateOrderShipped(order);
        return updatedOrder;
    }

    /**
     * 卖家退款
     * @param order
     * @return
     */
    @PutMapping("/admin/orderrefunded")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderRefunded(@RequestBody Order order){
        Order updateOrderRefunded = orderService.updateOrderRefunded(order);
        return updateOrderRefunded;
    }

    /**
     * 卖家拒绝买家申请退款
     * @param order
     * @return
     */
    @PutMapping("/admin/orderrejectRefund")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderRejectRefunded(@RequestBody Order order){
        Order updatedOrder = orderService.updateOrderRejectRefunded(order);
        return updatedOrder;
    }

    @PutMapping("/mobile/ordershipped")
    @Transactional(rollbackFor = Exception.class)
    public Order updateMobileOrderShipped(@RequestBody Order order){
        Order updateOrderShipped = orderService.updateMobileOrderShipped(order);
        return updateOrderShipped;
    }

    @PostMapping("/mobile/reserve")
    public Order reserve(@RequestBody Order order){
        try {
            orderService.saveReservation(order);
            //计算总价
            float ingot = 0;
            float credit = 0;
            ShopBox box = shopBoxService.getShopBoxByUid(order.getReservations().get(0).getBoxId());
            final float priceIngot = box.getPrice().getIngot();
            final float priceCredit = box.getPrice().getCredit();
            for (Reservation reservation : order.getReservations()) {
                ingot += priceIngot;
                credit += priceCredit;
            }
            //检查账户余额
            Amount balance = customerService.getCustomerBalance(order.getCustomer().getUid());
            if (balance.getCredit() < credit || balance.getIngot() < ingot) {
                String msg = "所需金额：" + ingot + "元宝 " + credit + "积分\n";
                msg += "当前余额：" + balance.getIngot() + "元宝 " + balance.getCredit() + "积分";
                throw new GlobalException(CodeMsg.INSUFFICIENT_BALANCE(msg));
            }
            //扣除金额
            customerService.pay(ingot, credit, order.getCustomer().getUid());
            return order;
        }catch (GlobalException e){
            throw e;
        }
    }


}
