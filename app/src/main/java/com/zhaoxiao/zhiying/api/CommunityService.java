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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface CommunityService {
    @GET("community/getTrendList")
    Call<Data<PageInfo<Trend>>> getTrendList(@QueryMap Map<String, String> map);
    @GET("community/getCommentList")
    Call<Data<PageInfo<Comment>>> getCommentList(@QueryMap Map<String, String> map);
    @GET("community/getTrend")
    Call<Data<Trend>> getTrend(@Query("trendId") int trendId,@Query("account") String account);
    @GET("community/getTopicList")
    Call<Data<PageInfo<Topic>>> getTopicList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);
    @GET("community/getTopic")
    Call<Data<Topic>> getTopic(@Query("topicId") int topicId,@Query("account") String account);
    @GET("community/getTrendCollectionList")
    Call<Data<PageInfo<Trend>>> getTrendCollectionList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);
    @GET("community/getTopicCollectionList")
    Call<Data<PageInfo<Topic>>> getTopicCollectionList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);
    @GET("community/getTrendHistoryList")
    Call<Data<PageInfo<Trend>>> getTrendHistoryList(@Query("pageNo") int pageNo,@Query("pageSize") int pageSize,@Query("account") String account);
    @GET("community/addTrendRecord")
    Call<Data<Boolean>> addTrendRecord(@Query("account") String account, @Query("trendId") int trendId);
    @POST("community/publishTrend")
    @Multipart
    Call<Data<Boolean>> publishTrend(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> imgFileList);
    @GET("community/deleteTrend")
    Call<Data<Boolean>> deleteTrend(@Query("trendId") int trendId);
    @POST("community/createTopic")
    Call<Data<Boolean>> createTopic(@Body Topic topic);
    @GET("community/like")
    Call<Data<Boolean>> like(@Query("account") String account,@Query("trendId") int trendId,@Query("like") boolean like);
    @GET("community/collect")
    Call<Data<Boolean>> collect(@Query("account") String account,@Query("trendId") int trendId,@Query("collect") boolean collect);
    @GET("community/attention")
    Call<Data<Boolean>> attention(@Query("userAccount") String userAccount,@Query("fanAccount") String trendId,@Query("attention") boolean attention);
    @GET("community/topicCollect")
    Call<Data<Boolean>> topicCollect(@Query("account") String account,@Query("topicId") int topicId,@Query("collect") boolean collect);
    @GET("community/getTrendSearchList")
    Call<Data<PageInfo<Trend>>> getTrendSearchList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,@Query("searchWord") String searchWord, @Query("account") String account);
    @GET("community/sendComment")
    Call<Data<Boolean>> sendComment(@Query("trendId") int trendId, @Query("account") String account, @Query("info") String info);
    @GET("community/likeComment")
    Call<Data<Boolean>> likeComment(@Query("commentId") int commentId, @Query("account") String account, @Query("like") boolean like);
    @GET("community/getTopicSearchList")
    Call<Data<PageInfo<Topic>>> getTopicSearchList(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,@Query("searchWord") String searchWord);
}
