package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Company;
import com.yuyi.tea.bean.Employee;
import com.yuyi.tea.service.CompanyService;
import com.yuyi.tea.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    RedisService redisService;

    /**
     * 获取公司信息
     * @return
     */
    @GetMapping("/admin/company")
    public Company getCompany(){
       Company companyInfo = companyService.getCompanyInfo();
        return companyInfo;
    }

    /**
     * 修改公司信息
     * @param company
     * @return
     */
    @PutMapping("/admin/company")
    @Transactional(rollbackFor = Exception.class)
    public Company updateCompany(@RequestBody Company company){
        System.out.println("update company"+company);
        companyService.updateCompany(company);
        return company;
    }
}
