package com.theokanning.openai.service;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.theokanning.openai.AzureOpenAiApi;
import com.theokanning.openai.OpenAiError;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.chat.ChatCompletionRequest;
import com.theokanning.openai.chat.ChatCompletionResult;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import io.reactivex.Single;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class AzureOpenAiService {

    private static final String BASE_URL = "https://{resource}.openai.azure.com/openai/deployments/{deployment}/";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final ObjectMapper errorMapper = defaultObjectMapper();

    private final AzureOpenAiApi api;
    private final String apiUrl;

    /**
     * Creates a new AzureOpenAiService that wraps OpenAiApi
     *
     * @param apikey OpenAi apikey string "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     */
    public AzureOpenAiService(final String apikey, final String resourceName, final String deploymentName) {
        this(apikey, resourceName, deploymentName, DEFAULT_TIMEOUT);
    }

    /**
     * Creates a new AzureOpenAiService that wraps OpenAiApi
     *
     * @param apikey   OpenAi apikey string "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public AzureOpenAiService(final String apikey, final String resourceName, final String deploymentName, final Duration timeout) {
        this.apiUrl = BASE_URL.replace("{resource}", resourceName).replace("{deployment}", deploymentName);
        //System.out.println("apiUrl: " + apiUrl);
        this.api = buildApi(apiUrl, apikey, timeout);
    }

    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        return execute(api.createChatCompletion(request));
    }

    public CompletionResult createCompletion(CompletionRequest request) {
        return execute(api.createCompletion(request));
    }

    public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
        return execute(api.createEmbeddings(request));
    }

    /**
     * Calls the Open AI api, returns the response, and parses error messages if the request fails
     */
    public static <T> T execute(Single<T> apiCall) {
        try {
            return apiCall.blockingGet();
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();

                OpenAiError error = errorMapper.readValue(errorBody, OpenAiError.class);
                throw new OpenAiHttpException(error, e, e.code());
            } catch (IOException ex) {
                // couldn't parse OpenAI error
                throw e;
            }
        }
    }

    public static AzureOpenAiApi buildApi(String apiUrl, String apikey, Duration timeout) {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(apikey, timeout);
        Retrofit retrofit = defaultRetrofit(apiUrl, client, mapper);

        return retrofit.create(AzureOpenAiApi.class);
    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    public static OkHttpClient defaultClient(String apikey, Duration timeout) {
        return new OkHttpClient.Builder()
                .addInterceptor(new ApikeyInterceptor(apikey))
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    static Retrofit defaultRetrofit(String apiUrl, OkHttpClient client, ObjectMapper mapper) {
        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
