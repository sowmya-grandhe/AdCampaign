package com.sowmya.adserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sowmya.adserver.bean.AdCampaign;
import com.sowmya.adserver.bean.AdCampaignWrapper;
import com.sowmya.adserver.bean.Status;;

@RestController
public class AdServerController {

	// The in memory data structure used to store the ad campaign data.
	// Key is the partner_id and value is the AdcampaignWrapper object which
	// stores the creation time of the ad and the JSON data bean.
	Map<String, AdCampaignWrapper> adCampaignMap = new HashMap<>();

	// The in memory data structure used to store the ad campaign data when
	// multiple ads are allowed per partner
	// Key is the partner_id and value is the list of AdcampaignWrapper objects
	Map<String, List<AdCampaignWrapper>> multipleAdMap = new HashMap<>();

	// This method is to create the ad campaign for a given partner.
	// It also enforces the condition that only one active campaign can exist
	// for a given partner.
	@RequestMapping(value = "/ad", method = RequestMethod.POST, headers = "Accept=application/json")
	public Status createAdCampaign(@RequestBody AdCampaign adCampaign) {

		String partnerId = adCampaign.getPartner_id();
		AdCampaignWrapper adCampaignWrapper = adCampaignMap.get(partnerId);

		if (adCampaignWrapper != null) {
			long durationInMillis = adCampaignWrapper.getAdCampaign().getDuration() * 1000L;

			if (System.currentTimeMillis() > (adCampaignWrapper.getCreationTime() + durationInMillis)) {
				AdCampaignWrapper newAdCampaignWrapper = new AdCampaignWrapper(System.currentTimeMillis(), adCampaign);
				adCampaignMap.put(partnerId, newAdCampaignWrapper);
			} else {
				return new Status("ERROR",
						"An active ad campaign for this partner"
								+ " is already exisiting. Cannot create more then one active"
								+ " ad campain for this partner ");
			}
		} else {
			AdCampaignWrapper newAdCampaignWrapper = new AdCampaignWrapper(System.currentTimeMillis(), adCampaign);
			adCampaignMap.put(partnerId, newAdCampaignWrapper);
		}

		return new Status("SUCCESS", "Ad Campaign created successfully");
	}

	// This method returns the active ad campaign data for the specified partner
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "ad/{partnerId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity getAd(@PathVariable("partnerId") String partnerId) {

		AdCampaignWrapper adCampaignWrapper = adCampaignMap.get(partnerId);

		if (adCampaignWrapper != null) {
			long durationInMillis = adCampaignWrapper.getAdCampaign().getDuration() * 1000L;

			if (System.currentTimeMillis() > (adCampaignWrapper.getCreationTime() + durationInMillis)) {
				return new ResponseEntity(new Status("ERROR", "No active ad campaign exists for the specified partner"),
						HttpStatus.OK);
			}
			return new ResponseEntity(adCampaignWrapper.getAdCampaign(), HttpStatus.OK);
		}
		return new ResponseEntity(new Status("ERROR", "No active ad campaign exists for the specified partner"),
				HttpStatus.OK);
	}
    
	// This method returns all the ad Campaigns created for all partners. (both active and inactive)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "ad/all", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity getAllCampaigns() {
		List<AdCampaign> adList = new ArrayList<>();
		for (AdCampaignWrapper ad : adCampaignMap.values()) {
			adList.add(ad.getAdCampaign());
		}
		if (adList.isEmpty()) {
			return new ResponseEntity(new Status("SUCCESS", "No ad campaigns exist in the system."), HttpStatus.OK);
		}
		return new ResponseEntity(adList, HttpStatus.OK);
	}

	//This method supports creating multiple ads for a specified partner.
	@RequestMapping(value = "/ad/multiple", method = RequestMethod.POST, headers = "Accept=application/json")
	public Status createMultipleAds(@RequestBody AdCampaign adCampaign) {

		String partnerId = adCampaign.getPartner_id();
		List<AdCampaignWrapper> adList = multipleAdMap.get(partnerId);
		if (adList == null) {
			adList = new ArrayList<>();
		}
		AdCampaignWrapper newAdCampaignWrapper = new AdCampaignWrapper(System.currentTimeMillis(), adCampaign);
		adList.add(newAdCampaignWrapper);
		multipleAdMap.put(partnerId, adList);

		return new Status("SUCCESS", "Ad Campaign created successfully");
	}

	//This method supports fetching multiple active ads for any specified partner.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "ads/{partnerId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity getAds(@PathVariable("partnerId") String partnerId) {
		
		List<AdCampaignWrapper> adList = multipleAdMap.get(partnerId);
		
		if (adList != null) {
			for (AdCampaignWrapper adInfo : adList) {
				long durationInMillis = adInfo.getAdCampaign().getDuration() * 1000L;

				if (System.currentTimeMillis() > (adInfo.getCreationTime() + durationInMillis)) {

					adList.remove(adInfo);
				}
			}

		}
		if (adList == null || (adList != null && adList.isEmpty())) {
			return new ResponseEntity(new Status("ERROR", "No active ad campaign exists for the specified partner"),
					HttpStatus.OK);
		}

		return new ResponseEntity(adList, HttpStatus.OK);

	}

}
