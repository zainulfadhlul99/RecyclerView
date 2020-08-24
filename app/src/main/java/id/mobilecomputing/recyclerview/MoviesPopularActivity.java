package id.mobilecomputing.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import id.mobilecomputing.recyclerview.adapter.MoviesPopularAdapter;
import id.mobilecomputing.recyclerview.model.MoviesPopularResponse;
import id.mobilecomputing.recyclerview.model.Result;
import id.mobilecomputing.recyclerview.network.ApiClient;
import id.mobilecomputing.recyclerview.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPopularActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final static String API_KEY ="954ce37c352a185f7d1220c29bb61c35";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_popular);


        recyclerView= findViewById(R.id.rv_movies_popular);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesPopularResponse> call = apiInterface.getMoviePopular(API_KEY);
        call.enqueue(new Callback<MoviesPopularResponse>() {
            @Override
            public void onResponse(Call<MoviesPopularResponse> call, Response<MoviesPopularResponse> response) {
                final List<Result> results =response.body().getResults();
                recyclerView.setAdapter(new MoviesPopularAdapter(results, R.layout.item_movies_popular, getApplicationContext()));

                /*perintah klik recyclerview*/
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                        public boolean onSingleTapUp(MotionEvent e){
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)){
                            int position = rv.getChildAdapterPosition(child);
                            /*Toast.makeText(getApplicationContext(), "Id : " + results.get(position).getId() + " selected", Toast.LENGTH_SHORT).show();*/
                            Intent i = new Intent(MoviesPopularActivity.this, DetailActivity.class);
                            i.putExtra("title", results.get(position).getTitle());
                            i.putExtra("date", results.get(position).getReleaseDate());
                            i.putExtra("vote", results.get(position).getVoteAverage().toString());
                            i.putExtra("overview", results.get(position).getOverview());
                            i.putExtra("bg", results.get(position).getPosterPath());
                            MoviesPopularActivity.this.startActivity(i);

                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });

            }

            @Override
            public void onFailure(Call<MoviesPopularResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Get Data Movies Failed", Toast.LENGTH_LONG).show();
            }
        });


    }



}