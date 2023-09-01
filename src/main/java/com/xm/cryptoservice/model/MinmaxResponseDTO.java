package com.xm.cryptoservice.model;

public class MinmaxResponseDTO {

	Double oldest;
	Double newest;
	Double min;
	Double max;

	public Double getOldest() {
		return oldest;
	}

	public void setOldest(Double oldest) {
		this.oldest = oldest;
	}

	public Double getNewest() {
		return newest;
	}

	public void setNewest(Double newest) {
		this.newest = newest;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "MinmaxResponseDTO [oldest=" + oldest + ", newest=" + newest + ", min=" + min + ", max=" + max + "]";
	}

}