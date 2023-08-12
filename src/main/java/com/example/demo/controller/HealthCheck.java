package com.example.demo.controller;

import com.example.demo.dao.PaymentDao;
import com.example.demo.model.base.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Log4j2
@Controller
public class HealthCheck {



    @Autowired
    PaymentDao paymentDao;

    @RequestMapping(value = "/check-connection-DB", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> healthCheck() throws Exception {
        BaseResponse response = new BaseResponse();
        response.setMess("SUCCESS");
        Boolean check = true;
        check = paymentDao.getConnectionDB();
        if (!check) {
            log.error("Loi ket noi DB ");
            response.setMess("FAIL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok().body(response);
    }
}