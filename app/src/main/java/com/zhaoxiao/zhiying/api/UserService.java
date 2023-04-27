package com.zhaoxiao.zhiying.api;

import com.zhaoxiao.zhiying.entity.mine.CodeResponse;
import com.zhaoxiao.zhiying.entity.mine.Login;
import com.zhaoxiao.zhiying.entity.mine.Plan;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.mine.User;

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
import retrofit2.http.Url;

public interface UserService {

    @GET("user/getList")
    Call<Data<List<User>>> getList();

    @GET("user/getByAccount")
    Call<Data<User>> getByAccount(@Query("account") int account);

    @POST("user/addUser")
    Call<Data<Boolean>> addUser(@Body User user);

    @GET
    Call<ResponseBody> getUrl(@Url String url);

    @POST
    Call<CodeResponse> sendCode (@Url String url, @Body Map<String,String> params);

    @POST("user/loginByCode")
    @FormUrlEncoded
    Call<Data<Login>> loginByCode (@Field("phone") String phone);

    @POST("user/loginByPassword")
    @FormUrlEncoded
    Call<Data<Login>> loginByPassword (@Field("account") String account, @Field("password") String password);

    @POST("user/setPassword")
    @FormUrlEncoded
    Call<Data<Boolean>> setPassword (@Field("account") String account, @Field("password") String password);

    @GET("user/getPlan")
    Call<Data<Plan>> getPlan(@Query("account") String account);

    @GET("user/setPlan")
    Call<Data<Boolean>> setPlan(@Query("account") String account, @Query("plan") long plan);

    @GET("user/addPlanDo")
    Call<Data<Boolean>> addPlanDo(@Query("account") String account, @Query("planDo") long planDo);
}
