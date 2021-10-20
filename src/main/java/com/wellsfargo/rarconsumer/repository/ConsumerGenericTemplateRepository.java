package com.wellsfargo.rarconsumer.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ConsumerGenericTemplate;

public interface ConsumerGenericTemplateRepository extends MongoRepository<ConsumerGenericTemplate, String>{

	Optional<List<ConsumerGenericTemplate>> findByExamTypeIDIn(List<Long> examTypeIds);
}
