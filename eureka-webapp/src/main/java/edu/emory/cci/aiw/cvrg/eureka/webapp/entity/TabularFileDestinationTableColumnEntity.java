package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

/*-
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2016 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "tf_dest_tablecolumns")
public class TabularFileDestinationTableColumnEntity {
	@Id
	@SequenceGenerator(name = "TF_DEST_TC_SEQ_GENERATOR",
		sequenceName = "TF_DEST_TC_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "TF_DEST_TC_SEQ_GENERATOR")
	private Long id;
	
	@Column(nullable = false)
	private String tableName;
	
	@Column(nullable = false)
	private String columnName;
	
	@Column(nullable = false)
	private Long rank;
	
	@Column(nullable = false)
	private String path;
	
	@ManyToOne
	@JoinColumn(name="tabularfiledestinations_id", nullable = false)
	private TabularFileDestinationEntity destination;
	
	private String format;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setDestination(TabularFileDestinationEntity inDestination) {
		if (this.destination != inDestination) {
			if (this.destination != null) {
				this.destination.removeTableColumn(this);
			}
			this.destination = inDestination;
			if (this.destination != null) {
				this.destination.addTableColumn(this);
			}
		}
	}
	
	public TabularFileDestinationEntity getDestination() {
		return this.destination;
	}
	
}
