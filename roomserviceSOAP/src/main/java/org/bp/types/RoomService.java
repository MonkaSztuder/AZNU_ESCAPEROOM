package org.bp.types;

public class RoomService {

	protected String typeOfService;
	protected Integer levelOfService;

	public String getTypeOfService() {
		return typeOfService;
	}

	public void setTypeOfService(String typeOfService) {
		this.typeOfService = typeOfService;
	}

	public Integer getLevelOfService() {
		return levelOfService;
	}

	public void setLevelOfService(Integer levelOfService) {
		this.levelOfService = levelOfService;
	}
}