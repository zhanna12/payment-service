package kz.iitu.pharm.paymentservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.iitu.pharm.paymentservice.entity.Basket;
import kz.iitu.pharm.paymentservice.entity.Drug;
import kz.iitu.pharm.paymentservice.repository.BasketRepository;
import kz.iitu.pharm.paymentservice.repository.DrugRepository;
import kz.iitu.pharm.paymentservice.repository.UserRepository;
import kz.iitu.pharm.paymentservice.service.BasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DrugRepository drugRepository;
    @Autowired
    DrugServiceImpl drugService;

    private Map<Drug, Integer> drugs = new HashMap<>();

//    public List<Basket> getBaskets() throws IOException {
//        String schoolPad = "http://localhost:8081/baskets/";
//        String result = drugService.getResult(schoolPad);
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(result, new TypeReference<List<Basket>>() {
//        });
//    }

    @Override
    public void addDrug(Drug drug) {
        if (drugs.containsKey(drug)) {
            drugs.replace(drug, drugs.get(drug) + 1);
        } else {
            drugs.put(drug, 1);
        }
    }

    @Override
    public void removeDrug(Drug drug) {
        if (drugs.containsKey(drug)) {
            if (drugs.get(drug) > 1)
                drugs.replace(drug, drugs.get(drug) - 1);
            else if (drugs.get(drug) == 1) {
                drugs.remove(drug);
            }
        }
    }

    @Transactional
    public Basket addDrugs(Long drugId){
        Basket basket = new Basket();
        Drug drug = drugRepository.findById(drugId).get();
        return basketRepository.save(basket);
    }

    @Transactional
    public boolean addBasketToUser(Long userId, Long basketId) {
        Basket basket = basketRepository.findById(basketId).get();
        basket.setUser(userRepository.findById(userId).get());
        basketRepository.save(basket);
        return true;
    }

    @Override
    public Map<Drug, Integer> getDrugsInBasket() {
        return  Collections.unmodifiableMap(drugs);
    }

    @Transactional
    public void save(Basket basket){
        basketRepository.save(basket);
    }

    @Transactional
    public void clear(){
        for(Basket b: basketRepository.findAll()){
            b.setUser(null);
            b.setId(null);
            basketRepository.save(b);
        }
        basketRepository.deleteAll();
    }

    @Override
    public BigDecimal getTotal() {
        return drugs.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public void checkout() {
//        Product product;
//        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//            // Refresh quantity for every product before checking
//            product = productRepository.findOne(entry.getKey().getId());
//            if (product.getQuantity() < entry.getValue())
//                throw new NotEnoughProductsInStockException(product);
//            entry.getKey().setQuantity(product.getQuantity() - entry.getValue());
//        }
//        productRepository.save(products.keySet());
//        productRepository.flush();
//        products.clear();
    }
}

