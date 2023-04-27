package com.zhaoxiao.zhiying.api;

import com.zhaoxiao.zhiying.entity.community.Comment;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.community.Trend;
import com.zhaoxiao.zhiying.entity.mine.CodeResponse;
import com.zhaoxiao.zhiying.entity.mine.Login;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.PageInfo;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface CommunityService {
    @GET("community/getTrendList")
    Call<Data<PageInfo<Trend>>> getTrendList(@QueryMap Map<String, String> map);
    @GET("community/getCommentList")
    Call<Data<PageInfo<Comment>>> getCommentList(@QueryMap Map<String, String> map);
    @GET("community/getTrend")
    Call<Data<Trend>> getTrend(@Query("trendId") int trendId);
    @GET("community/getTopicList")
    Call<Data<PageInfo<Topic>>> getTopicList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);
    @GET("community/getTopic")
    Call<Data<Topic>> getTopic(@Query("topicId") int topicId);
    @GET("community/getTrendCollectionList")
    Call<Data<PageInfo<Trend>>> getTrendCollectionList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);
    @GET("community/getTopicCollectionList")
    Call<Data<PageInfo<Topic>>> getTopicCollectionList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);
    @GET("community/getTrendHistoryList")
    Call<Data<PageInfo<Trend>>> getTrendHistoryList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);
    @GET("community/addTrendRecord")
    Call<Data<Boolean>> addTrendRecord(@Query("account") String account, @Query("trendId") int trendId);
}
