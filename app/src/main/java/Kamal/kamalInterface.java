package Kamal;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface kamalInterface {
    @GET("v6/9bffc5cfa84b8eb7ff3cb758/latest/{currency}")
    Call<JsonObject> getExchangeCurrency(@Path("currency") String currency);
}