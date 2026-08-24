package com.seanglay.fuelstation.station.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.seanglay.fuelstation.shared.domain.AggregateRoot;

@Entity
@Table(name = "stations")
public class Station extends AggregateRoot<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "code", nullable = false, unique = true)
	private String code;

	@Column(name = "address")
	private String address;

	@Column(name = "phone")
	private String phone;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	protected Station() {
	}

	public Station(String name, String code, String address, String phone, Double latitude, Double longitude) {
		this.name = name;
		this.code = code;
		this.address = address;
		this.phone = phone;
		this.latitude = latitude;
		this.longitude = longitude;
		this.enabled = true;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void update(String name, String address, String phone, Double latitude, Double longitude) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void enable() {
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
	}

}
