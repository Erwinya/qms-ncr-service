package com.halukkilincer.qms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NcrWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createListAndTransitionNcr() throws Exception {
        String body = """
                {
                  "title": "Thickness out of tolerance",
                  "description": "Lot exceeded upper thickness limit on inspection.",
                  "severity": "HIGH",
                  "lotNumber": "LOT-4821",
                  "partNumber": "WAFER-A",
                  "reportedBy": "qa.engineer"
                }
                """;

        MvcResult created = mockMvc.perform(post("/api/v1/ncrs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("OPEN"))
                .andExpect(jsonPath("$.data.ncrNumber").isNotEmpty())
                .andReturn();

        JsonNode data = objectMapper.readTree(created.getResponse().getContentAsString()).path("data");
        long id = data.path("id").asLong();
        assertThat(data.path("ncrNumber").asText()).startsWith("NCR-");

        mockMvc.perform(get("/api/v1/ncrs").param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(id));

        mockMvc.perform(put("/api/v1/ncrs/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"UNDER_REVIEW","note":"Assigned to process engineering"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UNDER_REVIEW"));

        mockMvc.perform(put("/api/v1/ncrs/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"OPEN"}
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/v1/ncrs/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"CONTAINED"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value("containmentAction is required when moving to CONTAINED"));

        mockMvc.perform(put("/api/v1/ncrs/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status":"CONTAINED",
                                  "containmentAction":"Hold remaining wafers and quarantine lot LOT-4821",
                                  "note":"Containment logged"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CONTAINED"))
                .andExpect(jsonPath("$.data.containmentAction").value(
                        "Hold remaining wafers and quarantine lot LOT-4821"));
    }

    @Test
    void create_RejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/ncrs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"",
                                  "description":"",
                                  "severity":null,
                                  "reportedBy":""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusMessage").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.description").exists())
                .andExpect(jsonPath("$.data.severity").exists())
                .andExpect(jsonPath("$.data.reportedBy").exists());
    }
}
