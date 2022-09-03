package com.surge.vms.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name = "taskfilter")
public class TasklistFilter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "filter_id")
	private Long filterId;

	@Column
	private String filterName;

	@Column
	private String filterDescription;

	@ElementCollection
	@JoinTable(name = "filtercriteria", joinColumns = @JoinColumn(name = "filter_id"))
	@MapKeyColumn(name = "filterKey")
	@Column(name = "filterValue")
	private Map<String, String> filterCriteria = new HashMap<String, String>();

	public TasklistFilter() {
	}

	public Long getFilterId() {
		return filterId;
	}

	public void setFilterId(Long filterId) {
		this.filterId = filterId;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterDescription() {
		return filterDescription;
	}

	public void setFilterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
	}

	public Map<String, String> getFilterCriteria() {
		return filterCriteria;
	}

	public void setFilterCriteria(Map<String, String> filterCriteria) {
		this.filterCriteria = filterCriteria;
	}


}
