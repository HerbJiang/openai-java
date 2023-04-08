package com.theokanning.openai.generation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * A request for OpenAi to generate a predicted completion for a prompt.
 * All fields are nullable.
 *
 * https://beta.openai.com/docs/api-reference/completions/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenerationRequest {

    /**
     * An optional prompt to complete from
     */
    List<String> text;

    /**
     * The maximum number of tokens to generate. Default 40
     * Requests can use up to 2048 tokens shared between prompt and completion.
     * (One token is roughly 4 characters for normal English text)
     */
    Integer maxNewTokens;

    /**
     * What sampling temperature to use. Higher values means the model will take more risks.
     * Try 0.9 for more creative applications, and 0 (argmax sampling) for ones with a well-defined answer.
     */
    Double temperature;

    /**
     *
     */
    Double topK;

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of
     * the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are
     * considered.
     */
    Double topP;

    /**
     * Number between 0 and 1 (default 1) that penalizes new tokens based on their existing frequency in the text so far.
     * Decreases the model's likelihood to repeat the same line verbatim.
     */
    Double repetitionPenalty;

    /**
     * ....
     */
    Boolean doSample;

    /**
     * Echo back the input in addition to the response
     */
    Boolean removeInputFromOutput;
}
