package com.zhaoxiao.zhiying.api;

import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.ArticleDetail;
import com.zhaoxiao.zhiying.entity.study.ArticleNote;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.entity.study.Banner;
import com.zhaoxiao.zhiying.entity.study.Channel;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Ftype;
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.entity.test.TestFtype;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StudyService {
    @GET("study/getBanner")
    Call<Data<List<Banner>>> getBanner();

    @GET("study/getRecentList")
    Call<Data<PageInfo<Recent>>> getRecentList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET("study/getHotList")
    Call<Data<PageInfo<Hot>>> getHotList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET("study/getFtypeList")
    Call<Data<List<Ftype>>> getFtypeList();

    @GET("study/getFtypeById")
    Call<Data<Ftype>> getFtypeById(@Query("ftypeId") int ftypeId);

    @GET("study/getChannelList")
    Call<Data<PageInfo<Channel>>> getChannelList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                 @Query("ftypeId") int ftypeId, @Query("stypeId") int stypeId);


    @GET("study/getChannelById")
    Call<Data<Channel>> getChannelById(@Query("channelId") int channelId,@Query("account") String account);

    @GET("study/getArticleList")
    Call<Data<PageInfo<Article>>> getArticleList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                 @Query("channelId") int channelId,
                                                 @Query("title") boolean title, @Query("asc") boolean asc);

    @GET("study/getArticleDetail")
    Call<Data<ArticleDetail>> getArticleDetail(@Query("account") String account, @Query("articleId") int articleId);

    @GET("study/addArticleRecord")
    Call<Data<Boolean>> addArticleRecord(@Query("account") String account, @Query("articleId") int articleId);

    @GET("study/collect")
    Call<Data<Boolean>> collect(@Query("account") String account, @Query("articleId") int articleId, @Query("collect") boolean collect);

    @GET("study/saveNote")
    Call<Data<Boolean>> saveNote(@Query("account") String account, @Query("articleId") int articleId, @Query("info") String info);

    @GET("study/deleteNote")
    Call<Data<Boolean>> deleteNote(@Query("account") String account, @Query("articleId") int articleId);

    @GET("study/getNote")
    Call<Data<ArticleNote>> getNote(@Query("account") String account, @Query("articleId") int articleId);

    @GET("study/getChannelCollectionList")
    Call<Data<PageInfo<Channel>>> getChannelCollectionList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,@Query("account") String account);

    @GET("study/getArticleCollectionList")
    Call<Data<PageInfo<Article>>> getArticleCollectionList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);

    @GET("study/getArticleHistoryList")
    Call<Data<PageInfo<Article>>> getArticleHistoryList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);

    @GET("study/getArticleNoteList")
    Call<Data<PageInfo<ArticleNoteDetail>>> getArticleNoteList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("account") String account);

    @GET("study/channelCollect")
    Call<Data<Boolean>> channelCollect(@Query("account") String account, @Query("channelId") int channelId, @Query("collect") boolean collect);

    @GET("study/getArticleSearchList")
    Call<Data<PageInfo<Article>>> getArticleSearchList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                 @Query("searchWord") String searchWord);
}
