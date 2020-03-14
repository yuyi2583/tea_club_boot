package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Company;
import com.yuyi.tea.bean.Employee;
import com.yuyi.tea.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @GetMapping("/company")
    public Company getCompany(){
        Company companyInfo = companyService.getCompanyInfo();
        return companyInfo;
    }

    @PutMapping("/company")
    public Company updateCompany(@RequestBody Company company){//TODO no value in parameter company
        System.out.println("update company"+company);
        companyService.updateCompany(company);
        return company;
    }
}
