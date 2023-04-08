package com.theokanning.openai;

import com.theokanning.openai.generation.GenerationRequest;
import com.theokanning.openai.generation.GenerationResult;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TwsAiApi {

    @POST("/generate/")
    Single<GenerationResult> createGeneration(@Body GenerationRequest request);
}
