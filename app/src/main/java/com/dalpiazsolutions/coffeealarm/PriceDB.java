package com.dalpiazsolutions.coffeealarm;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dalpiazsolutions.coffeealarm.model.Item;
import com.dalpiazsolutions.coffeealarm.dao.ItemDAO;

@Database(entities = {Item.class}, version = 1)
public abstract class PriceDB extends RoomDatabase {
    public abstract ItemDAO getItemDAO();
}
