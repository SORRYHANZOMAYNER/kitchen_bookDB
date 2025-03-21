package org.example.controllers;

import org.example.models.Feedback;
import org.example.models.KitchenUser;
import org.example.models.PairList;
import org.example.models.Recipe;
import org.example.services.PairListService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api1/v1")
public class PairListController {

    @Autowired
    private PairListService pairListService;
    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public List<PairList> getAllPairs() {
        return pairListService.getAllPairs();
    }

    @GetMapping("/instruction/{id}")
    public ResponseEntity<PairList> getPairById(@PathVariable Long id) {
        PairList pairList = pairListService.getPairById(id);
        if (pairList != null) {
            return new ResponseEntity<>(pairList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/instruction/{id}")
    public ResponseEntity<PairList> createPair(@RequestBody PairList pairList, @PathVariable long id) {
        Recipe recipe = recipeService.findById(id);
        if(recipe != null){
            recipe.addInstructionBlock(pairList);
        }
        PairList createdPairList = pairListService.createPair(pairList);
        return new ResponseEntity<>(createdPairList, HttpStatus.CREATED);
    }

    @PutMapping("/instruction/{id}")
    public ResponseEntity<PairList> updatePair(@PathVariable Long id, @RequestBody PairList pairList) {
        PairList updatedPair = pairListService.updatePair(id, pairList);
        if (updatedPair != null) {
            return new ResponseEntity<>(updatedPair, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/instruction/{id}/{recipeId}")
    public ResponseEntity<Void> deletePair(@PathVariable Long id, @PathVariable Long recipeId) {
        PairList pairList = pairListService.getPairById(id);
        Recipe recipe = recipeService.findById(recipeId);
        if(recipe != null){
            recipe.removeInstructionBlock(pairList);
        }
        pairListService.deletePair(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
