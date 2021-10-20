package com.wellsfargo.rarconsumer.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.wellsfargo.rarconsumer.entity.AuditColl;
import com.wellsfargo.rarconsumer.entity.AuditHistory;

public class AppUtil {

	/**
	 * Cancels the given CompletableFuture.
	 *
	 * @param completableFuture the different CompletableFuture.
	 */
	public static void cancelCompletableFutures(CompletableFuture<?>... completableFuture) {
		for (CompletableFuture cf : completableFuture) {
			cf.cancel(true);
		}
	}
	
	public static boolean checkAlreadyStatusExist(List<AuditHistory> audit,String status) throws RuntimeException {
			if(!CollectionUtils.isEmpty(audit)) {
				boolean statusExist = audit.stream().anyMatch(auditHistoryObj -> auditHistoryObj.getStatus().equalsIgnoreCase(status));
				return statusExist;
			}
		return false;
	}

}
