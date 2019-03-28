package live.vio.swing.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/stock")
@EnableEurekaClient
public class StockController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/{userName}/getstock")
    public List<Quote> getStockByQuote(@PathVariable(value = "userName") String userName) {

        List<String> quoteslist=restTemplate.exchange("http://datamanagement-ms/data/quote/v1/" + userName, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<String>> (){ }).getBody();

        List<Quote> list = new ArrayList<>();
        for (String quote : quoteslist) {
            Stock stock =getStock(quote);
            if(Objects.nonNull(stock)) {
                Quote quote1 = new Quote(quote, stock.getQuote().getPrice());
                list.add(quote1);
            }
        }
        return list;

    }

    public Stock getStock(String quote){
        try {
            return YahooFinance.get(quote);
        } catch (IOException e) {
            e.printStackTrace();
            return new Stock(quote);
        }
    }

    private class Quote {
        private String quote;
        private BigDecimal price;

        public Quote() {
        }

        public Quote(String quote, BigDecimal price) {
            this.quote = quote;
            this.price = price;
        }

        public String getQuote() {
            return quote;
        }

        public Quote setQuote(String quote) {
            this.quote = quote;
            return this;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public Quote setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }
    }

    @RequestMapping("/admin")
    public String homeAdmin() {
        return "This is the admin area of Gallery service running at port: ";
    }
}
