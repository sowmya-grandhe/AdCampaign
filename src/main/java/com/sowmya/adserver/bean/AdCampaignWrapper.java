package com.sowmya.adserver.bean;

public class AdCampaignWrapper {
	private long creationTime;
    private AdCampaign adCampaign;
    
	public AdCampaignWrapper(long creationTime, AdCampaign adCampaign) {
	 this.creationTime = creationTime;
	 this.adCampaign = adCampaign;
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public AdCampaign getAdCampaign() {
		return adCampaign;
	}
	public void setAdCampaign(AdCampaign adCampaign) {
		this.adCampaign = adCampaign;
	}
    
    
}
