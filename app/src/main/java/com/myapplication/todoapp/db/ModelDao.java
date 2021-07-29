package com.myapplication.todoapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ModelDao {

    @Query("SELECT * FROM Model")
    List<Model> getAll();

    @Insert
    void insert(Model model);

    @Delete
    void delete(Model model);

    @Update
    void update(Model model);

    @Query("SELECT * FROM model WHERE category LIKE :goal")
    Model findModelByGoal(String goal);

    //getCategoryNames
    @Query("SELECT DISTINCT category FROM Model")
    List<String> getDistinctCategory();

    //filterByStatus
    @Query("SELECT * FROM Model WHERE status in(:status)")
    List<Model> filterByStatus(List<Boolean> status);

    //filterByCategory
    @Query("SELECT * FROM Model WHERE category IN (:category)")
    List<Model> getFilterByCategory(List<String> category);

}
