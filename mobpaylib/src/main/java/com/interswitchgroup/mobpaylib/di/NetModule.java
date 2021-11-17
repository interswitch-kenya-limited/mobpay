package com.interswitchgroup.mobpaylib.di;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String mBaseUrl;
    private final String clientId;
    private final String clientSecret;


    public NetModule(String baseUrl, String clientId, String clientSecret) {
        this.mBaseUrl = baseUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {
        HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient
                .Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    try {
                        // Build signature and headers
                        String timestamp = String.valueOf(new Date().getTime() / 1000);
                        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
                        String encodedClientId = Base64.encodeToString(clientId.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
                        String httpMethod = original.method();
                        String url = original.url().toString();
                        String encodedUrl = URLEncoder.encode(url, "UTF-8");
                        String[] signatureItems = {httpMethod, encodedUrl, timestamp, nonce, clientId, clientSecret};
                        String signatureCipher = TextUtils.join("&", signatureItems);
                        String signatureMethod = "SHA1";
                        MessageDigest messageDigest = MessageDigest.getInstance(signatureMethod);
                        String signature = Base64.encodeToString(messageDigest.digest(signatureCipher.getBytes(StandardCharsets.UTF_8)), Base64.NO_WRAP);

                        // Add headers to request
                        Request.Builder builder = original.newBuilder()
                                .header("User-Agent", "Android_" + Build.VERSION.SDK_INT)
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .header("Nonce", nonce)
                                .header("Timestamp", timestamp)
                                .header("SignatureMethod", signatureMethod)
                                .header("Signature", signature)
                                .header("Authorization", "InterswitchAuth " + encodedClientId)
                                .method(original.method(), original.body());
                        Request request = builder.build();

                        return chain.proceed(request);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return chain.proceed(original);
                    }
                })
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(httpLogger)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
