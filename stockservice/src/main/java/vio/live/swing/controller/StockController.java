package vio.live.swing.controller;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
@RequestMapping("/rest/stock")
public class StockController {

    @RequestMapping("/{userName}/getstock")
    public String getStockByQuote() {

       return "load balancer is executed";
    }

    @RequestMapping("/admin")
    public String homeAdmin() {
        return "This is the admin area of Gallery service running at port: ";
    }




}
