package com.seanglay.fuelstation.shared.domain;

import java.util.List;
import java.util.function.Function;

public record CursorPageResult<T>(List<T> content, Long nextCursor, boolean hasNext) {

	public <R> CursorPageResult<R> map(Function<T, R> mapper) {
		return new CursorPageResult<>(content.stream().map(mapper).toList(), nextCursor, hasNext);
	}

}
