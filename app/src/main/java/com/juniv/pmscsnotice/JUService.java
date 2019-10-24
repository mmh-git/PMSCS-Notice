package com.juniv.pmscsnotice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

  interface iJUService {
    @Headers("Content-Type: text/plain")
    @GET("discussion/more")
    Call<String> get(@Query("keyword") String keyword, @Query("departmentId")
            String departmentId, @Query("type") String type
            , @Query("formDate") String formDate, @Query("toDate") String toDate, @Query("currentPage") int currentPage);
}
public class JUService implements iJUService {
  private  static Retrofit retrofit=null;
  private  String BASE_URL;
  private final String keyword="";
  private final String departmentId="9";
  private final String type="NOTICE";

  iJUService api;
  public JUService() {
    BASE_URL= "http://juniv.edu.bd";
    if(retrofit==null) {
      retrofit = Config.Create(BASE_URL);
    }
    api= retrofit.create(iJUService.class);
  }
  @Override
  public Call<String> get(String keyword, String departmentId, String type, String formDate, String toDate, int currentPage) {
    return api.get(keyword,departmentId,type,formDate,toDate,currentPage);
  }
  public  Call<String>get(int currentPage)
  {
    return  api.get(keyword,departmentId,type,"","",currentPage);
  }
}
class Config {

  public static Retrofit Create(String baseUrl)
  {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()

            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build();
    Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create());

    return  builder.build();
  }
}