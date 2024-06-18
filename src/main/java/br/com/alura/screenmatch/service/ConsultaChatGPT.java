package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsultaChatGPT {
        public static String obterTraducao(String texto){
            String secret = System.getenv("api.security.openai");
        OpenAiService service = new OpenAiService(secret);
        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("Traduza para o português este texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();
        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}
