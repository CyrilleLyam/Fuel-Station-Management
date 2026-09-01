package com.seanglay.fuelstation.sales.application;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.seanglay.fuelstation.product.ProductCatalog;
import com.seanglay.fuelstation.product.ProductSnapshot;
import com.seanglay.fuelstation.sales.PaymentMethod;
import com.seanglay.fuelstation.sales.SaleCompleted;
import com.seanglay.fuelstation.sales.domain.Sale;
import com.seanglay.fuelstation.sales.domain.SaleRepository;
import com.seanglay.fuelstation.shared.application.UseCase;
import com.seanglay.fuelstation.shared.domain.NotFoundException;

@UseCase
public class RecordSaleUseCase {

	private final SaleRepository saleRepository;

	private final ProductCatalog productCatalog;

	private final ApplicationEventPublisher eventPublisher;

	public RecordSaleUseCase(SaleRepository saleRepository, ProductCatalog productCatalog,
			ApplicationEventPublisher eventPublisher) {
		this.saleRepository = saleRepository;
		this.productCatalog = productCatalog;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public Sale execute(Long stationId, Long tankId, Long productId, String attendant, BigDecimal quantity,
			PaymentMethod paymentMethod, Instant soldAt) {
		ProductSnapshot product = productCatalog.findById(productId)
			.orElseThrow(() -> new NotFoundException("Product not found: " + productId));

		if (!product.active()) {
			throw new IllegalStateException("Product is not available for sale: " + product.sku());
		}

		Sale sale = saleRepository.save(new Sale(stationId, tankId, productId, attendant, quantity, product.unitPrice(),
				paymentMethod, soldAt));

		eventPublisher.publishEvent(new SaleCompleted(sale.getReference(), sale.getId(), sale.getStationId(),
				sale.getTankId(), sale.getProductId(), sale.getAttendant(), sale.getQuantity(), sale.getUnitPrice(),
				sale.getTotalAmount(), sale.getPaymentMethod(), sale.getSoldAt()));

		return sale;
	}

}
