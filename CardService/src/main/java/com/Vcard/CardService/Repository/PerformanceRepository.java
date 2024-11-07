package com.Vcard.CardService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.CardService.DTO.PerfDataDto;
import com.Vcard.CardService.Entity.PerfData;



@Repository
public interface PerformanceRepository extends JpaRepository<PerfData, Integer>  {

	PerfDataDto findByAssociateId(Integer associateId);

	PerfData getPerformaceById(Integer perf);

}
