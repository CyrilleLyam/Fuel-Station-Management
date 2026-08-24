package com.seanglay.fuelstation.shared.domain;

import java.io.Serializable;
import java.util.Objects;

public abstract class AggregateRoot<ID extends Serializable> extends BaseEntity {

	public abstract ID getId();

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AggregateRoot<?> that)) {
			return false;
		}

		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
