package org.springframework.samples.petclinic.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public class OwnerModelPage extends PagedModel<OwnerModel> {

	@JsonProperty("_embedded")
	private EmbeddedOwnerModelList embeddedOwnerModelList;

	@JsonProperty("page")
	private PageMetadata pageMetadata;

	public EmbeddedOwnerModelList getEmbeddedOwnerModelList() {
		return embeddedOwnerModelList;
	}

	public void setEmbeddedOwnerModelList(EmbeddedOwnerModelList embeddedOwnerModelList) {
		this.embeddedOwnerModelList = embeddedOwnerModelList;
	}

	public PageMetadata getMetadata() {
		return pageMetadata;
	}

	public void setMetadata(PageMetadata pageMetadata) {
		this.pageMetadata = pageMetadata;
	}

	public static class EmbeddedOwnerModelList {

		@JsonProperty("ownerModelList")
		private List<OwnerModel> ownerModels;

		public List<OwnerModel> getOwnerModels() {
			return ownerModels;
		}

		public void setOwnerModels(List<OwnerModel> ownerModels) {
			this.ownerModels = ownerModels;
		}
	}
}
