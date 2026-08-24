package com.seanglay.fuelstation.shared.presentation;

import com.seanglay.fuelstation.shared.domain.CursorPageResult;

public record CursorMeta(Long nextCursor, boolean hasNext, int size) {

	public static CursorMeta from(CursorPageResult<?> result, int size) {
		return new CursorMeta(result.nextCursor(), result.hasNext(), size);
	}

}
