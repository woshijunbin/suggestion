package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 房源名称简拼关系实体
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FYPYZD", schema = "dbo", catalog = "lmpt")
public class Fypyzd implements Serializable{

	@Id
	@Column(name="ID")
	private BigDecimal id; 
	@Column(name="PY")
	private String py;//名称简拼
	@Column(name="CONTENT")
	private String content;//简拼对应中文
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Fypyzd(BigDecimal id, String py, String content) {
		super();
		this.id = id;
		this.py = py;
		this.content = content;
	}
	public Fypyzd() {
		super();
	}
	
	
}
