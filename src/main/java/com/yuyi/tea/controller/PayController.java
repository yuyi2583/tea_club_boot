package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.common.Amount;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.Result;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.CustomerService;
import com.yuyi.tea.service.OrderService;
import com.yuyi.tea.service.interfaces.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PayController {

    @Autowired
    private WebSocketBalanceServer ws;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/mobile/simulatePay/{customerId}/{value}")
    public void simulatePay(@PathVariable int customerId,@PathVariable float value){
        try {
            //添加充值记录
            balanceService.recharge(customerId,value);
            //改变账户余额
            Amount balance = customerService.addBalance(customerId, value);
            //自动扣费所有未付款包厢预约订单，从最新记录开始
            //查询所有未付款预约包厢订单
            List<Order> unpayReserationOrders=orderService.getUnpayReservationOrder(customerId);
            for(Order order:unpayReserationOrders){
                float ingot = order.getIngot();
                float credit = order.getCredit();
                //检查账户余额
                customerService.checkBalance(ingot,credit,balance);
                //扣费
                balance=customerService.pay(ingot,credit,customerId);
                //改变订单状态
                orderService.updateReservationComplete(order);
            }
            Amount currentBalance = customerService.getCustomerBalance(customerId);
            Result result=new Result(currentBalance);
            ws.sendInfo(new Gson().toJson(result), customerId + "");
        }catch (Exception e){
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
    }

    /**
     * 小程序模拟充值
     * @param customerId
     * @param value
     * @return
     */
    @PostMapping("/mp/simulateCharge/{customerId}/{value}")
    public String mpSimulatePay(@PathVariable int customerId,@PathVariable float value){
        try{
            //添加充值记录
            balanceService.recharge(customerId,value);
            //改变账户余额
            Amount balance = customerService.addBalance(customerId, value);
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
        return "success";
    }

    @PostMapping("/mp/pay/{customerId}/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public String mpPay(@PathVariable int customerId,@PathVariable int orderId){
        Order order;
        if(orderId==-1){
            //为最近一次未付款订单付款
            order=orderService.getLatestUnpayOrder(customerId);
        }else{
            order=orderService.getOrder(orderId);
        }
        //查询账户余额
        Amount balance = customerService.getCustomerBalance(customerId);
        float ingot = order.getIngot();
        float credit = order.getCredit();
        //检查账户余额
        customerService.checkBalance(ingot,credit,balance);
        //扣费
        customerService.pay(ingot,credit,customerId);
        //改变订单状态
        orderService.updateOrderPayed(order);
        return "success";
    }
}
