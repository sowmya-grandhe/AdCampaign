package com.sowmya.adserver.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowmya.adserver.bean.AdCampaign;
import com.sowmya.adserver.controller.AdServerController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AdServerController.class })
public class AdServerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void createAdTest() throws Exception {
    	
    	AdCampaign adCampaign = new AdCampaign("partner_1", 240, "partner_1 ad content");
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString(adCampaign);

    	mockMvc
        .perform(post("/ad")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    public void getAdTest() throws Exception {
    	
    	mockMvc
        .perform(get("/ad/partner_1")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    public void getAllCampaignsTest() throws Exception {
    	
    	mockMvc
        .perform(get("/ad/all")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
        
    @Test
    public void createMultipleAdsTestCase1() throws Exception {
    	
    	AdCampaign adCampaign = new AdCampaign("partner_1", 240, "partner_1 first ad content");
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString(adCampaign);

    	mockMvc
        .perform(post("/ad/multiple")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("responseStatus").value("SUCCESS"))
        .andExpect(jsonPath("message").value("Ad Campaign created successfully")); 
    }
    
    @Test
    public void createMultipleAdsTestCase2() throws Exception {
    	
    	AdCampaign adCampaign = new AdCampaign("partner_1", 240, "partner_1 second ad content");
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString(adCampaign);

    	mockMvc
        .perform(post("/ad/multiple")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("responseStatus").value("SUCCESS"))
        .andExpect(jsonPath("message").value("Ad Campaign created successfully"));    
    }
    
    @Test
    public void getMultipleAdsTestCase() throws Exception {
    	
    	mockMvc
        .perform(get("/ads/partner_1")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
