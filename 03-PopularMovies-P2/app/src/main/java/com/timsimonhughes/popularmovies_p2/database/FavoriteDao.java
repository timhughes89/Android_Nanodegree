package com.timsimonhughes.popularmovies_p2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.timsimonhughes.popularmovies_p2.model.Movie;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favoritetable")
    LiveData<List<Movie>> loadAllFavorite();

    @Query("SELECT * FROM favoritetable WHERE title = :title")
    List<Movie> loadAll(String title);

    @Query("SELECT * FROM favoritetable")
    List<Movie> loadAllMovies();

    @Query("SELECT * FROM favoritetable WHERE id = :id")
    Movie loadTaskById(int id);

    @Insert
    void insertFavorite(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(Movie movie);

//    @Delete
//    void deleteFavorite(Movie movie);

    @Query("DELETE FROM favoritetable WHERE id = :id")
    void deleteFavoriteWithId(int id);

    @Query("DELETE FROM favoritetable")
    void deleteTable();

//    @Query("SELECT * FROM favoritetable WHERE id = :id")
//    LiveData<Movie> loadFavoriteById(int id);


}
