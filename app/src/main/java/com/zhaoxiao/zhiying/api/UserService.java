package com.zhaoxiao.zhiying.api;

import com.zhaoxiao.zhiying.entity.Data;
import com.zhaoxiao.zhiying.entity.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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
}
