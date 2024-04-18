package Kamal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class kamalBuild {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(" https://v6.exchangerate-api.com/v6/9bffc5cfa84b8eb7ff3cb758/latest/USD/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}