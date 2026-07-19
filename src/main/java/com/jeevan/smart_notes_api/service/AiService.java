package com.jeevan.smart_notes_api.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String summarize(String text) {

        return chatClient.prompt()
                .user("""
                        You are an expert note summarizer.

                        TASK:
                        Create short study notes from the given content.

                        RULES:
                        - Return only the answer.
                        - No introduction.
                        - No conclusion.
                        - No explanation.
                        - No "Here is your summary".
                        - Use simple English.
                        - Focus only on important concepts.
                        - Maximum 8 bullet points.
                        - Student friendly.

                        CONTENT:
                        %s
                        """.formatted(text))
                .call()
                .content();
    }


    public String generateTitle(String text) {

        return chatClient.prompt()
                .user("""
                        Generate 5 professional titles.

                        RULES:
                        - Return only titles.
                        - No numbering.
                        - One title per line.
                        - Maximum 6 words each.
                        - Student friendly.

                        CONTENT:
                        %s
                        """.formatted(text))
                .call()
                .content();
    }


    public String summarizeWithPrompt(
            String text,
            String userPrompt
    ) {

        return chatClient.prompt()
                .user("""
                        You are an expert study notes assistant.

                        USER REQUEST:
                        %s

                        RULES:
                        - Follow the user request exactly.
                        - Return only the final answer.
                        - No introduction.
                        - No conclusion.
                        - No explanation.
                        - Student friendly.
                        - Easy English.

                        FILE CONTENT:
                        %s
                        """.formatted(userPrompt, text))
                .call()
                .content();
    }
}