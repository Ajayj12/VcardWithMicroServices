package com.Vcard.CardService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.CardService.Entity.MTransaction;



@Repository
public interface TransactionsRepository extends JpaRepository<MTransaction, Integer> {

}
