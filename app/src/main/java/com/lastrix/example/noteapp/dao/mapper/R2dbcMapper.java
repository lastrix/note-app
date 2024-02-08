package com.lastrix.example.noteapp.dao.mapper;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.function.BiFunction;


public interface R2dbcMapper<E> extends BiFunction<Row, RowMetadata, E> {

}
