package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Notification;
import com.yuyi.tea.service.interfaces.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 获取客户未读通知
     * @param customerId
     * @return
     */
    @GetMapping("/mp/notice/unread/{customerId}")
    public List<Notification> getUnreadNotifications(@PathVariable int customerId){
        List<Notification> notifications=noticeService.getUnreadNotifications(customerId);
        noticeService.clearNotifications(notifications);
        return notifications;
    }

    /**
     * 分页获取客户通知（20条）
     * @param customerId
     * @param page
     * @return
     */
    @GetMapping("/mp/notice/{customerId}/{page}")
    public List<Notification> getNotifications(@PathVariable int customerId,@PathVariable int page){
        List<Notification> notifications=noticeService.getNotifications(customerId,page);
        noticeService.clearNotifications(notifications);
        return notifications;
    }
}
