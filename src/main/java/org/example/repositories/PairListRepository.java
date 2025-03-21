package org.example.repositories;

import org.example.models.PairList;
import org.example.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PairListRepository extends JpaRepository<PairList,Long>, JpaSpecificationExecutor<Recipe> {
}
