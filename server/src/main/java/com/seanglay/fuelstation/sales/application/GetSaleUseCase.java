package com.seanglay.fuelstation.sales.application;

import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.sales.domain.Sale;
import com.seanglay.fuelstation.sales.domain.SaleRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class GetSaleUseCase {

	private final SaleRepository saleRepository;

	public GetSaleUseCase(SaleRepository saleRepository) {
		this.saleRepository = saleRepository;
	}

	@Transactional(readOnly = true)
	public Sale execute(Long id) {
		return saleRepository.findById(id).orElseThrow(() -> new NotFoundException("Sale not found: " + id));
	}

}
