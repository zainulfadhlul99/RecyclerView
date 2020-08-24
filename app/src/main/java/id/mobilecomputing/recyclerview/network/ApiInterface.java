package id.mobilecomputing.recyclerview.network;

import id.mobilecomputing.recyclerview.model.MoviesPopularResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("popular")
    Call<MoviesPopularResponse> getMoviePopular(@Query("api_key") String api_key);

}
