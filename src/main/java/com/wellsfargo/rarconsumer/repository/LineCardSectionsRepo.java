package com.wellsfargo.rarconsumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.LinecardSections;

public interface LineCardSectionsRepo extends MongoRepository<LinecardSections, Long> {
	LinecardSections findFirstBySectionID(Long sectionID);
	LinecardSections findBySectionID(long sectionID);
}
