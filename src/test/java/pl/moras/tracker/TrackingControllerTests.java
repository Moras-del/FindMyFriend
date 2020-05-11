package pl.moras.tracker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.moras.model.LocationDto;
import pl.moras.services.IAuthService;
import pl.moras.services.ITrackingService;
import pl.moras.services.TrackingService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "principal")
public class TrackingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockBean
    private ITrackingService trackingService;

    @MockBean
    private IAuthService authService;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    void should_enable_tracking() throws Exception {
        mockMvc.perform(put("/tracking/enable"))
                .andExpect(status().isOk());
    }

    @Test
    void disableTracking() throws Exception {
        mockMvc.perform(put("/tracking/disable"))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_location() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(50);
        locationDto.setLongitude(20);
        String requestBody = objectMapper.writeValueAsString(locationDto);

        mockMvc.perform(put("/tracking/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void should_fail_update_location() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setLongitude(200);
        locationDto.setLatitude(-100);
        String requestBody = objectMapper.writeValueAsString(locationDto);

        mockMvc.perform(put("/tracking/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
