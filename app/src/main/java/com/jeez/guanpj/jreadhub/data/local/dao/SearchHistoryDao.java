package com.jeez.guanpj.jreadhub.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.jeez.guanpj.jreadhub.bean.SearchHistoryBean;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY time DESC")
    Flowable<List<SearchHistoryBean>> getAllHistory();

    @Query("SELECT * FROM search_history WHERE keyWord = :keyWord")
    Single<SearchHistoryBean> getSingleHistory(@NonNull String keyWord);

    @Query("SELECT id AS _id, keyWord, time FROM search_history WHERE keyWord Like '%' || :keyWord || '%'")
    Cursor getHistoryCursor(@NonNull String keyWord);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(SearchHistoryBean... searchHistoryBeans);

    @Update
    int updateHistory(SearchHistoryBean searchHistoryBean);

    @Query("DELETE FROM search_history Where id = :historyId")
    void deleteHistoryById(@NonNull String historyId);

    @Delete
    void deleteHistory(SearchHistoryBean searchHistoryBean);

    @Query("DELETE FROM search_history")
    void deleteAllHistory();
}
