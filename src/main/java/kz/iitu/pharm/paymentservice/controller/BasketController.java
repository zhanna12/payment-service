package kz.iitu.pharm.paymentservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.iitu.pharm.paymentservice.entity.Basket;
import kz.iitu.pharm.paymentservice.entity.Drug;
import kz.iitu.pharm.paymentservice.entity.User;
import kz.iitu.pharm.paymentservice.repository.BasketRepository;
import kz.iitu.pharm.paymentservice.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/baskets")
@Api(value = "Basket Controller class", description = "This class is used for accessing, editing and deleting basket details")
public class BasketController {

    @Autowired
    private DrugServiceImpl drugService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private BasketServiceImpl basketService;

    @Autowired
    private RestTemplate restTemplate;

    private Map<Drug, Integer> drugs = new HashMap<>();

    @GetMapping("")
    @ResponseBody
    public List<Basket> findAll() {
        return basketRepository.findAll();
    }


    @GetMapping("/{id}")
    @ResponseBody
    public Optional<Basket> findBasketById(@PathVariable Long id) {
        return basketRepository.findById(id);
    }

    @GetMapping("/list")
    public Drug[] getAllDrugs() {
        ResponseEntity<Drug[]> response =
                restTemplate.getForEntity(
                        "http://drug-service/drugs/",
                        Drug[].class);
        Drug[] products = response.getBody();
        return products;
    }

    @GetMapping("/user/{userId}")
    public List<Drug> requestAllProducts(@PathVariable Long userId) {
        ResponseEntity<List<Drug>> responseEnties = null;
        List<Drug> response;
        User user = restTemplate.getForObject("http://localhost:8082/users/" + userId, User.class);
        if (user != null) {
            responseEnties = new RestTemplate().exchange(
                    "http://localhost:8080/drugs/",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Drug>>(){});
        }
        response = responseEnties.getBody();
        return response;
    }

    @GetMapping("/addProduct/{productId}")
    public void addProductToCart(@PathVariable("productId") Long productId) {
        drugService.findById(productId).ifPresent(basketService::addDrug);
    }

    @PatchMapping("/add/")
    public void addBasketToUser(@RequestParam(value="userId") Long userId, @RequestParam(value="basketId") Long basketId){
        if(basketService.addBasketToUser(userId,basketId)){
            System.out.println("Basket added to " + userId);
        }
        else{
            System.out.println("basket is already owned");
        }
    }
    @PostMapping("/create/")
    public Basket createBasket(@RequestBody Basket basket) {
        return basketRepository.save(basket);
    }

    @ApiOperation(value = "Method for deleting basket")
    @DeleteMapping("/delete")
    public void clear(){
        basketService.clear();
    }
}