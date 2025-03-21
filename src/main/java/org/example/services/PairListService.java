package org.example.services;
import org.example.models.PairList;
import org.example.repositories.PairListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PairListService {

    @Autowired
    private PairListRepository pairListRepository;

    public List<PairList> getAllPairs() {
        return pairListRepository.findAll();
    }

    public PairList getPairById(Long id) {
        return pairListRepository.findById(id).orElse(null);
    }

    public PairList createPair(PairList pairList) {
        return pairListRepository.save(pairList);
    }

    public PairList updatePair(Long id, PairList pairListUpdates) {
        PairList existingPair = getPairById(id);
        if (existingPair != null) {
            existingPair.setUrlImage(pairListUpdates.getUrlImage());
            existingPair.setPartInstructions(pairListUpdates.getPartInstructions());
            return pairListRepository.save(existingPair);
        }
        return null;
    }

    public void deletePair(Long id) {
        pairListRepository.deleteById(id);
    }
}
