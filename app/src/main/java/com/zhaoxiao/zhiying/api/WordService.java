package com.zhaoxiao.zhiying.api;

import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.entity.word.Book;
import com.zhaoxiao.zhiying.entity.word.BookMore;
import com.zhaoxiao.zhiying.entity.word.WordSimple;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface WordService {
    @GET("word/getBookMore")
    Call<Data<BookMore>> getBookMore(@Query("account") String account, @Query("bookId") String bookId);
    @GET("word/getBookList")
    Call<Data<PageInfo<Book>>> getBookList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("type") String type);
    @GET("word/getReviewList")
    Call<Data<PageInfo<WordSimple>>> getReviewList(@Query("account") String account, @Query("num") int num, @Query("bookId") String bookId);
    @GET("word/getLearnList")
    Call<Data<PageInfo<WordSimple>>> getLearnList(@Query("account") String account, @Query("num") int num, @Query("bookId") String bookId);
    @GET("word/addWordRecord")
    Call<Data<Map<String,Integer>>> addWordRecord(@Query("account") String account, @Query("wordId") String wordId, @Query("type") int type, @Query("bookId") String bookId);
    @GET("word/getNeedNum")
    Call<Data<Map<String, Integer>>> getNeedNum(@Query("account") String account, @Query("bookId") String bookId);
    @GET("word/getWordSimplePageInfo")
    Call<Data<PageInfo<WordSimple>>> getWordSimplePageInfo(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("account") String account, @Query("bookId") String bookId);
    @GET("word/getCollectionList")
    Call<Data<PageInfo<WordSimple>>> getCollectionList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("account") String account, @Query("bookId") String bookId);
    @GET("word/collect")
    Call<Data<Boolean>> collect(@Query("account") String account, @Query("wordId") String wordId, @Query("bookId") String bookId, @Query("collect") boolean collect);
    @GET("word/getHistoryList")
    Call<Data<PageInfo<WordSimple>>> getHistoryList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("account") String account, @Query("bookId") String bookId);
}
