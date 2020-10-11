package kz.iitu.pharm.paymentservice.repository;

import kz.iitu.pharm.paymentservice.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket,Long> {

    Basket save(Basket basket);
    List<Basket> findBasketById(Long userId);
    Optional<Basket> findById(Long id);
}

