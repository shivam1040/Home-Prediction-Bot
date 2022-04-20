/*
 * Mapped 1 to 1 class to DB acting as DAO entity
 */
package com.sts.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Component
@Entity
public class Home {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long  id;
	private Date date;
	private float price;
	private float bedrooms;
	private float bathrooms;
	private float sqft_living;
	private float sqft_lot;
	private float floors;
	private int waterfront;
	private int view;
	@Column(name = "conditions")
	private int condition;
	private float sqft_above;
	private float sqft_basement;
	private int yr_built;
	private int yr_renovated;
	private String street;
	private String city;
	private String statezip;
	private String country;
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public Date getDate() {
		return date;
	}
	
	
	
	public void setDate(String date) {
		
		this.date = Timestamp.valueOf(date);
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public float getBedrooms() {
		return bedrooms;
	}
	
	public void setBedrooms(float bedrooms) {
		this.bedrooms = bedrooms;
	}
	
	public float getBathrooms() {
		return bathrooms;
	}
	
	public void setBathrooms(float bathrooms) {
		this.bathrooms = bathrooms;
	}
	
	public float getSqft_living() {
		return sqft_living;
	}
	
	public void setSqft_living(float sqft_living) {
		this.sqft_living = sqft_living;
	}
	
	public float getSqft_lot() {
		return sqft_lot;
	}
	
	public void setSqft_lot(float sqft_lot) {
		this.sqft_lot = sqft_lot;
	}
	
	public float getFloors() {
		return floors;
	}
	
	public void setFloors(float floors) {
		this.floors = floors;
	}
	
	public int getWaterfront() {
		return waterfront;
	}
	
	public void setWaterfront(int waterfront) {
		this.waterfront = waterfront;
	}
	
	public int getView() {
		return view;
	}
	
	public void setView(int view) {
		this.view = view;
	}
	
	public int getCondition() {
		return condition;
	}
	
	public void setCondition(int condition) {
		this.condition = condition;
	}
	
	public float getSqft_above() {
		return sqft_above;
	}
	
	public void setSqft_above(float sqft_above) {
		this.sqft_above = sqft_above;
	}
	
	public float getSqft_basement() {
		return sqft_basement;
	}
	
	public void setSqft_basement(float sqft_basement) {
		this.sqft_basement = sqft_basement;
	}
	
	public int getYr_built() {
		return yr_built;
	}
	
	public void setYr_built(int yr_built) {
		this.yr_built = yr_built;
	}
	
	public int getYr_renovated() {
		return yr_renovated;
	}
	
	public void setYr_renovated(int yr_renovated) {
		this.yr_renovated = yr_renovated;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getStatezip() {
		return statezip;
	}
	
	public void setStatezip(String statezip) {
		this.statezip = statezip;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Home(long  id,String date, float price, float bedrooms, float bathrooms, float sqft_living, float sqft_lot,
			float floors, int waterfront, int view, int condition, float sqft_above, float sqft_basement, int yr_built,
			int yr_renovated, String street, String city, String statezip, String country) {
		super();
		this.id=id;
		this.date = Timestamp.valueOf(date);
		this.price = price;
		this.bedrooms = bedrooms;
		this.bathrooms = bathrooms;
		this.sqft_living = sqft_living;
		this.sqft_lot = sqft_lot;
		this.floors = floors;
		this.waterfront = waterfront;
		this.view = view;
		this.condition = condition;
		this.sqft_above = sqft_above;
		this.sqft_basement = sqft_basement;
		this.yr_built = yr_built;
		this.yr_renovated = yr_renovated;
		this.street = street;
		this.city = city;
		this.statezip = statezip;
		this.country = country;
	}
	
	@Override
	public String toString() {
		return "Home [date=" + date + ", price=" + price + ", bedrooms=" + bedrooms + ", bathrooms="
				+ bathrooms + ", sqft_living=" + sqft_living + ", sqft_lot=" + sqft_lot + ", floors=" + floors
				+ ", waterfront=" + waterfront + ", view=" + view + ", condition=" + condition + ", sqft_above="
				+ sqft_above + ", sqft_basement=" + sqft_basement + ", yr_built=" + yr_built + ", yr_renovated="
				+ yr_renovated + ", street=" + street + ", city=" + city + ", statezip=" + statezip + ", country="
				+ country + "]";
	}
	
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}
}