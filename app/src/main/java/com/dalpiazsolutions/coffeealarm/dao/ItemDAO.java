package com.dalpiazsolutions.coffeealarm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dalpiazsolutions.coffeealarm.model.Item;

import java.util.List;

@Dao
public interface ItemDAO {
    @Insert
    public void insert(Item... items);
    @Update
    public void update(Item... items);
    @Delete
    public void delete(Item item);
    @Query("SELECT * FROM prices")
    public List<Item> getItems();
    @Query("SELECT * FROM prices WHERE id = :id")
    public Item getItemById(Long id);
    @Query("DELETE FROM prices")
    public void nukeTable();
}
