package kz.iitu.pharm.paymentservice.service.impl;

import kz.iitu.pharm.paymentservice.entity.Drug;
import kz.iitu.pharm.paymentservice.repository.DrugRepository;
import kz.iitu.pharm.paymentservice.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DrugServiceImpl implements DrugService {
    @Autowired
    DrugRepository drugRepository;

    @Override
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    @Override
    public Optional<Drug> findById(Long id) {
        return drugRepository.findById(id);
    }

    @Override
    public Drug getDrug(Long drugId){
        return drugRepository.getOne(drugId);
    }

    @Override
    public Drug saveItem(Drug drug) {
        return drugRepository.save(drug);
    }


    @Transactional
    public boolean addDrug(Drug drugname) {
        if (drugRepository.findByName(drugname.getName()) != null) {
          //  drugname.setId(Long.MIN_VALUE);
            System.out.println("Error");
            return false;
        }
        drugRepository.save(drugname);
        return true;
    }
}
