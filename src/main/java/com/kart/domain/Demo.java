package com.kart.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Lazy;

import io.swagger.annotations.ApiModel;

@Entity
@Table(name = "demo")
@Lazy(false)
@ApiModel
public class Demo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEMO_MASTER_ID")
	private Integer demoId;

	@Column(name = "DEMO_NAME")
	private String demoName;

	@Column(name = "DEMO_DESCRIPTION")
	private String demoDescription;

	public Integer getDemoId() {
		return demoId;
	}

	public void setDemoId(Integer demoId) {
		this.demoId = demoId;
	}

	public String getDemoName() {
		return demoName;
	}

	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}

	public String getDemoDescription() {
		return demoDescription;
	}

	public void setDemoDescription(String demoDescription) {
		this.demoDescription = demoDescription;
	}

	
	
}
