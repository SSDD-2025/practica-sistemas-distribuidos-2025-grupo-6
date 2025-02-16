package es.dlj.onlinestore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductType;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class HomeController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String getHome2(Model model) {

        UserInfo user = userComponent.getUser();
        List<Product> newProducts = productService.getProductsByProductType(ProductType.NEW);
        List<Product> reconditionedProducts = productService.getProductsByProductType(ProductType.RECONDITIONED);
        List<Product> secondhandProducts =  productService.getProductsByProductType(ProductType.SECONDHAND);
        
        model.addAttribute("userName", user.getUserName());
        model.addAttribute("newProductList", newProducts);
        model.addAttribute("reconditionedProductList", reconditionedProducts);
        model.addAttribute("secondhandProductList", secondhandProducts);
        model.addAttribute("tags", productService.getAllTags());

		return "home_template";
    }

}
