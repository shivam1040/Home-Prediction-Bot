package com.sts.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Api {
	@Id
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Api(String key) {
		super();
		this.key = key;
	}

	public Api() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
