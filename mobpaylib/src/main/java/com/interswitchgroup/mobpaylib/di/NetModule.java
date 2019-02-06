package com.interswitchgroup.mobpaylib.di;

import android.os.Build;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interswitchgroup.mobpaylib.BuildConfig;
import com.interswitchgroup.mobpaylib.api.utils.TLSSocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    private final String LOG_TAG = this.getClass().getSimpleName();
    String mBaseUrl;

    public NetModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
    }


    @Provides
    @Singleton
    TLSSocketFactory provideTlsSocketFactory() {
        try {
            return new TLSSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(TLSSocketFactory tlsV1point2factory) {
        HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient
                .Builder()
                .addInterceptor(httpLogger)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        // Add headers so we get Android version and Paystack Library version
                        Request.Builder builder = original.newBuilder()
                                .header("User-Agent", "Android_" + Build.VERSION.SDK_INT + "_Paystack_" + BuildConfig.VERSION_NAME)
                                .header("Accept", "application/json")
                                .method(original.method(), original.body());
                        Request request = builder.build();

                        return chain.proceed(request);
                    }
                })
                .sslSocketFactory(tlsV1point2factory, tlsV1point2factory.getX509TrustManager())
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
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
