package com.wellsfargo.rarconsumer.util;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

public class GenericAggregationOperation implements AggregationOperation {

	private String jsonOperation;

	public GenericAggregationOperation(String jsonOperation) {
		this.jsonOperation = jsonOperation;
	}

	public Document toDocument(AggregationOperationContext aggregationOperationContext) {
		return aggregationOperationContext.getMappedObject(Document.parse(jsonOperation));
	}
}
