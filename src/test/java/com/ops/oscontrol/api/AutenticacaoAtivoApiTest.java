package com.ops.oscontrol.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AutenticacaoAtivoApiTest {
    @Autowired MockMvc mockMvc;

    @Test
    void deveAutenticarEIsolarAtivosEntreEmpresas() throws Exception {
        String sufixo = UUID.randomUUID().toString();
        String tokenA = registrar("Empresa A", "a-" + sufixo + "@teste.local");
        String tokenB = registrar("Empresa B", "b-" + sufixo + "@teste.local");
        criarAtivo(tokenA, "A-" + sufixo, "Caminhão A");
        criarAtivo(tokenB, "B-" + sufixo, "Máquina B");

        mockMvc.perform(get("/api/ativos").header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].descricao").value("Caminhão A"))
                .andExpect(jsonPath("$[1]").doesNotExist());
        mockMvc.perform(get("/api/ativos").header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].descricao").value("Máquina B"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void deveRejeitarAcessoSemTokenELoginInvalido() throws Exception {
        String email = "admin-" + UUID.randomUUID() + "@teste.local";
        registrar("Empresa Segura", email);
        mockMvc.perform(get("/api/ativos")).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\",\"senha\":\"incorreta\"}".formatted(email)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha inválidos"));
    }

    private String registrar(String empresa, String email) throws Exception {
        String resposta = mockMvc.perform(post("/api/auth/registrar").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"empresa\":\"%s\",\"nome\":\"Admin\",\"email\":\"%s\",\"senha\":\"Senha@123\"}"
                                .formatted(empresa, email)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(resposta, "$.accessToken");
    }

    private void criarAtivo(String token, String codigo, String descricao) throws Exception {
        mockMvc.perform(post("/api/ativos").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"%s\",\"descricao\":\"%s\",\"tipo\":\"VEICULO\"}"
                                .formatted(codigo, descricao)))
                .andExpect(status().isCreated());
    }
}
