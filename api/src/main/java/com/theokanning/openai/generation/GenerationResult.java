package com.theokanning.openai.generation;

import com.theokanning.openai.Usage;
import com.theokanning.openai.completion.CompletionChoice;
import lombok.Data;

import java.util.List;

/**
 * An object containing a response from the completion api
 *
 * https://beta.openai.com/docs/api-reference/completions/create
 */
@Data
public class GenerationResult {
    /**
     * A unique id assigned to this completion.
     */
    String id;

    /**
     * The type of object returned, should be "text_completion"
     */
    String method;

    Integer queryId;

    /**
     * A list of generated completions.
     */
    List<String> text;

    /**
     * The API usage for this request
     */
    String totalTimeTaken;
}
