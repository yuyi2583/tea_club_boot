package com.yuyi.tea;

import com.auth0.jwt.interfaces.Claim;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.mapper.*;
import com.yuyi.tea.service.LoginService;
import com.yuyi.tea.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.support.JstlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TeaClubBootApplicationTests {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    EnterpriseMapper enterpriseMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    LoginService loginService;

    @Autowired
    AuthorityMapper authorityMapper;

    @Test
    void contextLoads() {
        boolean shops = redisService.exists("shops:shop");
        System.out.println(shops);
    }

}
