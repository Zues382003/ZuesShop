package com.zuesshopbackend.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuesshop.entity.Country;
import com.zuesshopbackend.setting.Repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;// help transform object Java to Json, else transform Json to Object Java

    @Autowired
    private CountryRepository repo;

    @Test
    @WithMockUser(username = "21521883@gm.uit.edu.vn", password = "chanh382003", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/countries/list";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();//can take chain Json, it can take any chain as html
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);//read is transform json to object Java

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "21521883@gm.uit.edu.vn", password = "chanh382003", roles = "ADMIN")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "Germany";
        String countryCode = "CA";
        Country country = new Country(countryName,countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(country))//write is transform from java to Json to send to server and server user @RequestBody to handle.
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        String jsonResponse = result.getResponse().getContentAsString();//can take any datatype
        Optional<Country> findById = repo.findById(Integer.valueOf(jsonResponse));

        assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "21521883@gm.uit.edu.vn", password = "chanh382003", roles = "ADMIN")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";
        Integer countryId = 7;
        String countryName = "Japan";
        String countryCode = "JP";
        Country country = new Country(countryId,countryName,countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))//write is transform from java to Json to send to server and server user @RequestBody to handle.
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        String jsonResponse = result.getResponse().getContentAsString();//can take any datatype
        Optional<Country> findById = repo.findById(Integer.valueOf(jsonResponse));

        assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "21521883@gm.uit.edu.vn", password = "chanh382003", roles = "ADMIN")
    public void testDeleteCountry() throws Exception {
        Integer countryId = 7;
        String url = "/countries/delete/" + countryId;
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Country> findById = repo.findById(countryId);
        assertThat(findById).isNotPresent();
    }
}
