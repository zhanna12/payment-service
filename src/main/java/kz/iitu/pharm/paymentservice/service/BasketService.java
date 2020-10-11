package kz.iitu.pharm.paymentservice.service;

import kz.iitu.pharm.paymentservice.entity.Basket;
import kz.iitu.pharm.paymentservice.entity.Drug;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface BasketService {
    void addDrug(Drug drug);
    void removeDrug(Drug drug);
    boolean addBasketToUser(Long userId, Long BasketId);
    Map<Drug, Integer> getDrugsInBasket();
    BigDecimal getTotal();

    void checkout();
}
