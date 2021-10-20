package com.wellsfargo.rarconsumer.util;

import java.util.Map;
import java.util.Map.Entry;

public class GenericAggregationUtils {

	static GenericAggregationOperation addAggregateStage(String jsonString){
		return new GenericAggregationOperation(jsonString);
	}
	
	static GenericAggregationOperation addAggregateStage(String jsonString, Map<String, String> params){
		for(Entry<String, String> paramEntrySet: params.entrySet()){
			jsonString = jsonString.replaceAll(paramEntrySet.getKey(), paramEntrySet.getValue());
		}
		if(jsonString.contains("#")){
			throw new IllegalArgumentException("Params map does not contain all required parameters");
		}
		return new GenericAggregationOperation(jsonString);
	}
}
