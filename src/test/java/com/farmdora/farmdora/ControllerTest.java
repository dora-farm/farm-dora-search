package com.farmdora.farmdora;

import com.farmdora.farmdora.admin.controller.PopupController;
import com.farmdora.farmdora.admin.controller.UserController;
import com.farmdora.farmdora.admin.service.PopupService;
import com.farmdora.farmdora.admin.service.UserService;
import com.farmdora.farmdora.auth.PrincipalUtil;
import com.farmdora.farmdora.order.controller.OrderController;
import com.farmdora.farmdora.order.service.OrderService;
import com.farmdora.farmdora.opinion.controller.OpinionController;
import com.farmdora.farmdora.opinion.service.OpinionService;
import com.farmdora.farmdora.sale.controller.SaleController;
import com.farmdora.farmdora.sale.controller.SellerSaleController;
import com.farmdora.farmdora.sale.service.SaleService;
import com.farmdora.farmdora.sale.service.SellerSaleService;
import com.farmdora.farmdora.security.TestSecurityConfig;
import com.farmdora.farmdora.security.WithCustomMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        OrderController.class,
        SaleController.class,
        OpinionController.class,
        SellerSaleController.class,
        SaleController.class,
        UserController.class,
        PopupController.class
})
@WithCustomMockUser
@Import({TestSecurityConfig.class})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockitoBean
    protected PrincipalUtil principalUtil;

    @MockitoBean
    protected OrderService orderService;

    @MockitoBean
    protected SaleService saleService;

    @MockitoBean
    protected SellerSaleService sellerSaleService;

    @MockitoBean
    protected OpinionService opinionService;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected PopupService popupService;
}
