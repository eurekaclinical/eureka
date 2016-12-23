package edu.emory.cci.aiw.cvrg.eureka.common.entity;

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
@Table(name = "tabularfile_dest_tablecolumns")
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
