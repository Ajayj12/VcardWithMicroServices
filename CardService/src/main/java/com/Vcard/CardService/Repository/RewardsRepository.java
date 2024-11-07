package com.Vcard.CardService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.CardService.Entity.RewardPoints;



@Repository
public interface RewardsRepository extends JpaRepository<RewardPoints, Integer> {

}
