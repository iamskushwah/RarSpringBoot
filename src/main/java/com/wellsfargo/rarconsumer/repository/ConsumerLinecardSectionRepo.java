package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ConsumerLinecardSection;

public interface ConsumerLinecardSectionRepo extends MongoRepository<ConsumerLinecardSection, Long> {

	Optional<ConsumerLinecardSection> findBySectionID(long sectionId);
	
	Optional<List<ConsumerLinecardSection>> findBySectionIDIn(List<Long> sectionIds);
}
