package project.players.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.players.entity.Player;
import project.players.exception.ResourceAlreadyExistException;
import project.players.service.PlayerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Player player = new Player();

    @BeforeEach
    public void init() {
        player.setId(1);
        player.setUsername("testUser");
        player.setFirstName("Name");
        player.setLastName("Lastname");
        player.setEmail("user@example.com");
        player.setAccountBalance(new BigDecimal("0"));

    }

    @Test
    void testAddPlayerIsCreated() throws Exception {
        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Player testUser is added successfully"));
    }

    @Test
    void testAddPlayerIsBedRequest() throws Exception {
        Player playerNotValid = new Player(1, "x", "name", "lastNAme", "user@exmple.com", new BigDecimal("0"));

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerNotValid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddPlayerIsResourceAlreadyExists() throws Exception {
        when(playerService.addPlayer(player)).thenReturn(player);
        when(playerService.addPlayer(player)).thenThrow(new ResourceAlreadyExistException("Resource already exists"));

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isConflict());
    }


    @Test
    void testGetAllPlayers() throws Exception {
        when(playerService.getPlayers()).thenReturn(List.of(player));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/players")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("testUser"));
    }

    @Test
    void testGetPlayerIsOk() throws Exception {
        when(playerService.getPlayerById(player.getId())).thenReturn(Optional.of(player));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/players/{id}", player.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testUser"));
    }

    @Test
    void testGetPlayerIsNotFound() throws Exception {
        when(playerService.getPlayerById(0)).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/players/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatePlayerIsOk() throws Exception {
        Integer id = player.getId();
        Player updatedPlayer = new Player(id, "userTest", "name", "lastNAme", player.getEmail(), new BigDecimal("0"));

        when(playerService.getPlayerById(id)).thenReturn(Optional.of(updatedPlayer));
        when(playerService.updatePlayer(any(), any(Player.class))).thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(updatedPlayer.getUsername()));
    }

    @Test
    void testUpdatePlayerIsNotFound() throws Exception {
        Player updatedPlayer = new Player(2, "userTest", "name", "lastNAme", player.getEmail(), new BigDecimal("0"));

        when(playerService.getPlayerById(2)).thenReturn(Optional.ofNullable(null));
        when(playerService.updatePlayer(any(), any(Player.class))).thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andExpect(status().isNotFound());
    }


    @Test
    void testDeletePlayerIsOk() throws Exception {
        when(playerService.getPlayerById(player.getId())).thenReturn(Optional.of(player));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/players/{id}", player.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void testDeletePlayerIsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/players/{id}", player.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}