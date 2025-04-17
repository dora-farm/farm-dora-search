package com.farmdora.farmdora;

import com.farmdora.farmdora.order.controller.OrderController;
import com.farmdora.farmdora.order.service.OrderService;
import com.farmdora.farmdora.sale.controller.SaleController;
import com.farmdora.farmdora.sale.controller.SellerSaleController;
import com.farmdora.farmdora.sale.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        OrderController.class,
        SellerSaleController.class,
        SaleController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockitoBean
    protected OrderService orderService;

    @MockitoBean
    protected SaleService saleService;
}
