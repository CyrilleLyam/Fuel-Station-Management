package com.seanglay.fuelstation.accounting.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.seanglay.fuelstation.shared.domain.ValueObject;

@Embeddable
public class JournalLine implements ValueObject {

	private static final int AMOUNT_SCALE = 2;

	private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(AMOUNT_SCALE);

	@Enumerated(EnumType.STRING)
	@Column(name = "account", nullable = false)
	private AccountCode account;

	@Column(name = "debit", nullable = false)
	private BigDecimal debit;

	@Column(name = "credit", nullable = false)
	private BigDecimal credit;

	protected JournalLine() {
	}

	private JournalLine(AccountCode account, BigDecimal debit, BigDecimal credit) {
		this.account = Objects.requireNonNull(account, "Account is required");
		this.debit = debit;
		this.credit = credit;
	}

	public static JournalLine debit(AccountCode account, BigDecimal amount) {
		return new JournalLine(account, normalize(amount), ZERO);
	}

	public static JournalLine credit(AccountCode account, BigDecimal amount) {
		return new JournalLine(account, ZERO, normalize(amount));
	}

	public AccountCode getAccount() {
		return account;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof JournalLine that)) {
			return false;
		}

		return account == that.account && debit.compareTo(that.debit) == 0 && credit.compareTo(that.credit) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account, debit.stripTrailingZeros(), credit.stripTrailingZeros());
	}

	private static BigDecimal normalize(BigDecimal amount) {
		if (amount == null || amount.signum() < 0) {
			throw new IllegalArgumentException("Journal line amount must be zero or greater");
		}
		return amount.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
	}

}
