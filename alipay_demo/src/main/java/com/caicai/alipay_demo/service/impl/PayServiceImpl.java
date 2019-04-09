package com.caicai.alipay_demo.service.impl;

import com.alipay.api.AlipayApiException;
import com.caicai.alipay_demo.api.Alipay;
import com.caicai.alipay_demo.bean.AlipayBean;
import com.caicai.alipay_demo.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private Alipay alipay;

    @Override
    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {
        return alipay.pay(alipayBean);
    }

}
