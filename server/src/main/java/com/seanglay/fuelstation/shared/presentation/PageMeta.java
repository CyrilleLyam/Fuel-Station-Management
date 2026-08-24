package com.seanglay.fuelstation.shared.presentation;

import com.seanglay.fuelstation.shared.domain.PageResult;

public record PageMeta(int page, int size, long totalElements, int totalPages) {

	public static PageMeta from(PageResult<?> result) {
		return new PageMeta(result.page(), result.size(), result.totalElements(), result.totalPages());
	}

}
