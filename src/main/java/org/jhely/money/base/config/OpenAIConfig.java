package org.jhely.money.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openai.springboot.OpenAIClientCustomizer;

@Configuration
public class OpenAIConfig {
	
	@Bean
    public OpenAIClientCustomizer customizer() {
        return builder -> builder.maxRetries(3);
    }

//    @Bean
//    public ChatClient chatClient(OpenAiChatModel model) {
//        return ChatClient.builder(model).build();
//    }
//
//    @Bean
//    public OpenAiChatModel openAiChatModel(OpenAiApi openAiApi) {
//        // You can set the model name via properties or here.
//        return new OpenAiChatModel(openAiApi);
//    }
//
//    @Bean
//    public OpenAiApi openAiApi(org.springframework.core.env.Environment env) {
//        return new OpenAiApi(env.getProperty("spring.ai.openai.api-key"));
//    }
}

