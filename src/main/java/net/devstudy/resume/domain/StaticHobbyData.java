package net.devstudy.resume.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "staticHobbyData")
public class StaticHobbyData implements Serializable {
	
	private static final long serialVersionUID = -4937799992479955679L;

	@Id
	private String id;
	
	private String idHobby;

	private String icon;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdHobby() {
		return idHobby;
	}

	public void setIdHobby(String idHobby) {
		this.idHobby = idHobby;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}